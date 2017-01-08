package com.abctech.blogtalking.module.base;

public interface BaseView {
    void showProgressDialog(int stringResId);

    void showProgressDialog(String str);

    void hideProgressDialog();

    void toast(int stringResId);

    void toast(String str);
}
