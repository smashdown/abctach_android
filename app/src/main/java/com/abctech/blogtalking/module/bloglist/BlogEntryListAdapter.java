package com.abctech.blogtalking.module.bloglist;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abctech.blogtalking.R;
import com.abctech.blogtalking.model.BTBlogEntry;
import com.abctech.blogtalking.module.base.BTRecyclerViewType;
import com.abctech.blogtalking.module.base.BaseAdapter;
import com.abctech.blogtalking.module.base.BaseRefreshListener;
import com.abctech.blogtalking.module.blogviewer.BlogEntryViewerActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.DateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BlogEntryListAdapter extends BaseAdapter<BTBlogEntry, RecyclerView.ViewHolder> {
    private Activity activity;

    public BlogEntryListAdapter(Activity activity, BaseRefreshListener refreshListener) {
        super(activity, refreshListener);

        this.activity = activity;
    }


    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == BTRecyclerViewType.ITEM.ordinal()) {
            return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_frg_blog_entry_list_item, parent, false));
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            BTBlogEntry blogEntry = getItem(position);
            if (blogEntry != null) {
                ItemViewHolder viewHolder = (ItemViewHolder) holder;

                viewHolder.tv_title.setText(blogEntry.getTitle());

                Document doc = Jsoup.parse(blogEntry.getContent());
                String text = doc.body().text(); // "An example link"
                viewHolder.tv_text.setText(text);

                viewHolder.tv_date.setText(DateFormat.getDateTimeInstance().format(blogEntry.getCreatedDate()));

                viewHolder.root.setTag(blogEntry);
            }
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.root)     View     root;
        @BindView(R.id.tv_title) TextView tv_title;
        @BindView(R.id.tv_text)  TextView tv_text;
        @BindView(R.id.tv_date)  TextView tv_date;

        public ItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        @OnClick(R.id.root)
        public void onClickItem(View v) {
            final BTBlogEntry blogEntry = (BTBlogEntry) v.getTag();
            Log.d("JJY", "onClickItem() - blog id=" + blogEntry.getId());

            Intent intent = BlogEntryViewerActivity.getIntent(activity, blogEntry.getId());
            activity.startActivity(intent);
        }
    }
}
