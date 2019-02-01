package com.cryptowallet.deviantx.Utilities;

import com.google.gson.Gson;

/**
 * Created by Sulaiman on 12/12/2018.
 */
public class GsonUtils {
    private static Gson gson = null;
    public static Gson getInstance(){
        if (gson == null)
            gson = new Gson();
        return gson;
    }
}
