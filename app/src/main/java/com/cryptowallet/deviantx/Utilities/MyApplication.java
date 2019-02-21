package com.cryptowallet.deviantx.Utilities;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.cryptowallet.deviantx.UI.Interfaces.AirdropWalletUIListener;
import com.cryptowallet.deviantx.UI.Interfaces.AirdropsHistoryUIListener;
import com.cryptowallet.deviantx.UI.Interfaces.AllCoinsUIListener;
import com.cryptowallet.deviantx.UI.Interfaces.CoinPairsUIListener;
import com.cryptowallet.deviantx.UI.Interfaces.DividendAirdropsUIListener;
import com.cryptowallet.deviantx.UI.Interfaces.ExcOrdersUIListener;
import com.cryptowallet.deviantx.UI.Interfaces.FeaturedAirdropsUIListener;
import com.cryptowallet.deviantx.UI.Interfaces.HeaderBannerUIListener;
import com.cryptowallet.deviantx.UI.Interfaces.NewsDXUIListener;
import com.cryptowallet.deviantx.UI.Interfaces.PairsListUIListener;
import com.cryptowallet.deviantx.UI.Interfaces.WalletDetailsUIListener;
import com.cryptowallet.deviantx.UI.Interfaces.WalletUIChangeListener;
import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;
import com.parse.Parse;

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
    public FeaturedAirdropsUIListener featuredAirdropsUIListener;
    public DividendAirdropsUIListener dividendAirdropsUIListener;
    public AirdropsHistoryUIListener airdropsHistoryUIListener;
    public NewsDXUIListener newsDXUIListener;
    public HeaderBannerUIListener headerBannerUIListener;
    public WalletDetailsUIListener walletDetailsUIListener;
    public CoinPairsUIListener coinPairsUIListener;
    public PairsListUIListener pairsListUIListener;
    public ExcOrdersUIListener excOrdersUIListener;

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

        new Instabug.Builder(this, "a3fffb4323814c6408c9b5eb7201b3e0")
                .setInvocationEvents(InstabugInvocationEvent.SHAKE, InstabugInvocationEvent.SCREENSHOT)
                .build();


        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(CONSTANTS.AppId)                // if desired
                .clientKey(CONSTANTS.MasterKey)
                .server(CONSTANTS.ServerUrl)
                .build()
        );


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

    public FeaturedAirdropsUIListener getFeaturedAirdropsUIListener() {
        return featuredAirdropsUIListener;
    }

    public void setFeaturedAirdropsUIListener(FeaturedAirdropsUIListener featuredAirdropsUIListener) {
        this.featuredAirdropsUIListener = featuredAirdropsUIListener;
    }

    public DividendAirdropsUIListener getDividendAirdropsUIListener() {
        return dividendAirdropsUIListener;
    }

    public void setDividendAirdropsUIListener(DividendAirdropsUIListener dividendAirdropsUIListener) {
        this.dividendAirdropsUIListener = dividendAirdropsUIListener;
    }

    public AirdropsHistoryUIListener getAirdropsHistoryUIListener() {
        return airdropsHistoryUIListener;
    }

    public void setAirdropsHistoryUIListener(AirdropsHistoryUIListener airdropsHistoryUIListener) {
        this.airdropsHistoryUIListener = airdropsHistoryUIListener;
    }

    public NewsDXUIListener getNewsDXUIListener() {
        return newsDXUIListener;
    }

    public void setNewsDXUIListener(NewsDXUIListener newsDXUIListener) {
        this.newsDXUIListener = newsDXUIListener;
    }

    public HeaderBannerUIListener getHeaderBannerUIListener() {
        return headerBannerUIListener;
    }

    public void setHeaderBannerUIListener(HeaderBannerUIListener headerBannerUIListener) {
        this.headerBannerUIListener = headerBannerUIListener;
    }

    public WalletDetailsUIListener getWalletDetailsUIListener() {
        return walletDetailsUIListener;
    }

    public void setWalletDetailsUIListener(WalletDetailsUIListener walletDetailsUIListener) {
        this.walletDetailsUIListener = walletDetailsUIListener;
    }

    public CoinPairsUIListener getCoinPairsUIListener() {
        return coinPairsUIListener;
    }

    public void setCoinPairsUIListener(CoinPairsUIListener coinPairsUIListener) {
        this.coinPairsUIListener = coinPairsUIListener;
    }

    public PairsListUIListener getPairsListUIListener() {
        return pairsListUIListener;
    }

    public void setPairsListUIListener(PairsListUIListener pairsListUIListener) {
        this.pairsListUIListener = pairsListUIListener;
    }

    public ExcOrdersUIListener getExcOrdersUIListener() {
        return excOrdersUIListener;
    }

    public void setExcOrdersUIListener(ExcOrdersUIListener excOrdersUIListener) {
        this.excOrdersUIListener = excOrdersUIListener;
    }
}
