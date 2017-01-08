package com.abctech.blogtalking.module.base;

import android.support.v7.widget.RecyclerView;

public class BaseOnScrollLoadMoreListener extends RecyclerView.OnScrollListener {
    private BaseRefreshInterface mOnLoadMoreListener;
    private BaseRefreshListener  mRefreshListener;
    private boolean              mIsReverse;

    public BaseOnScrollLoadMoreListener(BaseRefreshInterface adapter, BaseRefreshListener refreshListener, boolean isReverse) {
        this.mOnLoadMoreListener = adapter;
        this.mRefreshListener = refreshListener;
        this.mIsReverse = isReverse;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (!recyclerView.canScrollVertically(mIsReverse ? 1 : -1)) {
            // Scrolled On Top
        } else if (!recyclerView.canScrollVertically(mIsReverse ? -1 : 1)) {
            if (mOnLoadMoreListener != null && mOnLoadMoreListener.canLoadMore()) {
                mRefreshListener.onLoadMore(mOnLoadMoreListener.getPureItemCount());
            }
        } else if (dy < 0) {
            // Scrolled Up
        } else if (dy > 0) {
            // Scrolled Down
        }
    }
}
