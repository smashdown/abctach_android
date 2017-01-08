package com.abctech.blogtalking.module.bloglist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.abctech.blogtalking.app.BTApp;
import com.abctech.blogtalking.app.BTPreferences;
import com.abctech.blogtalking.event.BTEventBlogEntryChanged;
import com.abctech.blogtalking.model.BTBlogEntry;
import com.abctech.blogtalking.module.base.BaseRefreshListener;
import com.abctech.blogtalking.repository.BlogEntryRepository;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.realm.Realm;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BlogEntryListPresenter implements BlogEntryListContract.Presenter, BaseRefreshListener {
    private final BlogEntryRepository        mBlogEntryRepository;
    private final BlogEntryListContract.View mView;

    private Context mContext;

    public BlogEntryListPresenter(Realm realm, Context context, @NonNull BlogEntryListContract.View view) {
        this.mBlogEntryRepository = new BlogEntryRepository(realm);
        this.mContext = context;
        this.mView = view;

        EventBus.getDefault().register(this);
    }

    @Override
    public void onLoadMore(int offset) {
        Log.d("JJY", "onLoadMore() - offset=" + offset);

        String sortBy = BTPreferences.getInstance(mContext).getSortBy();
        BTApp.getRestService().getBlogEntryList(sortBy, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<BTBlogEntry>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<BTBlogEntry> blogEntryList) {
                        mBlogEntryRepository.save(blogEntryList);
                        mView.updateLoadMoreStatus(blogEntryList.size() >= BTApp.PAGE_ITEM_COUNT);
                    }
                });
    }

    @Override
    public void onRefresh() {
        mView.showLoadingView();

        String sortBy = BTPreferences.getInstance(mContext).getSortBy();
        BTApp.getRestService().getBlogEntryList(sortBy, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<BTBlogEntry>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();


                        String sortBy = BTPreferences.getInstance(mContext).getSortBy();
                        List<BTBlogEntry> blogEntryList;
                        if (sortBy.equals("createdDate")) {
                            blogEntryList = mBlogEntryRepository.findAllSortedByDated();
                        } else if (sortBy.equals("title_asc")) {
                            blogEntryList = mBlogEntryRepository.findAllSortedByTitleAsc();
                        } else {
                            blogEntryList = mBlogEntryRepository.findAllSortedByTitleDesc();
                        }
                        if (blogEntryList != null && blogEntryList.size() > 0) {
                            mView.showBlogEntityList(blogEntryList);
                        } else {
                            mView.showFailedView();
                        }
                    }

                    @Override
                    public void onNext(List<BTBlogEntry> blogEntryList) {
                        mBlogEntryRepository.deleteAll();
                        mBlogEntryRepository.save(blogEntryList);

                        mView.updateLoadMoreStatus(blogEntryList.size() >= BTApp.PAGE_ITEM_COUNT);
                    }
                });
    }

    @Override
    public void loadBlogEntryListSortByDate() {

    }

    @Override
    public void loadBlogEntryListSortByTitle() {

    }

    @Override
    public void start() {
        onRefresh();
    }

    @Override
    public void stop() {
        EventBus.getDefault().unregister(this);
    }

    /***********************************************************************************************
     * DB Event
     **********************************************************************************************/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BTEventBlogEntryChanged event) {
        String sortBy = BTPreferences.getInstance(mContext).getSortBy();
        List<BTBlogEntry> blogEntryList;
        if (sortBy.equals("createdDate")) {
            blogEntryList = mBlogEntryRepository.findAllSortedByDated();
        } else if (sortBy.equals("title_asc")) {
            blogEntryList = mBlogEntryRepository.findAllSortedByTitleAsc();
        } else {
            blogEntryList = mBlogEntryRepository.findAllSortedByTitleDesc();
        }
        mView.showBlogEntityList(blogEntryList);
    }
}