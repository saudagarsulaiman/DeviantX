package com.aequalis.deviantx.Utilities;

import android.app.Application;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.Map;

//import com.google.android.gms.plus.Plus;


/*
 * Created by Sulaiman on 28/3/2018.
 */

public class MyApplication extends Application {
    Typeface font;

    public AppCompatActivity activity;

    @Override
    public void onCreate() {
        super.onCreate();
        String strLocale = CommonUtilities.loadLocale(this);
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
