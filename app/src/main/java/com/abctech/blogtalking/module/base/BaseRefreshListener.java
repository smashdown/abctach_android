package com.abctech.blogtalking.module.base;

import android.support.v4.widget.SwipeRefreshLayout;

public interface BaseRefreshListener extends SwipeRefreshLayout.OnRefreshListener {
    void onLoadMore(int offset);
}