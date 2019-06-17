package com.cryptowallet.deviantx.Utilities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.LocaleList;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Activities.WelcomeActivity;
import com.cryptowallet.deviantx.UI.Receiver.RefreshServiceReceiver;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public final class CommonUtilities {
    /******************** * Test Server Links  ****************************************************/

    //Main Link
//    public static final String URL = "http://142.93.51.57:3322";

    //Local Link
//    public static final String URL = "http://192.168.0.179:3322";
//    public static final String URL = "http://192.168.0.111:3322";

/*
    public static final String URL = "http://10.0.0.35:3322";
    public static final String WS = "http://10.0.0.35:3323/ws_v2/deviant/websocket";
*/
/*
    public static final String URL = "http://10.0.0.27:3322";
    public static final String WS = "http://10.0.0.27:3323/ws_v2/deviant/websocket";
*/
/*
    public static final String URL = "http://10.0.0.39:3322";
    public static final String WS = "http://10.0.0.39:3323/ws_v2/deviant/websocket";
*/
/*
    public static final String URL = "http://10.0.0.16:3322";
    public static final String WS = "http://10.0.0.16:3323/ws_v2/deviant/websocket";
*/

    private static Locale myLocale;


    /********************* Live Server Links ****************************************************/

    public static final String URL = "https://deviantx.app";
    public static final String WS = "wss://deviantx.app/ws_v2/deviant/websocket";

    //    ********************* Reusable Methods*******************************************************
    public static boolean isConnectionAvailable(Context ctx) {
        //boolean bConnection = false;
        ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean hideBalance(Context ctx) {
        boolean hide_bal = false;
        SharedPreferences prefs = ctx.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        hide_bal = prefs.getBoolean("HIDEBALANCE", false);
        return hide_bal;
    }

    public static boolean disableScreennshots(Context ctx) {
        boolean disable = false;
        SharedPreferences prefs = ctx.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        disable = prefs.getBoolean("SCREENSHOT", false);
        return disable;
    }

    public static boolean lightMode(Context ctx) {
        boolean lightmode = false;
        SharedPreferences prefs = ctx.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        lightmode = prefs.getBoolean("LIGHTMODE", false);
        return lightmode;
    }


    public static void saveLocale(Context ctx, String lang) {
        String langPref = "Language";
        SharedPreferences prefs = ctx.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }

    public static String loadLocale(Context ctx) {
        String langPref = "Language";
        SharedPreferences prefs = ctx.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "en");
        changeLang(ctx, language);
        return language;
    }

    public static void changeLang(Context ctx, String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(ctx, lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
//		ctx.getResources().updateConfiguration(config, ctx.getResources().getDisplayMetrics());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            LocaleList localeList = new LocaleList(myLocale);
            LocaleList.setDefault(localeList);
            config.setLocales(localeList);
            config.setLocale(myLocale);
        } else {
            config.locale = myLocale;
            ctx.getResources().updateConfiguration(config, ctx.getResources().getDisplayMetrics());
        }
    }


    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 121;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 321;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkGalleryPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(false);
                    alertBuilder.setTitle(context.getResources().getString(R.string.Prmsns_nec));
                    alertBuilder.setMessage(context.getResources().getString(R.string.Prmsns_extrnl_strg));
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkCameraPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(false);
                    alertBuilder.setTitle(context.getResources().getString(R.string.Prmsns_nec));
                    alertBuilder.setMessage(context.getResources().getString(R.string.Prmsns_cam));
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkExtrnlStrgPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(false);
                    alertBuilder.setTitle(context.getResources().getString(R.string.Prmsns_nec));
                    alertBuilder.setMessage(context.getResources().getString(R.string.Prmsns_extrnl_strg));
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public static void sessionExpired(Activity activity, String loginResponseMsg) {
        ShowToastMessage(activity, loginResponseMsg);
        SharedPreferences prefs = activity.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(CONSTANTS.token, null);
        editor.putString(CONSTANTS.usrnm, null);
        editor.putString(CONSTANTS.email, null);
        editor.putString(CONSTANTS.pswd, null);
        editor.putBoolean(CONSTANTS.seed, false);
        editor.apply();
        Intent intent = new Intent(activity, WelcomeActivity.class);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }


    public static void matchingPasswordText(Activity activity, String text, TextView txt_lower_case, TextView txt_upper_case, TextView txt_number, TextView txt_chars) {
//        if (text.matches("(?=^.{8,25}$)(?=.*\\d)(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$")) {
        if (text.matches("(?=^.{8,25}$)(?=.*\\d)(?![.\\n])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=!*_]).*$")) {
            txt_lower_case.setBackground(activity.getResources().getDrawable(R.drawable.rec_green_c2));
            txt_upper_case.setBackground(activity.getResources().getDrawable(R.drawable.rec_green_c2));
            txt_number.setBackground(activity.getResources().getDrawable(R.drawable.rec_green_c2));
            txt_chars.setBackground(activity.getResources().getDrawable(R.drawable.rec_green_c2));
        } else {
//            if (text.matches("(?![.\\n])(?=.*[a-z]).*$+")) {
            if (text.matches("(?![.\\n])(?=.*[@#$%^&+=!*_]).*$+")) {
                txt_lower_case.setBackground(activity.getResources().getDrawable(R.drawable.rec_green_c2));
            } else {
                txt_lower_case.setBackground(activity.getResources().getDrawable(R.drawable.rec_marred_c2));
            }
            if (text.matches("(?![.\\n])(?=.*[A-Z]).*$+")) {
                txt_upper_case.setBackground(activity.getResources().getDrawable(R.drawable.rec_green_c2));
            } else {
                txt_upper_case.setBackground(activity.getResources().getDrawable(R.drawable.rec_marred_c2));
            }

            if (text.matches("(?![.\\n])(?=.*\\d).*$+")) {
                txt_number.setBackground(activity.getResources().getDrawable(R.drawable.rec_green_c2));
            } else {
                txt_number.setBackground(activity.getResources().getDrawable(R.drawable.rec_marred_c2));
            }

            if (text.length() > 7 && text.length() < 26) {
                txt_chars.setBackground(activity.getResources().getDrawable(R.drawable.rec_green_c2));
            } else {
                txt_chars.setBackground(activity.getResources().getDrawable(R.drawable.rec_marred_c2));
            }
        }
    }


    public static String convertToHumanReadable(long milliseconds) {
        Calendar today = Calendar.getInstance();
        Calendar postedDay = Calendar.getInstance();
        postedDay.setTimeInMillis(milliseconds);
        long day = 86400000L;
        long hour = 3600000L;
        long minute = 60000L;
        long month = 2628000000L;
        long year = 31536000000L;
        int time;

        if (today.getTimeInMillis() - postedDay.getTimeInMillis() > year) {
            time = Math.round((today.getTimeInMillis() - postedDay.getTimeInMillis()) / year);
            return time + (time == 1 ? "yr" : "yrs");
        } else if (today.getTimeInMillis() - postedDay.getTimeInMillis() > month) {
            time = Math.round((today.getTimeInMillis() - postedDay.getTimeInMillis()) / month);
            return time + (time == 1 ? "m" : "m");
        } else if (today.getTimeInMillis() - postedDay.getTimeInMillis() > day) {
            time = Math.round((today.getTimeInMillis() - postedDay.getTimeInMillis()) / day);
            return time + (time == 1 ? "d" : "d");
        } else if (today.getTimeInMillis() - postedDay.getTimeInMillis() > hour) {
            time = Math.round((today.getTimeInMillis() - postedDay.getTimeInMillis()) / hour);
            return time + (time == 1 ? "hr" : "hrs");
        } else {
            time = Math.round((today.getTimeInMillis() - postedDay.getTimeInMillis()) / minute);
            return time + (time == 1 ? "min" : "mins");
        }
    }


//    public static boolean checkExtrnlStrgPermission(final Context context) {
//        int result = ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE);
//        int result1 = ContextCompat.checkSelfPermission(context, RECORD_AUDIO);
//        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void requestExtrnlStrgPermission(final Context context) {
//        ActivityCompat.requestPermissions((Activity) context, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
//    }


//    public static boolean isEmptyString(String inputString) {
//        return inputString == null || inputString.isEmpty();
//    }
//
//    public static boolean isValidEmailAddress(String inputString) {
//        String emailAddressPattern = "[a-zA-Z][a-zA-Z0-9._-]+@[a-z0-9.-]+\\.+[a-z]+[a-zA-Z]";
//        return inputString.matches(emailAddressPattern);
//    }

//    public static boolean isValidPassword(String InputPassword) {
//        Boolean passwordcheck = false;
//        if (InputPassword.isEmpty() || InputPassword.length() < 6) {
//
//            return passwordcheck;
//        } else {
//            passwordcheck = true;
//        }
//        return passwordcheck;
//    }

    public static void ShowToastMessage(Context mContext, String xMessage) {
        Toast mToast = Toast.makeText(mContext, xMessage, Toast.LENGTH_SHORT);
//        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }
//
//    public static void customTost(Context context, String message) {
//        Cue.init()
//                .with(context)
//                .setMessage(message)
//                .setType(Type.PRIMARY)
//                .show();
//    }

    public static String getValueFromSP(Context ctx, String Key) {
        SharedPreferences sp = ctx.getSharedPreferences("CommonPrefs", Context.MODE_PRIVATE);
        return sp.getString(Key, "");
    }

//    public static Double coinValueConverter(String str_coin_code) {
//
//        return null;
//    }

    //    QR Code Generator
    public static void qrCodeGenerate(String addressValue, ImageView imageView, Context context) {
        try {
            Bitmap bitmap = encodeAsBitmap(addressValue);
//            Picasso.with(context).load().into(imageView);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    static Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            Map<EncodeHintType, Object> hintMap = new HashMap<EncodeHintType, Object>();
            hintMap.put(EncodeHintType.MARGIN, new Integer(1));
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 500, 500, hintMap);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 500, 0, 0, w, h);
        return bitmap;
    }

    public static void copyToClipboard(Context context, String address, String str_coin_name) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(address);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText(str_coin_name + " address", address);
            clipboard.setPrimaryClip(clip);
        }
        CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.copied));
    }

    public static void shareAddress(String str_data_address, Context context) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, str_data_address);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }


    public static void serviceStart(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, RefreshServiceReceiver.class);
        //intent.putExtra(ONE_TIME, Boolean.TRUE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0,
                intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (1000 * 60 * 3), pi);

       /* AlarmManager alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(DashBoardActivity.this, RefreshServiceReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(DashBoardActivity.this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,System.currentTimeMillis(),60000, pendingIntent);
*/
    }

    public static void serviceStop(Context context) {
        Intent intent = new Intent(context, RefreshServiceReceiver.class);
        context.stopService(intent);
    }

    public static String getDeviceID(Context context) {
        String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//        Toast.makeText(context, "android_id= " + android_id, Toast.LENGTH_LONG).show();
        return android_id;
    }

    public static double getUSDValue(Double str_data_balance, Double dbl_coin_usdValue) {
        double result = 0.0;
        result = str_data_balance * dbl_coin_usdValue;
        return result;
    }


    public static double getTotalBal(Double reserved, Double avail) {
        double result = 0.0;
        result = reserved + avail;
        return result;
    }

    public static String covertMsToTime(long timestampInMilliSeconds) {
        timestampInMilliSeconds = timestampInMilliSeconds * 1000;
        Date date = new Date();
        date.setTime(timestampInMilliSeconds);
        String formattedDate = new SimpleDateFormat("HH:mm:ss").format(date);
        return formattedDate;

    }

    public static Date getTimeFromMS(long time) {
        Date date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Date d1 = calendar.getTime();
        date = d1;
        return date;
    }
}


