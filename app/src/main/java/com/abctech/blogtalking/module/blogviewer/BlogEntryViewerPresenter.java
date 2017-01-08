package com.abctech.blogtalking.module.blogviewer;

import android.content.Context;
import android.support.annotation.NonNull;

import com.abctech.blogtalking.model.BTBlogEntry;
import com.abctech.blogtalking.repository.BlogEntryRepository;

import io.realm.Realm;

public class BlogEntryViewerPresenter implements BlogEntryViewerContract.Presenter {
    private final BlogEntryRepository          mBlogEntryRepository;
    private final BlogEntryViewerContract.View mView;

    private Context mContext;

    public BlogEntryViewerPresenter(Realm realm, Context context, @NonNull BlogEntryViewerContract.View view) {
        this.mBlogEntryRepository = new BlogEntryRepository(realm);
        this.mContext = context;
        this.mView = view;
    }

    @Override
    public void loadBlogEntry(String id) {
        mView.showLoadingView();
        BTBlogEntry blogEntry = mBlogEntryRepository.findById(id);
        mView.showBlogEntity(blogEntry);
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }
}