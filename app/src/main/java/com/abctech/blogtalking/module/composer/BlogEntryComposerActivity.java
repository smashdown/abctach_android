package com.abctech.blogtalking.module.composer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.abctech.blogtalking.R;
import com.abctech.blogtalking.module.base.BTBaseActivity;
import com.abctech.blogtalking.util.AndroidUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;

public class BlogEntryComposerActivity extends BTBaseActivity {
    public static final String ARG_BLOG_ENTRY_ID = "blog_entry_id";

    @BindView(R.id.toolbar) Toolbar toolbar;

    /**
     * @param context
     * @param blogEntryId Blog entry id, If it's called for editing, null otherwise.
     * @return
     */
    public static Intent getIntent(Context context, String blogEntryId) {
        Intent intent = new Intent(context, BlogEntryComposerActivity.class);
        intent.putExtra(ARG_BLOG_ENTRY_ID, !TextUtils.isEmpty(blogEntryId) ? blogEntryId : "");

        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_blog_entry_composer;
    }

    @Override
    protected boolean setupData(Bundle savedInstanceState) {
        return true;
    }

    @Override
    protected boolean setupUI(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.bt_add_blog_entity);

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

    @Override
    public void onBackPressed() {
        AndroidUtils.hideKeyboard(this);

        String currentBlogEntryId = getIntent().getStringExtra(ARG_BLOG_ENTRY_ID);

        int textId = TextUtils.isEmpty(currentBlogEntryId) ? R.string.bt_cancel_write_desc : R.string.bt_cancel_edit_desc;
        new MaterialDialog.Builder(this)
                .content(textId)
                .positiveText(R.string.bt_yes)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                })
                .cancelable(true)
                .negativeText(R.string.bt_no)
                .show();
    }
}
