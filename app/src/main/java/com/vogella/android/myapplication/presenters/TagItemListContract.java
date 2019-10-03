package com.vogella.android.myapplication.presenters;

import android.content.Context;

import com.vogella.android.myapplication.adapters.ExpandableRecyclerViewAdapter;
import com.vogella.android.myapplication.models.TagItem;
import com.vogella.android.myapplication.views.activities.MainActivity;

import java.util.ArrayList;

public interface TagItemListContract {

    interface Presenter {
        void getTagsList(int pageNumber);
        void getItems(String tagName);
    }

    interface TagsView {
        void showLoading(boolean showLoading);
        void showRefresh(boolean showRefresh);
        void updateList(ArrayList<TagItem> items);
        void changeListItem(int index, TagItem item);
        MainActivity getMainActivity();
        Context getContext();
        void setPageNumber(int value);
        ExpandableRecyclerViewAdapter getAdapter();
    }
}
