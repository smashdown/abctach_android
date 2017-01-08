package com.abctech.blogtalking.module.blogviewer;

import com.abctech.blogtalking.model.BTBlogEntry;
import com.abctech.blogtalking.module.base.BasePresenter;
import com.abctech.blogtalking.module.base.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface BlogEntryViewerContract {

    interface View extends BaseView {

        void showLoadingView();

        void showBlogEntity(BTBlogEntry blogEntry);

    }

    interface Presenter extends BasePresenter {
        void loadBlogEntry(String id);
    }
}
