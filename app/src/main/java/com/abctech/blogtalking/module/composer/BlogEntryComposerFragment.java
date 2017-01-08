package com.abctech.blogtalking.module.composer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.abctech.blogtalking.R;
import com.abctech.blogtalking.model.BTBlogEntry;
import com.abctech.blogtalking.module.base.BTBaseFragment;
import com.abctech.blogtalking.module.imagepicker.HSImagePickerActivity;
import com.abctech.blogtalking.util.AndroidUtils;
import com.abctech.blogtalking.util.AppUtils;
import com.abctech.blogtalking.util.FileUtils;
import com.abctech.blogtalking.util.ImageInputHelper;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import io.realm.Realm;
import jp.wasabeef.richeditor.RichEditor;
import retrofit2.adapter.rxjava.HttpException;

public class BlogEntryComposerFragment extends BTBaseFragment implements BlogEntryComposerContract.View, ImageInputHelper.ImageActionListener {
    private static final int REQ_PERMISSION = 300;
    private static final int REQ_TAKE_PHOTO = 500;

    @BindView(R.id.etTitle) EditText   mEtTitle;
    @BindView(R.id.editor)  RichEditor mEditor;

    @BindView(R.id.action_undo)            ImageButton action_undo;
    @BindView(R.id.action_redo)            ImageButton action_redo;
    @BindView(R.id.action_bold)            ImageButton action_bold;
    @BindView(R.id.action_italic)          ImageButton action_italic;
    @BindView(R.id.action_strikethrough)   ImageButton action_strikethrough;
    @BindView(R.id.action_underline)       ImageButton action_underline;
    @BindView(R.id.action_heading1)        ImageButton action_heading1;
    @BindView(R.id.action_heading2)        ImageButton action_heading2;
    @BindView(R.id.action_align_left)      ImageButton action_align_left;
    @BindView(R.id.action_align_center)    ImageButton action_align_center;
    @BindView(R.id.action_align_right)     ImageButton action_align_right;
    @BindView(R.id.action_insert_image)    ImageButton action_insert_image;
    @BindView(R.id.action_insert_checkbox) ImageButton action_insert_checkbox;

    String[] mRequiredPermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    private Realm                      realm;
    private BlogEntryComposerPresenter mPresenter;
    private ImageInputHelper           mImageInputHelper;

    public BlogEntryComposerFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frg_blog_entry_composer;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_TAKE_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<Uri> uris = data.getParcelableArrayListExtra(HSImagePickerActivity.EXTRA_IMAGE_URIS);
                mPresenter.uploadImage(new File(uris.get(0).getPath()));
            }
        } else {
            mImageInputHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected boolean setupData(Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();
        mPresenter = new BlogEntryComposerPresenter(realm, this);
        mImageInputHelper = new ImageInputHelper(this);
        mImageInputHelper.setImageActionListener(this);

        return true;
    }

    @Override
    public void onDestroy() {
        if (realm != null)
            realm.close();

        super.onDestroy();
    }

    @Override
    protected boolean setupUI() {
        setHasOptionsMenu(true);

        action_undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.undo();
            }
        });

        action_redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.redo();
            }
        });

        action_bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBold();
            }
        });

        action_italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setItalic();
            }
        });

        action_strikethrough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setStrikeThrough();
            }
        });

        action_underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        action_heading1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(1);
            }
        });

        action_heading2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(2);
            }
        });

        action_align_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        action_align_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        action_align_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

        action_insert_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionAttach();
            }
        });

        action_insert_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertTodo();
            }
        });

        mEditor.setPlaceholder(getString(R.string.bt_content));
        return false;
    }

    @Override
    public boolean updateData() {
        // If it's called for editing, blog entry ID should be present.
        Bundle bundle = getActivity().getIntent().getExtras();
        String blogEntryId = bundle.getString(BlogEntryComposerActivity.ARG_BLOG_ENTRY_ID);

        if (!TextUtils.isEmpty(blogEntryId)) {
            mPresenter.loadBlogEntry(blogEntryId);
        }

        return false;
    }

    @Override
    public boolean updateUI() {
        return false;
    }

    @Override
    public void addImage(String url) {
        mEditor.insertImage(url, String.valueOf(System.currentTimeMillis()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_blog_entity_composer, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_send) {
            actionSave();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showData(BTBlogEntry blogEntry) {
        if (!TextUtils.isEmpty(blogEntry.getTitle()))
            mEtTitle.setText(blogEntry.getTitle());
        if (!TextUtils.isEmpty(blogEntry.getContent()))
            mEditor.setHtml(blogEntry.getContent());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("JJY", "onRequestPermissionsResult() - requestCode=" + requestCode +
                ", permissions=" + Arrays.toString(permissions) +
                ", grantResults=" + Arrays.toString(grantResults));

        if (requestCode == REQ_PERMISSION) {
            boolean allGranted = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;

                    if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        AndroidUtils.toast(getActivity(), R.string.bt_permission_desc_external_storage);
                    } else if (permissions[i].equals(Manifest.permission.CAMERA)) {
                        AndroidUtils.toast(getActivity(), R.string.bt_permission_desc_camera);
                    }
                    break;
                }
            }

            if (allGranted) {
                actionAttach();
            }
        }
    }

    @Override
    public void onImageSelectedFromGallery(Uri uri, File imageFile) {
        mPresenter.uploadImage(new File(FileUtils.getPath(getActivity(), uri)));
    }

    @Override
    public void onImageTakenFromCamera(Uri uri, File imageFile) {
        mPresenter.uploadImage(imageFile);
    }

    @Override
    public void onImageCropped(Uri uri, File imageFile) {
    }

    private void actionSave() {
        final String title = mEtTitle.getText().toString();
        if (TextUtils.isEmpty(title)) {
            AndroidUtils.toast(getActivity(), R.string.bt_err_empty_title);
            return;
        }

        final String content = mEditor.getHtml();
        if (TextUtils.isEmpty(content)) {
            AndroidUtils.toast(getActivity(), R.string.bt_err_empty_content);
            return;
        }

        new MaterialDialog.Builder(getActivity())
                .content(R.string.bt_save_confirm)
                .positiveText(R.string.bt_ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mPresenter.save(title, content);
                    }
                })
                .negativeText(R.string.bt_cancel)
                .show();
    }

    private void actionAttach() {
        if (AndroidUtils.checkAndRequestPermissions(this, REQ_PERMISSION, mRequiredPermissions)) {
            final String[] choiceList = getResources().getStringArray(R.array.input_image);
            new MaterialDialog.Builder(getActivity())
                    .items(choiceList)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                            switch (which) {
                                case 0:
                                    // gallery
                                    Intent intent = HSImagePickerActivity.getIntent(getActivity(), 1, 1);
                                    startActivityForResult(intent, REQ_TAKE_PHOTO);
                                    break;
                                case 1:
                                    // camera
                                    mImageInputHelper.takePhotoWithCamera();
                                    break;
                            }
                        }
                    })
                    .show();
        }
    }

    @Override
    public void handleApiException(Throwable e) {
        if (e instanceof HttpException) {
            AppUtils.handleServerError(getActivity(), ((HttpException) e).response(), false);
        } else {
            AppUtils.handleNetworkFailException(getActivity(), false);
        }
    }

    @Override
    public void close() {
        getActivity().finish();
    }
}
