package com.abctech.blogtalking.module.blogviewer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.abctech.blogtalking.R;
import com.abctech.blogtalking.module.base.BTBaseActivity;
import com.abctech.blogtalking.util.AndroidUtils;

import butterknife.BindView;

public class BlogEntryViewerActivity extends BTBaseActivity {
    public static final String ARG_BLOG_ENTRY_ID = "blog_entry_id";

    @BindView(R.id.toolbar) Toolbar toolbar;

    /**
     * @param context
     * @param blogEntryId Blog entry id, If it's called for editing, null otherwise.
     * @return
     */
    public static Intent getIntent(Context context, String blogEntryId) {
        Intent intent = new Intent(context, BlogEntryViewerActivity.class);
        intent.putExtra(ARG_BLOG_ENTRY_ID, !TextUtils.isEmpty(blogEntryId) ? blogEntryId : "");

        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_blog_entry_viewer;
    }

    @Override
    protected boolean setupData(Bundle savedInstanceState) {
        String blogEntryId = getIntent().getStringExtra(ARG_BLOG_ENTRY_ID);
        if (TextUtils.isEmpty(blogEntryId)) {
            AndroidUtils.toast(this, R.string.bt_err_unknown);
            return false;
        } else
            return true;
    }

    @Override
    protected boolean setupUI(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.bt_blog_entry_viewer);

        return false;
    }

    @Override
    public boolean updateData() {
        return false;
    }

    @Override
    public boolean updateUI() {
        return false;
    }

}
