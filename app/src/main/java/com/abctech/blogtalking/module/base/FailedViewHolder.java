package com.abctech.blogtalking.module.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.abctech.blogtalking.R;


public class FailedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public View     root;
    public TextView tvFailedMessage;

    private BaseRefreshListener mRefreshListener;

    public FailedViewHolder(View v, BaseRefreshListener refreshListener) {
        super(v);

        root = v.findViewById(R.id.root);
        root.setOnClickListener(this);
        tvFailedMessage = (TextView) v.findViewById(R.id.tvFailedMessage);
        mRefreshListener = refreshListener;
    }

    @Override
    public void onClick(View v) {
        if (mRefreshListener != null)
            mRefreshListener.onRefresh();
    }

}