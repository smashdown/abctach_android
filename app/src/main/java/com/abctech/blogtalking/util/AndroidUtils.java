package com.abctech.blogtalking.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AndroidUtils {
    public static void toast(Context context, final String msg) {
        try {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void toast(Context context, final int msgId) {
        try {
            Toast.makeText(context, msgId, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getVersionName(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void sendEmail(Activity context, int chooserTitleResId, String from, String to, String title) {
        ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(context);
        builder.setType("message/rfc822");
        builder.addEmailTo(to);

        StringBuilder content = new StringBuilder(title);
        if (!TextUtils.isEmpty(from))
            content.append("(").append(from).append(")");
        builder.setSubject(content.toString());
        builder.setChooserTitle(chooserTitleResId);
        builder.startChooser();
    }

    public static void call(Activity context, String phoneNumber, boolean immediately) {
        Intent myIntent = null;

        if (immediately)
            myIntent = new Intent(Intent.ACTION_CALL);
        else
            myIntent = new Intent(Intent.ACTION_DIAL);
        String phNum = "tel:" + phoneNumber;
        myIntent.setData(Uri.parse(phNum));
        context.startActivity(myIntent);
    }

    public static void sendMessage(Context context, String phone) {
        Uri uri = Uri.parse("smsto:" + phone);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        // it.putExtra("sms_body", "The SMS text");
        context.startActivity(it);
    }

    public static float spToPx(Context ctx, int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, ctx.getResources().getDisplayMetrics());
    }

    public static int dpToPx(Context ctx, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, ctx.getResources().getDisplayMetrics());
    }

    public static float pxToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }


    public static void makeTextViewStrikeLine(TextView tv) {
        tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public static void makeTextViewUnderline(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public static void showKeyboard(Activity activity) {
        if (activity != null) {
            activity.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static void hideKeyboard(Dialog dialog) {
        if (dialog != null) {
            InputMethodManager inputManager = (InputMethodManager) dialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            View view = dialog.getCurrentFocus();
            if (view != null)
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void hideKeyboard(Activity context) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = context.getCurrentFocus();
        if (v != null)
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    //
    //    public static void showKeyboard(Activity activity) {
    //        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    //        View v = activity.getCurrentFocus();
    //        if (v != null)
    //            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.SHOW_IMPLICIT);
    //    }


    public static String getCountryCode(Context context) {
        try {
            TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String code = telManager.getNetworkCountryIso().toUpperCase();

            if (!TextUtils.isEmpty(code))
                return code;
            else {
                code = telManager.getSimCountryIso().toUpperCase();

                if (!TextUtils.isEmpty(code))
                    return code;
            }
        } catch (Exception e) {
        }

        return "KR";
    }

    public static void goToMarket(Activity activity, String appPackageName) {
        // getPackageName() from Context or Activity object
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }


    public static String loadAssetText(Context context, String assetFileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(assetFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static String getContentType(Context context, Uri uri) {
        return context.getContentResolver().getType(uri);
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        Log.d("JJY", "getMimeType() - url=" + url + ", extension=" + extension);

        if (!TextUtils.isEmpty(extension)) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            if (type == null) {
                return "image/" + extension;
            } else {
                return type;
            }
        } else {
            return "image/*";
        }
    }


    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }


    public static String takePhoto(Fragment fragment, String providerAuthority, int REQ_TAKE_PHOTO) {
        // Ensure that there's a camera activity to handle the intent
        try {
            // Create the File where the photo should go
            File photoFile = createImageFile(fragment.getActivity());
            Uri photoURI = FileProvider.getUriForFile(fragment.getActivity(), providerAuthority, photoFile);
            String absoluteFilePath = photoFile.getAbsolutePath();

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

            if (intent.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
                fragment.startActivityForResult(intent, REQ_TAKE_PHOTO);
                return absoluteFilePath;
            } else {
                Log.e("JJY", "takePhoto - no activity to handle this intent");
            }
        } catch (IOException ex) {
            // Error occurred while creating the File
            ex.printStackTrace();
        }
        return null;

    }

    public static String takePhoto(Activity context, String providerAuthority, int REQ_TAKE_PHOTO) {
        // Ensure that there's a camera activity to handle the intent
        try {
            // Create the File where the photo should go
            File photoFile = createImageFile(context);
            Uri photoURI = FileProvider.getUriForFile(context, providerAuthority, photoFile);
            String absoluteFilePath = photoFile.getAbsolutePath();

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivityForResult(intent, REQ_TAKE_PHOTO);
                return absoluteFilePath;
            } else {
                Log.d("JJY", "takePhoto - no activity to handle this intent");
            }
        } catch (IOException ex) {
            // Error occurred while creating the File
            ex.printStackTrace();
        }
        return null;
    }

    private static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "jpeg_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        // mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static boolean checkPermissions(Context context, String[] neededPermission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (neededPermission == null || neededPermission.length < 1) {
            return true;
        }

        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : neededPermission) {
            int permissionStatus = ContextCompat.checkSelfPermission(context, permission);
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                Log.i("JJY", "checkPermissions() - need " + permission);
                listPermissionsNeeded.add(permission);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            Log.i("JJY", "checkPermissions() - send request size=" + listPermissionsNeeded.size());
            return false;
        }
        return true;
    }

    public static boolean checkAndRequestPermissions(AppCompatActivity context, int requestCode, String[] neededPermission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (neededPermission == null || neededPermission.length < 1) {
            return true;
        }

        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : neededPermission) {
            int permissionStatus = ContextCompat.checkSelfPermission(context, permission);
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                Log.i("JJY", "checkAndRequestPermissions() - need " + permission);
                listPermissionsNeeded.add(permission);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            Log.i("JJY", "checkAndRequestPermissions() - send request size=" + listPermissionsNeeded.size());
            ActivityCompat.requestPermissions(context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), requestCode);
            return false;
        }
        return true;
    }

    public static boolean checkAndRequestPermissions(Fragment context, int requestCode, String[] neededPermission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (neededPermission == null || neededPermission.length < 1) {
            return true;
        }

        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : neededPermission) {
            int permissionStatus = ContextCompat.checkSelfPermission(context.getActivity(), permission);
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                Log.i("JJY", "checkAndRequestPermissions() - need " + permission);
                listPermissionsNeeded.add(permission);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            Log.i("JJY", "checkAndRequestPermissions() - send request size=" + listPermissionsNeeded.size());
            context.requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), requestCode);
            return false;
        }
        return true;
    }
}
