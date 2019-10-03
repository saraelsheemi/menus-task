package com.vogella.android.myapplication.presenters;

import android.util.Log;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.vogella.android.myapplication.models.TagItem;
import com.vogella.android.myapplication.models.TagItemDetails;
import com.vogella.android.myapplication.models.TagItemDetailsResponse;
import com.vogella.android.myapplication.models.TagListResponse;
import com.vogella.android.myapplication.network.ApiService;
import com.vogella.android.myapplication.network.CacheManager;
import com.vogella.android.myapplication.network.CacheUtils;
import com.vogella.android.myapplication.network.ServiceGenerator;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class TagItemListPresenter implements TagItemListContract.Presenter {
    private static final String TAG = "TagItemListPresenter";

    private TagItemListContract.TagsView tagsView;
    private CompositeDisposable disposable = new CompositeDisposable();
    private ApiService apiService;
    private ArrayList<TagItem> tagItems = new ArrayList<>();
    private CacheManager cacheManager;

    public TagItemListPresenter(TagItemListContract.TagsView tagsView) {
        this.tagsView = tagsView;
        apiService = ServiceGenerator.getClient(tagsView.getContext()).create(ApiService.class);

    }

    @Override
    public void getTagsList(final int pageNumber) {
        cacheManager = tagsView.getMainActivity().getCacheManager();
        tagsView.showLoading(true);
        disposable.add(apiService.loadTags(pageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<TagListResponse>() {
                    @Override
                    public void onSuccess(TagListResponse tagListResponse) {
                        tagsView.showLoading(false);
                        tagsView.showRefresh(false);

                        tagItems.addAll(tagListResponse.getTagItems());
                        tagsView.updateList(tagListResponse.getTagItems());
                        Type type = new TypeToken<ArrayList<TagItem>>() {
                        }.getType();
                        cacheManager.writeJson(tagItems, type, CacheUtils.TAGS);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "OnError");
                        tagsView.showLoading(false);
                        tagsView.setPageNumber(pageNumber - 1);
                        handleError();
                    }
                }));
    }

    private void handleError() {
        Type type = new TypeToken<ArrayList<TagItem>>() {
        }.getType();
        ArrayList<TagItem> cachedItems = (ArrayList<TagItem>) cacheManager.readJson(type, CacheUtils.TAGS);

        if (cachedItems != null && cachedItems.size() > 0) {
            tagsView.getAdapter().clear();
            tagItems.clear();
            tagItems.addAll(cachedItems);
            tagsView.updateList(cachedItems);
            //clear cache
//            cacheManager.writeJson(new ArrayList<TagItem>(), type, CacheUtils.TAGS);
        } else {
            tagsView.showRefresh(true);
        }
    }

    @Override
    public void getItems(final String tagName) {
        cacheManager = tagsView.getMainActivity().getCacheManager();
        disposable.add(apiService.loadItems(tagName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<TagItemDetailsResponse>() {
                    @Override
                    public void onSuccess(TagItemDetailsResponse tagListResponse) {
                        Type type = new TypeToken<ArrayList<TagItemDetails>>() {
                        }.getType();
                        cacheManager.writeJson(tagListResponse.getTagItemDetails(), type, CacheUtils.ITEMS);
                        //expand list
                        updateItemsDetails(tagName, tagListResponse.getTagItemDetails());
                        Log.d(TAG, "success loaded items");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "OnError");
                        Type type = new TypeToken<ArrayList<TagItemDetails>>() {
                        }.getType();
                        ArrayList<TagItemDetails> cachedItems = (ArrayList<TagItemDetails>) cacheManager.readJson(type, CacheUtils.ITEMS);
                        if (cachedItems != null) {
                            updateItemsDetails(tagName, cachedItems);
                            //clear cache
//                            cacheManager.writeJson(new ArrayList<TagItemDetails>(), type, CacheUtils.ITEMS);
                        }

                    }
                }));
    }

    private int findTagNameIndex(String tagName) {
        for (int i = 0; i < tagItems.size(); i++) {
            if (tagItems.get(i).getTagName().contains(tagName))
                return i;

        }
        return -1;
    }

    private void updateItemsDetails(String tagName, ArrayList<TagItemDetails> itemDetails) {
        int tagIndex = findTagNameIndex(tagName);
        String currentTagName = tagItems.get(tagIndex).getTagName();
        if (tagIndex != -1 && (currentTagName.equals(itemDetails.get(0).getName().substring(0,currentTagName.length())))) {
            tagItems.get(tagIndex).setTagItemDetails(itemDetails);
            tagsView.changeListItem(tagIndex, tagItems.get(tagIndex));
        } else {
            Log.d(TAG, "onSuccess: tag name not found");
        }
    }
}
