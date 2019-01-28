package com.cryptowallet.deviantx.Utilities;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.cryptowallet.deviantx.UI.Interfaces.AirdropWalletUIListener;
import com.cryptowallet.deviantx.UI.Interfaces.AllCoinsUIListener;
import com.cryptowallet.deviantx.UI.Interfaces.WalletUIChangeListener;
import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;

import java.lang.reflect.Field;
import java.util.Map;

//import com.google.android.gms.plus.Plus;


/*
 * Created by Sulaiman on 28/3/2018.
 */

public class MyApplication extends Application {

    public static MyApplication myApplication;

    public AppCompatActivity activity;
    SharedPreferences sharedPreferences;
    public WalletUIChangeListener walletUIChangeListener;
    public AllCoinsUIListener allCoinsUIListener;
    public AirdropWalletUIListener airdropWalletUIListener;

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

    public Boolean get2FA() {
        return is2FAactive;
    }

    public void set2FA(Boolean twoFA) {
        is2FAactive = twoFA;
    }

    public int getDefaultWallet() {
        return defaultWallet;
    }

    public void setDefaultWallet(int dDefaultWallet) {
        defaultWallet = dDefaultWallet;
    }


    public Boolean getAppPin() {
        return isAppPin;
    }

    public void setAppPin(Boolean appPin) {
        isAppPin = appPin;
    }

    Boolean isHideBalance;
    Boolean is2FAactive;
    Boolean isScreenShot;
    Boolean isAppPin;
    int defaultWallet;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        isHideBalance = sharedPreferences.getBoolean(CONSTANTS.hideBal, false);
        isScreenShot = sharedPreferences.getBoolean(CONSTANTS.screenshot, false);
        is2FAactive = sharedPreferences.getBoolean(CONSTANTS.twoFactorAuth, false);
        defaultWallet = sharedPreferences.getInt(CONSTANTS.defaultWallet, 0);
        isAppPin = sharedPreferences.getBoolean(CONSTANTS.is_app_pin, false);

        new Instabug.Builder(this, "c90aaf55987fd8140ea3ffb8470b98c9")
                .setInvocationEvents(InstabugInvocationEvent.SHAKE, InstabugInvocationEvent.SCREENSHOT)
                .build();
    }

    public void disableScreenCapture(Activity context) {
        if (isScreenShot) {
            context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        } else {
            context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }
    }

    public void setWalletUIChangeListener(WalletUIChangeListener walletUIChangeListener) {
        this.walletUIChangeListener = walletUIChangeListener;
    }

    public WalletUIChangeListener getWalletUIChangeListener() {
        return walletUIChangeListener;
    }

    public void setAllCoinsUIListener(AllCoinsUIListener allCoinsUIListener) {
        this.allCoinsUIListener = allCoinsUIListener;
    }

    public AllCoinsUIListener getAllCoinsUIListener() {
        return allCoinsUIListener;
    }

    public void setAirdropWalletUIListener(AirdropWalletUIListener airdropWalletUIListener) {
        this.airdropWalletUIListener = airdropWalletUIListener;
    }

    public AirdropWalletUIListener getAirdropWalletUIListener() {
        return airdropWalletUIListener;
    }

}
