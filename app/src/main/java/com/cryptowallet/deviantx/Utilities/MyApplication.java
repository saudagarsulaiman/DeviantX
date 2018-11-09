package com.cryptowallet.deviantx.Utilities;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.util.Map;

//import com.google.android.gms.plus.Plus;


/*
 * Created by Sulaiman on 28/3/2018.
 */

public class MyApplication extends Application {
    Typeface font;

    public static MyApplication myApplication;

    public AppCompatActivity activity;
    SharedPreferences sharedPreferences;

    public Boolean getHideBalance() {
        return isHideBalance;
    }

    public void setHideBalance(Boolean hideBalance) {
        isHideBalance = hideBalance;
    }

    public Boolean getScreenShot() {
        return isScreenShot;
    }

    public void setScreenShot(Boolean screenShot) {
        isScreenShot = screenShot;
    }

    Boolean isHideBalance;
    Boolean isScreenShot;

    @Override
    public void onCreate() {
        super.onCreate();
        String strLocale = CommonUtilities.loadLocale(this);
        myApplication = this;
        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        isHideBalance = sharedPreferences.getBoolean(CONSTANTS.hideBal, false);
        isScreenShot = sharedPreferences.getBoolean(CONSTANTS.screenshot, false);
    }

    public void disableScreenCapture(Activity context) {
        if (isScreenShot) {
            context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);
        }else{
            context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }
    }


    private boolean injectTypeface(String fontFamily, Typeface typeface) {
        try {
            Field field = Typeface.class.getDeclaredField("sSystemFontMap");
            field.setAccessible(true);
            Object fieldValue = field.get(null);
            Map<String, Typeface> map = (Map<String, Typeface>) fieldValue;
            map.put(fontFamily, typeface);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            //Log.e("Font-Injection", "Failed to inject typeface.", e);
        }
        return true;
        //return false;
    }


}
