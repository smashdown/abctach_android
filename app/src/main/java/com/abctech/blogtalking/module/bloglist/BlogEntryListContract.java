package com.abctech.blogtalking.module.bloglist;

import com.abctech.blogtalking.model.BTBlogEntry;
import com.abctech.blogtalking.module.base.BasePresenter;
import com.abctech.blogtalking.module.base.BaseView;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface BlogEntryListContract {

    interface View extends BaseView {

        void showLoadingView();

        void showFailedView();

        void handleApiException(Throwable e);

        void updateLoadMoreStatus(boolean canLoadMore);

        void showBlogEntityList(List<BTBlogEntry> blogEntry);

        void close();
    }

    interface Presenter extends BasePresenter {
        void loadBlogEntryListSortByDate();

        void loadBlogEntryListSortByTitle();
    }
}
