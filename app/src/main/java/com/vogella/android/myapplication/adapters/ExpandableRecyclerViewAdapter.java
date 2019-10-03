package com.vogella.android.myapplication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vogella.android.myapplication.R;
import com.vogella.android.myapplication.models.TagItem;
import com.vogella.android.myapplication.utils.OnChildClickListener;
import com.vogella.android.myapplication.utils.OnParentClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ExpandableRecyclerViewAdapter extends RecyclerView.Adapter<ExpandableRecyclerViewAdapter.ViewHolder> {
    private ArrayList<TagItem> tagItems;
    private Context mContext;
    private final OnParentClickListener parentListener;
    private final OnChildClickListener childListner;
    private static final String TAG = "ExpandableRecyclerViewAdapter";

    public ExpandableRecyclerViewAdapter(ArrayList<TagItem> tagItems, Context mContext, OnParentClickListener listener,
                                         OnChildClickListener childListner) {
        this.tagItems = tagItems;
        this.mContext = mContext;
        this.parentListener = listener;
        this.childListner = childListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.tag_list_item, parent, false);

        return new ViewHolder(v);
    }

    public void clear() {
        tagItems.clear();
        notifyItemRangeRemoved(0, getItemCount());
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final TagItem tagItem = tagItems.get(position);
        holder.bind(tagItem, parentListener);
        holder.tagName.setText(tagItem.getTagName());
        InnerRecyclerViewAdapter itemInnerRecyclerView = new InnerRecyclerViewAdapter(tagItems.get(position).getTagItemDetails(),
                mContext, childListner);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        holder.recyclerView.setAdapter(itemInnerRecyclerView);
        Log.d(TAG, "arraysize" + itemInnerRecyclerView.getItemCount() + "");
        if (tagItem.getTagItemDetails().size() > 0)
            holder.recyclerView.setVisibility(View.VISIBLE);
        Glide.with(mContext)
                .load(tagItem.getPhotoURL())
                .apply(new RequestOptions().dontAnimate())
                .into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return tagItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_menu_item)
        ImageView itemImage;
        @BindView(R.id.txt_item_name)
        TextView tagName;
        @BindView(R.id.innerRecyclerView)
        RecyclerView recyclerView;
        @BindView(R.id.container)
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final TagItem item, final OnParentClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerView.getVisibility() == View.GONE) {
                        listener.onItemClick(item);
                    } else
                        recyclerView.setVisibility(View.GONE);
                }
            });
        }
    }

}
