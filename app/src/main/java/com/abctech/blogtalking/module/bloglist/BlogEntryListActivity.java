package com.abctech.blogtalking.module.bloglist;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.abctech.blogtalking.R;
import com.abctech.blogtalking.module.base.BTBaseActivity;

import butterknife.BindView;

public class BlogEntryListActivity extends BTBaseActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.act_blog_entry_list;
    }

    @Override
    protected boolean setupData(Bundle savedInstanceState) {
        return true;
    }

    @Override
    protected boolean setupUI(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

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
