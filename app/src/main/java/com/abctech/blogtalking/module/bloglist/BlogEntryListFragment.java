package com.abctech.blogtalking.module.bloglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioGroup;

import com.abctech.blogtalking.R;
import com.abctech.blogtalking.app.BTPreferences;
import com.abctech.blogtalking.model.BTBlogEntry;
import com.abctech.blogtalking.module.base.BTBaseFragment;
import com.abctech.blogtalking.module.base.BTRecyclerViewStatus;
import com.abctech.blogtalking.module.base.BaseOnScrollLoadMoreListener;
import com.abctech.blogtalking.module.composer.BlogEntryComposerActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import info.hoang8f.android.segmented.SegmentedGroup;
import io.realm.Realm;

public class BlogEntryListFragment extends BTBaseFragment implements BlogEntryListContract.View {

    @BindView(R.id.sgSort)           SegmentedGroup             mSgSort;
    @BindView(R.id.srlBlogEntryList) SwipeRefreshLayout         mSrlBlogEntryList;
    @BindView(R.id.rvBlogEntryList)  RecyclerView               mRvBlogEntryList;
    protected                        RecyclerView.LayoutManager mLayoutManager;
    private                          BlogEntryListAdapter       mAdapter;

    Realm                  realm;
    BlogEntryListPresenter mPresenter;

    public BlogEntryListFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frg_blog_entry_list;
    }

    @Override
    protected boolean setupData(Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();
        mPresenter = new BlogEntryListPresenter(realm, getActivity(), this);

        return false;
    }

    @Override
    public void onDestroy() {
        if (realm != null)
            realm.close();

        if (mPresenter != null)
            mPresenter.stop();

        super.onDestroy();
    }

    @Override
    protected boolean setupUI() {
        // init recycler view
        mLayoutManager = new LinearLayoutManager(getActivity());

        mAdapter = new BlogEntryListAdapter(getActivity(), mPresenter);
        mAdapter.setEmptyMessage(getString(R.string.bt_empty_blog_entry));
        mAdapter.setFailedMessage(getString(R.string.bt_err_server_unavailable_with_refresh));

        mSrlBlogEntryList.setColorSchemeResources(R.color.OrangeRed, R.color.Coral, R.color.Orange);
        mSrlBlogEntryList.setOnRefreshListener(mPresenter);

        mRvBlogEntryList.addOnScrollListener(new BaseOnScrollLoadMoreListener(mAdapter, mPresenter, false));
        mRvBlogEntryList.setLayoutManager(mLayoutManager);
        mRvBlogEntryList.setAdapter(mAdapter);

        String sortBy = BTPreferences.getInstance(getActivity()).getSortBy();
        if (sortBy.equals("createdDate")) {
            mSgSort.check(R.id.rbSortByDate);
        } else if (sortBy.equals("title_asc")) {
            mSgSort.check(R.id.rbSortByTitleAsc);
        } else {
            mSgSort.check(R.id.rbSortByTitleDesc);
        }

        mSgSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbSortByDate:
                        BTPreferences.getInstance(getActivity()).setSortBy("createdDate");
                        mPresenter.onRefresh();
                        break;
                    case R.id.rbSortByTitleAsc:
                        BTPreferences.getInstance(getActivity()).setSortBy("title_asc");
                        mPresenter.onRefresh();
                        break;
                    case R.id.rbSortByTitleDesc:
                        BTPreferences.getInstance(getActivity()).setSortBy("title_desc");
                        mPresenter.onRefresh();
                        break;
                }
            }
        });
        return false;
    }

    @Override
    public boolean updateData() {
        mPresenter.start();

        return false;
    }

    @Override
    public boolean updateUI() {
        return false;
    }

    @Override
    public void showLoadingView() {
        mSrlBlogEntryList.setRefreshing(false);
        mAdapter.setStatus(BTRecyclerViewStatus.LOADING);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showFailedView() {
        mSrlBlogEntryList.setRefreshing(false);
        mAdapter.setStatus(BTRecyclerViewStatus.FAILED);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void handleApiException(Throwable e) {

    }

    @Override
    public void updateLoadMoreStatus(boolean canLoadMore) {
        mAdapter.setCanLoadMore(canLoadMore);
    }

    @Override
    public void showBlogEntityList(List<BTBlogEntry> blogEntry) {
        mAdapter.addData(blogEntry, true);
        mAdapter.setStatus(BTRecyclerViewStatus.DONE);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void close() {

    }

    /***********************************************************************************************
     * USER EVENTS
     **********************************************************************************************/

    @OnClick(R.id.fab)
    protected void onClickAddBlogEntity(View view) {
        Intent intent = BlogEntryComposerActivity.getIntent(getActivity(), null);
        startActivity(intent);
    }
}
