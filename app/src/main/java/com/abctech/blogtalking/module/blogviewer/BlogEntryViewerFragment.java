package com.abctech.blogtalking.module.blogviewer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.abctech.blogtalking.R;
import com.abctech.blogtalking.model.BTBlogEntry;
import com.abctech.blogtalking.module.base.BTBaseFragment;
import com.abctech.blogtalking.module.composer.BlogEntryComposerActivity;

import butterknife.BindView;
import io.realm.Realm;

/**
 * A placeholder fragment containing a simple view.
 */
public class BlogEntryViewerFragment extends BTBaseFragment implements BlogEntryViewerContract.View {
    @BindView(R.id.tvTitle)   TextView mTvTitle;
    @BindView(R.id.wvContent) WebView  mWvContent;

    private Realm                    realm;
    private BlogEntryViewerPresenter mPresenter;

    public BlogEntryViewerFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_blog_entry_viewer;
    }

    @Override
    protected boolean setupData(Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();
        mPresenter = new BlogEntryViewerPresenter(realm, getActivity(), this);

        setHasOptionsMenu(true);

        return false;
    }

    @Override
    protected boolean setupUI() {
        return false;
    }

    @Override
    public boolean updateData() {
        Bundle bundle = getActivity().getIntent().getExtras();
        String blogEntryId = bundle.getString(BlogEntryComposerActivity.ARG_BLOG_ENTRY_ID);

        if (!TextUtils.isEmpty(blogEntryId)) {
            mPresenter.loadBlogEntry(blogEntryId);
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_blog_entry_viewer, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            Bundle bundle = getActivity().getIntent().getExtras();
            String blogEntryId = bundle.getString(BlogEntryComposerActivity.ARG_BLOG_ENTRY_ID);

            Intent intent = BlogEntryComposerActivity.getIntent(getActivity(), blogEntryId);
            startActivity(intent);

            getActivity().finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean updateUI() {
        return false;
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void showBlogEntity(BTBlogEntry blogEntry) {
        mTvTitle.setText(blogEntry.getTitle());

        mWvContent.getSettings().setJavaScriptEnabled(true);
        mWvContent.loadDataWithBaseURL("", blogEntry.getContent(), "text/html", "UTF-8", "");
    }

}
