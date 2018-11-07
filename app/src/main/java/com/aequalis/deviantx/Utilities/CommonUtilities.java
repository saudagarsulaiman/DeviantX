package com.aequalis.deviantx.Utilities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.aequalis.deviantx.R;
import com.aequalis.deviantx.UI.Activities.LoginActivity;
import com.aequalis.deviantx.UI.Activities.ReceiveCoinActivity;
import com.aequalis.deviantx.UI.Activities.WelcomeActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public final class CommonUtilities {
    /******************** * Test Server Links  ****************************************************/

    //Local Link
    public static final String URL = "http://178.128.15.223:7070";

    //Client Link
//    public static final String URL = "http://";


    private static Locale myLocale;


    /********************* Live Server Links ****************************************************/

//    public static final String URL = "http:// ";
    public static boolean isConnectionAvailable(Context ctx) {
        //boolean bConnection = false;
        ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

//    private boolean haveNetworkConnection(Context ctx) {
//        boolean haveConnectedWifi = false;
//        boolean haveConnectedMobile = false;
//        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
//        for (NetworkInfo ni : netInfo) {
//            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
//                if (ni.isConnected())
//                    haveConnectedWifi = true;
//            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
//                if (ni.isConnected())
//                    haveConnectedMobile = true;
//        }
//        return haveConnectedWifi || haveConnectedMobile;
//    }

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
        if (text.matches("(?=^.{8,25}$)(?=.*\\d)(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$")) {
            txt_lower_case.setBackground(activity.getResources().getDrawable(R.drawable.rec_green_c2));
            txt_upper_case.setBackground(activity.getResources().getDrawable(R.drawable.rec_green_c2));
            txt_number.setBackground(activity.getResources().getDrawable(R.drawable.rec_green_c2));
            txt_chars.setBackground(activity.getResources().getDrawable(R.drawable.rec_green_c2));
        } else {
            if (text.matches("(?![.\\n])(?=.*[a-z]).*$+")) {
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
        SharedPreferences sp = ctx.getSharedPreferences("config_info", 0);
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
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, 500, 500, null);
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

    public static Double getCoinUsdValue(String str_coin_code) {
        Double result = 0.0;
        switch (str_coin_code) {
            case "BTC":
                break;
            case "BCH":
                break;
            case "ETH":
                break;
            case "DASH":
                break;
            case "LTC":
                break;
            case "ETC":
                break;
            case "ZEC":
                break;
            case "XRP":
                break;
            case "DOGE":
                break;
        }
        return result;
    }

    public static Double getCoinValue(double userUsdValue, String str_coin_code, double str_coin_usdValue) {
        Double result = 0.0;
        switch (str_coin_code) {
            case "BTC":
                break;
            case "BCH":
                break;
            case "ETH":
                break;
            case "DASH":
                break;
            case "LTC":
                break;
            case "ETC":
                break;
            case "ZEC":
                break;
            case "XRP":
                break;
            case "DOGE":
                break;
        }
        return result;
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


}


