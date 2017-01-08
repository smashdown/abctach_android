package com.abctech.blogtalking.module.composer;

import android.support.annotation.NonNull;
import android.util.Log;

import com.abctech.blogtalking.app.BTApp;
import com.abctech.blogtalking.model.BTBlogEntry;
import com.abctech.blogtalking.repository.BlogEntryRepository;
import com.abctech.blogtalking.util.FileUtils;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BlogEntryComposerPresenter implements BlogEntryComposerContract.Presenter {
    private final BlogEntryRepository            mBlogEntryRepository;
    private final BlogEntryComposerContract.View mView;

    private Realm       realm;
    private BTBlogEntry mCurrentBlogEntry;

    public BlogEntryComposerPresenter(Realm realm, @NonNull BlogEntryComposerContract.View view) {
        this.mBlogEntryRepository = new BlogEntryRepository(realm);
        this.mView = view;
        this.realm = realm;

        mCurrentBlogEntry = new BTBlogEntry();
    }

    @Override
    public void loadBlogEntry(String blogEntryId) {
        BTBlogEntry realmObject = mBlogEntryRepository.findById(blogEntryId);
        if (realmObject != null) {
            mCurrentBlogEntry = realm.copyFromRealm(realmObject);
            mView.showData(mCurrentBlogEntry);
        }
    }

    @Override
    public void uploadImage(File file) {
        Map<String, RequestBody> map = makeRequestBodyMap(file);

        mView.showProgressDialog("Uploading image...");
        BTApp.getRestService().postImage(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                        mView.hideProgressDialog();
                        mView.handleApiException(e);
                    }

                    @Override
                    public void onNext(ResponseBody imageIdBody) {
                        mView.hideProgressDialog();
                        try {
                            mView.addImage(BTApp.makeImageUrl(imageIdBody.string()));
                        } catch (Exception e) {
                        }
                    }
                });
    }

    private Map<String, RequestBody> makeRequestBodyMap(File file) {
        try {
            Log.i("JJY", "target file=" + file.getAbsolutePath());
            Log.i("JJY", "target file size=" + file.getTotalSpace());

            Map<String, RequestBody> map = new HashMap<>();

            RequestBody fileBody = RequestBody.create(MediaType.parse(FileUtils.getMimeType(file)), file);
            map.put("file\"; filename=\"" + file.getName(), fileBody);

            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void save(String title, String content) {
        mCurrentBlogEntry.setTitle(title);
        mCurrentBlogEntry.setContent(content);
        mCurrentBlogEntry.setCreatedDate(new Date());

        mView.showProgressDialog("Saving...");
        BTApp.getRestService().postOrder(mCurrentBlogEntry)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BTBlogEntry>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                        mView.hideProgressDialog();
                        mView.handleApiException(e);
                    }

                    @Override
                    public void onNext(BTBlogEntry blogEntry) {
                        mView.hideProgressDialog();

                        mBlogEntryRepository.update(blogEntry);

                        mView.close();
                    }
                });
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }
}