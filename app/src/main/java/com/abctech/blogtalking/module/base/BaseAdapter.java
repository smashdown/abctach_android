package com.abctech.blogtalking.module.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abctech.blogtalking.R;
import com.abctech.blogtalking.app.BTApp;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter implements BaseRefreshInterface {
    protected BTRecyclerViewStatus mStatus      = BTRecyclerViewStatus.LOADING;
    protected boolean              mCanLoadMore = false;

    protected String mLoadingMessage = "Loading...";
    protected String mEmptyMessage   = "There is no data";
    protected String mFailedMessage  = "Cannot connect to server, after checking network status try it again please.";

    protected Context mContext = null;
    protected BaseRefreshListener mRefreshListener;

    protected List<T> mDataList = null;

    public BaseAdapter(Context context, BaseRefreshListener refreshListener) {
        mContext = context;
        mDataList = new ArrayList<T>();
        mRefreshListener = refreshListener;
    }

    @Override
    public int getPureItemCount() {
        int pureItemCount = mDataList.size();
        //        Log.d(HSApp.LOG_TAG, this.getClass().getSimpleName() + "::getPureItemCount() - pureItemCount=" + pureItemCount);

        return pureItemCount;
    }


    protected T getItem(int position) {
        if (mDataList == null) {
            return null;
        } else {
            try {
                return mDataList.get(position - getHeaderViewCount());
            } catch (Exception e) {
            }
        }
        return null;
    }

    @Override
    public long getItemId(int index) {
        return super.getItemId(index);
    }

    // Header & Footer should be shown anytime.
    public int getHeaderViewCount() {
        return 0;
    }

    public int getFooterViewCount() {
        return 0;
    }

    public void setStatus(BTRecyclerViewStatus mStatus) {
        this.mStatus = mStatus;
    }

    public BTRecyclerViewStatus getStatus() {
        return mStatus;
    }

    public void setLoadingMessage(String loadingMessage) {
        this.mLoadingMessage = loadingMessage;
    }

    public void setEmptyMessage(String emptyMessage) {
        this.mEmptyMessage = emptyMessage;
    }

    public void setFailedMessage(String failedMessage) {
        this.mFailedMessage = failedMessage;
    }

    @Override
    public boolean canLoadMore() {
        return mCanLoadMore;
    }

    public void setCanLoadMore(boolean mCanLoadMore) {
        this.mCanLoadMore = mCanLoadMore;
    }

    @Override
    public int getItemCount() {
        int itemCount;
        switch (mStatus) {
            case DONE:
                if (getPureItemCount() > 0) {
                    itemCount = getPureItemCount() + getHeaderViewCount() + getFooterViewCount();
                } else {
                    itemCount = 1 + getHeaderViewCount() + getFooterViewCount();
                }
                break;
            default:
                itemCount = 1;
                itemCount += getHeaderViewCount();
                itemCount += getFooterViewCount();
                break;
        }
        //        Log.d(HSApp.LOG_TAG, this.getClass().getSimpleName() + "::getItemCount() - mStatus=" + mStatus + ", itemCount=" + itemCount);

        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        int type = BTRecyclerViewType.LOADING.ordinal();

        if (position < getHeaderViewCount()) {
            // Log.i(HSApp.LOG_TAG, this.getClass().getSimpleName() + "::getItemViewType() - pos=" + position + ", HEADER");
            return BTRecyclerViewType.HEADER.ordinal();
        }
        if (position > getHeaderViewCount() + getPureItemCount()) {
            // Log.i(HSApp.LOG_TAG, this.getClass().getSimpleName() + "::getItemViewType() - pos=" + position + ", FOOTER");
            return BTRecyclerViewType.FOOTER.ordinal();
        }

        switch (mStatus) {
            case LOADING:
                type = BTRecyclerViewType.LOADING.ordinal();
                break;
            case DONE:
                if (getPureItemCount() > 0)
                    type = BTRecyclerViewType.ITEM.ordinal();
                else
                    type = BTRecyclerViewType.EMPTY.ordinal();
                break;
            case FAILED:
                type = BTRecyclerViewType.FAILED.ordinal();
                break;
        }
        //        Log.i(HSApp.LOG_TAG, this.getClass().getSimpleName() + "::getItemViewType() - pos=" + position + ", type=" + type);

        return type;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == BTRecyclerViewType.LOADING.ordinal()) {
            Log.e(BTApp.LOG_TAG, BaseAdapter.class.getSimpleName() + "::onCreateViewHolder() - LOADING");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_adapter_loading, parent, false);
            VH holder = (VH) new LoadingViewHolder(view);
            ((LoadingViewHolder) holder).tvLoadingMessage = (TextView) view.findViewById(R.id.tvLoadingMessage);
            ((LoadingViewHolder) holder).root = view.findViewById(R.id.root);
            return holder;
        } else if (viewType == BTRecyclerViewType.EMPTY.ordinal()) {
            Log.e(BTApp.LOG_TAG, BaseAdapter.class.getSimpleName() + "::onCreateViewHolder() - EMPTY");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_adapter_empty, parent, false);
            return (VH) new EmptyViewHolder(view, mRefreshListener);
        } else if (viewType == BTRecyclerViewType.FAILED.ordinal()) {
            Log.e(BTApp.LOG_TAG, BaseAdapter.class.getSimpleName() + "::onCreateViewHolder() - FAILED");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_adapter_failed, parent, false);
            return (VH) new FailedViewHolder(view, mRefreshListener);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null) {
            if (holder instanceof LoadingViewHolder) {
                ((LoadingViewHolder) holder).tvLoadingMessage.setText(mLoadingMessage);
                if (getHeaderViewCount() > 0) {
                    ((LoadingViewHolder) holder).root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                } else {
                    ((LoadingViewHolder) holder).root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                }
            } else if (holder instanceof EmptyViewHolder) {
                ((EmptyViewHolder) holder).tvEmptyMessage.setText(mEmptyMessage);
                if (getHeaderViewCount() > 0) {
                    ((EmptyViewHolder) holder).root.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                } else {
                    ((EmptyViewHolder) holder).root.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                }
            } else if (holder instanceof FailedViewHolder) {
                ((FailedViewHolder) holder).tvFailedMessage.setText(mFailedMessage);
                if (getHeaderViewCount() > 0) {
                    ((FailedViewHolder) holder).root.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                } else {
                    ((FailedViewHolder) holder).root.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                }
            }
        }
    }

    public void addData(List<T> newData, boolean clear) {
        if (clear)
            this.mDataList.clear();

        this.mDataList.addAll(newData);
    }

    public List<T> getDataList() {
        return this.mDataList;
    }
}