package com.abctech.blogtalking.module.composer;

import com.abctech.blogtalking.model.BTBlogEntry;
import com.abctech.blogtalking.module.base.BasePresenter;
import com.abctech.blogtalking.module.base.BaseView;

import java.io.File;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface BlogEntryComposerContract {

    interface View extends BaseView {

        void handleApiException(Throwable e);

        void showData(BTBlogEntry blogEntry);

        void addImage(String url);

        void close();
    }

    interface Presenter extends BasePresenter {
        void uploadImage(File file);

        void loadBlogEntry(String blogEntryId);

        void save(String title, String content);
    }
}
