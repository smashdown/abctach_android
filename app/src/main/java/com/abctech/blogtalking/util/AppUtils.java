package com.abctech.blogtalking.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.abctech.blogtalking.R;
import com.abctech.blogtalking.app.BTApp;
import com.abctech.blogtalking.model.BTError;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import retrofit2.Response;

public class AppUtils {
    private static AppUtils instance;

    private AppUtils() {
    }

    public static AppUtils getInstance(Context context) {
        if (instance == null) {
            instance = new AppUtils();
        }
        return instance;
    }

    public static void handleServerError(final Activity context, Response response, boolean finish) {
        BTError error = BTApp.parseError(response);
        try {
            Log.e("JJY", "error=" + GsonUtil.getInstance().getGson().toJson(error));
        } catch (Exception e) {
            e.printStackTrace();
        }


        String title = error.error; // name of staus code
        String message = error.toString(); // string from server

        MaterialDialog.SingleButtonCallback onOkFinishListener = new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                context.finish();
            }
        };
        MaterialDialog.SingleButtonCallback onOkNonFinishListener = new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            }
        };

        message = context.getString(R.string.bt_err_unknown);

        new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .positiveText(R.string.bt_ok)
                .onPositive(finish ? onOkFinishListener : onOkNonFinishListener)
                .show();
    }

    public static void handleNetworkFailException(final Activity context, final boolean finish) {
        new MaterialDialog.Builder(context)
                .title("Connection error")
                .content(context.getString(R.string.bt_err_server_unavailable))
                .cancelable(false)
                .positiveText(R.string.bt_ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (finish)
                            context.finish();
                    }
                })
                .show();
    }
}
