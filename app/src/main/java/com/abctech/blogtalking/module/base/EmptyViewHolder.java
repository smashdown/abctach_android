package com.abctech.blogtalking.module.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.abctech.blogtalking.R;

public class EmptyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public View     root;
    public TextView tvEmptyMessage;

    BaseRefreshListener mRefreshListener;

    public EmptyViewHolder(View v, BaseRefreshListener refreshListener) {
        super(v);

        root = v.findViewById(R.id.root);
        root.setOnClickListener(this);
        tvEmptyMessage = (TextView) v.findViewById(R.id.tvEmptyMessage);
        this.mRefreshListener = refreshListener;
    }

    @Override
    public void onClick(View v) {
        if (mRefreshListener != null)
            mRefreshListener.onRefresh();
    }
}