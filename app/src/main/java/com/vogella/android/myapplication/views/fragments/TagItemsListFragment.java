package com.vogella.android.myapplication.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vogella.android.myapplication.R;
import com.vogella.android.myapplication.adapters.ExpandableRecyclerViewAdapter;
import com.vogella.android.myapplication.models.TagItem;
import com.vogella.android.myapplication.models.TagItemDetails;
import com.vogella.android.myapplication.presenters.TagItemListContract;
import com.vogella.android.myapplication.presenters.TagItemListPresenter;
import com.vogella.android.myapplication.utils.OnChildClickListener;
import com.vogella.android.myapplication.utils.OnParentClickListener;
import com.vogella.android.myapplication.views.activities.MainActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TagItemsListFragment extends Fragment implements TagItemListContract.TagsView {
    private static final String TAG = "TagItemsListFragment";
    private ArrayList<TagItem> tagItems = new ArrayList<>();
    @BindView(R.id.recycleview)
    RecyclerView recyclerView;
    private ExpandableRecyclerViewAdapter menuItemsListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private TagItemListPresenter presenter = new TagItemListPresenter(this);
    private int pageNumber = 1;
    private boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    View v;
    @BindView(R.id.btn_refresh)
    Button refreshButton;

    public TagItemsListFragment() {

    }

    public static TagItemsListFragment newInstance() {
        return new TagItemsListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_tag_items_list, container, false);
        ButterKnife.bind(this, v);
        initAdapter();
        return v;
    }

    private void initAdapter() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        menuItemsListAdapter = new ExpandableRecyclerViewAdapter(tagItems, getContext(),
                getParentListener(), getChildListener());

        recyclerView.setAdapter(menuItemsListAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;

                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    pageNumber++;
                    presenter.getTagsList(pageNumber);
                }
            }
        });
        presenter.getTagsList(pageNumber);
        refreshButton.setOnClickListener(getRefreshListener());
    }

    public View.OnClickListener getRefreshListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNumber++;
                presenter.getTagsList(pageNumber);
            }
        };
    }

    @Override
    public void showLoading(boolean showLoading) {
        if (showLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showRefresh(boolean showRefresh) {
        if (showRefresh) {
            refreshButton.setVisibility(View.VISIBLE);
        } else {
            refreshButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateList(ArrayList<TagItem> items) {
        tagItems.addAll(items);
        menuItemsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void changeListItem(int index, TagItem item) {
        tagItems.remove(index);
        tagItems.add(index, item);
        menuItemsListAdapter.notifyItemChanged(index);
    }

    @Override
    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    @Override
    public void setPageNumber(int value) {
        pageNumber = value;
    }

    @Override
    public ExpandableRecyclerViewAdapter getAdapter() {
        return menuItemsListAdapter;
    }


    public OnChildClickListener getChildListener() {
        return new OnChildClickListener() {
            @Override
            public void onItemClick(int position, Object item, ImageView imageView) {
                ItemDetailsFragment itemDetailsFragment = new ItemDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("details", ((TagItemDetails) item));
                bundle.putString("transition_name", ViewCompat.getTransitionName(imageView));
                itemDetailsFragment.setArguments(bundle);
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.addNewTransition(itemDetailsFragment, imageView);
            }
        };
    }

    public OnParentClickListener getParentListener() {
        return new OnParentClickListener() {
            @Override
            public void onItemClick(Object item) {
                presenter.getItems(((TagItem) item).getTagName());
            }
        };

    }
}
