package com.cryptowallet.deviantx.UI.Activities;


import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Fragments.AirDropFragment;
import com.cryptowallet.deviantx.UI.Fragments.DashboardFragment;
import com.cryptowallet.deviantx.UI.Fragments.ExploreCoinsFragment;
import com.cryptowallet.deviantx.UI.Fragments.ToolsFragment;
import com.cryptowallet.deviantx.UI.Receiver.RefreshServiceReceiver;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.TriangularPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;


public class DashBoardActivity extends AppCompatActivity {

    private Boolean exit = false;
    //    Bottom Navigation Layout (btm_nav_lyt) Widgets
    @Nullable
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @Nullable
    @BindView(R.id.txt_btm_nav_lbl)
    TextView txt_btm_nav_lbl;
    @Nullable
    @BindView(R.id.tool_nav)
    Toolbar toolbar_nav;
    @Nullable
    @BindView(R.id.img_tlbr_nav)
    ImageView img_tlbr_nav;
    @Nullable
    @BindView(R.id.txt_tlbr_title)
    TextView txt_tlbr_title;
    @Nullable
    @BindView(R.id.img_tlbr_search)
    ImageView img_tlbr_search;

    @Nullable
    @BindView(R.id.nav_drwr)
    NavigationView nav_drwr;
    @Nullable
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;

    @Nullable
    @BindView(R.id.view_nav_drwr_wallet)
    View view_nav_drwr_wallet;
    @Nullable
    @BindView(R.id.view_nav_drwr_settings)
    View view_nav_drwr_settings;
    @Nullable
    @BindView(R.id.view_nav_drwr_deviant)
    View view_nav_drwr_deviant;
    @Nullable
    @BindView(R.id.view_nav_drwr_help)
    View view_nav_drwr_help;
    @Nullable
    @BindView(R.id.img_nav_logo)
    ImageView img_nav_logo;
    @Nullable
    @BindView(R.id.img_nav_drwr_wallet)
    ImageView img_nav_drwr_wallet;
    @Nullable
    @BindView(R.id.img_nav_drwr_settings)
    ImageView img_nav_drwr_settings;
    @Nullable
    @BindView(R.id.img_nav_drwr_deviant)
    ImageView img_nav_drwr_deviant;
    @Nullable
    @BindView(R.id.img_nav_drwr_help)
    ImageView img_nav_drwr_help;
    @Nullable
    @BindView(R.id.txt_nav_lbl)
    TextView txt_nav_lbl;
    @Nullable
    @BindView(R.id.txt_nav_email)
    TextView txt_nav_email;
    @Nullable
    @BindView(R.id.txt_nav_drwr_wallet)
    TextView txt_nav_drwr_wallet;
    @Nullable
    @BindView(R.id.txt_nav_drwr_settings)
    TextView txt_nav_drwr_settings;
    @Nullable
    @BindView(R.id.txt_nav_drwr_deviant)
    TextView txt_nav_drwr_deviant;
    @Nullable
    @BindView(R.id.txt_nav_drwr_help)
    TextView txt_nav_drwr_help;
    @Nullable
    @BindView(R.id.lnr_nav_drwr_wallet)
    LinearLayout lnr_nav_drwr_wallet;
    @Nullable
    @BindView(R.id.lnr_nav_drwr_settings)
    LinearLayout lnr_nav_drwr_settings;
    @Nullable
    @BindView(R.id.lnr_nav_drwr_deviant)
    LinearLayout lnr_nav_drwr_deviant;
    @Nullable
    @BindView(R.id.lnr_nav_drwr_help)
    LinearLayout lnr_nav_drwr_help;

    int[] CHANNELSImage = new int[]{R.drawable.selector_btm_nav_dashboard, R.drawable.selector_btm_nav_exp_coins, R.drawable.selector_btm_nav_airdrop, R.drawable.selector_btm_nav_tools/*, R.drawable.selector_btm_nav_acc_list*/, R.drawable.ic_exchange_unselected};
    int[] channelsName = new int[]{R.string.dashboard, R.string.explore_coins, R.string.airdrop, R.string.tools, R.string.exchange};
    int[] channelTtlName = new int[]{R.string.app_name, R.string.devx_coin_list, R.string.devx_airdrop, R.string.devx_tools, R.string.devx_exchange};

    @Nullable
    @BindView(R.id.lnr_nav_drwr_logout)
    LinearLayout lnr_nav_drwr_logout;
    @Nullable
    @BindView(R.id.lnr_nav_drwr_expcoins)
    LinearLayout lnr_nav_drwr_expcoins;
    @BindView(R.id.view_nav_drwr_expcoins)
    View view_nav_drwr_expcoins;
    @Nullable
    @BindView(R.id.img_nav_drwr_expcoins)
    ImageView img_nav_drwr_expcoins;
    @Nullable
    @BindView(R.id.txt_nav_drwr_expcoins)
    TextView txt_nav_drwr_expcoins;

    FragmentManager supportFragmentManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ViewPagerAdapter adapter;

    String loginResponseMsg, loginResponseStatus, loginResponseData;
    ProgressDialog progressDialog;

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
        CommonUtilities.serviceStart(DashBoardActivity.this);
    }

/*
    private void serviceStart() {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(DashBoardActivity.this, RefreshServiceReceiver.class);
        //intent.putExtra(ONE_TIME, Boolean.TRUE);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0,
                intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (1000 * 60 * 3), pi);

       */
/* AlarmManager alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(DashBoardActivity.this, RefreshServiceReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(DashBoardActivity.this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,System.currentTimeMillis(),60000, pendingIntent);
*//*

    }
*/

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        ButterKnife.bind(this);
//        Fabric.with(this, new Crashlytics());

        txt_btm_nav_lbl.setText(channelsName[0]);
        txt_tlbr_title.setText(channelTtlName[0]);

        supportFragmentManager = getSupportFragmentManager();
        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

//        Background Service
        CommonUtilities.serviceStart(DashBoardActivity.this);
//        serviceStart();


        txt_nav_lbl.setText(sharedPreferences.getString(CONSTANTS.usrnm, "MiniDeviant"));
        txt_nav_email.setText(sharedPreferences.getString(CONSTANTS.email, "test@deviantcoin.io"));

        img_tlbr_search.setVisibility(View.GONE);
        initMagicIndicator();
        int selectedTab = (getIntent().getIntExtra(CONSTANTS.seletedTab, 0));
        setAllSelection(selectedTab);

        txt_btm_nav_lbl.setTextColor(getResources().getColor(R.color.grey));

        lnr_nav_drwr_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navDrawerWallet();
            }
        });

        lnr_nav_drwr_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navDrawerSettings();
            }
        });

        lnr_nav_drwr_deviant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navDrawerDeviant();
            }
        });

        lnr_nav_drwr_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navDrawerHelp();
            }
        });
        lnr_nav_drwr_expcoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navDrawerExpCoins();
            }
        });
        lnr_nav_drwr_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviantXDB db = DeviantXDB.getDatabase(getApplicationContext());
                db.clearAllTables();
                CommonUtilities.sessionExpired(DashBoardActivity.this, getResources().getString(R.string.logout_success));
                Intent intent = new Intent(DashBoardActivity.this, RefreshServiceReceiver.class);
                stopService(intent);
            }
        });

        img_tlbr_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.openDrawer(Gravity.START);
            }
        });


    }

    private void setCurrentTabFragment(int tabPosition) {
        switch (tabPosition) {
            case 0:
                replaceFragment(new DashboardFragment());
                break;
            case 1:
                replaceFragment(new ExploreCoinsFragment());
                break;
            case 2:
                replaceFragment(new AirDropFragment());
                break;
            case 3:
                replaceFragment(new ToolsFragment());
                break;
            case 4:
                Intent intent = new Intent(DashBoardActivity.this, ExchangeDashBoardActivity.class);
                startActivity(intent);
                break;
        }
    }


    public void replaceFragment(Fragment fragment) {
        /*FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.frame_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();*/

        Fragment f1 = supportFragmentManager.findFragmentByTag(fragment.getClass().getName());

        if (f1 == null) {
            f1 = fragment;
            supportFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.frame_container, f1, fragment.getClass().getName())
                    .addToBackStack(fragment.getClass().getName()).commit();
        } else {
            FragmentTransaction transaction = supportFragmentManager.beginTransaction();
            DashboardFragment dashboardFragment = (DashboardFragment) supportFragmentManager.findFragmentByTag(DashboardFragment.class.getName());
            ExploreCoinsFragment exploreCoinsFragment = (ExploreCoinsFragment) supportFragmentManager.findFragmentByTag(ExploreCoinsFragment.class.getName());
            AirDropFragment airDropFragment = (AirDropFragment) supportFragmentManager.findFragmentByTag(AirDropFragment.class.getName());
            ToolsFragment toolsFragment = (ToolsFragment) supportFragmentManager.findFragmentByTag(ToolsFragment.class.getName());
            if (toolsFragment != null)
                if (toolsFragment != f1)
                    transaction.hide(toolsFragment);

            if (dashboardFragment != null)
                if (dashboardFragment != f1)
                    transaction.hide(dashboardFragment);

            if (exploreCoinsFragment != null)
                if (exploreCoinsFragment != f1)
                    transaction.hide(exploreCoinsFragment);

            if (airDropFragment != null)
                if (airDropFragment != f1)
                    transaction.hide(airDropFragment);

            transaction.show(f1);
            transaction.commit();
            // supportFragmentManager.beginTransaction()
            //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            //  .replace(R.id.frame_container, f1, fragment.getClass().getName())
            // .addToBackStack(fragment.getClass().getName()).commit();
        }


    }

    private void navDrawerWallet() {
        view_nav_drwr_wallet.setVisibility(View.VISIBLE);
        view_nav_drwr_settings.setVisibility(View.GONE);
        view_nav_drwr_expcoins.setVisibility(View.GONE);

        img_nav_drwr_wallet.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_wallet_selected));
        txt_nav_drwr_wallet.setTextColor(getResources().getColor(R.color.yellow));

        img_nav_drwr_settings.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_settings_unselected));
        txt_nav_drwr_settings.setTextColor(getResources().getColor(R.color.grey));
        img_nav_drwr_expcoins.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_expcoins_unselected));
        txt_nav_drwr_expcoins.setTextColor(getResources().getColor(R.color.grey));

        Intent intent = new Intent(DashBoardActivity.this, WalletListActivity.class);
        startActivityForResult(intent, 100);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            // mViewPager.setCurrentItem(0, true);
           /* if (mViewPager.getCurrentItem() == 0 || mViewPager.getCurrentItem() == 1)
                setupViewPager(mViewPager);*/
//            txt_btm_nav_lbl.setTextColor(getResources().getColor(R.color.yellow));
        }/* else if (requestCode == 200) {
            setupViewPagerAD(mViewPager);
        }*/
    }

    private void navDrawerSettings() {
        view_nav_drwr_settings.setVisibility(View.VISIBLE);
        view_nav_drwr_wallet.setVisibility(View.GONE);
        view_nav_drwr_expcoins.setVisibility(View.GONE);
//        view_nav_drwr_deviant.setVisibility(View.GONE);
//        view_nav_drwr_help.setVisibility(View.GONE);

        img_nav_drwr_settings.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_settings_selected));
        txt_nav_drwr_settings.setTextColor(getResources().getColor(R.color.yellow));

        img_nav_drwr_wallet.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_wallet_unselected));
        txt_nav_drwr_wallet.setTextColor(getResources().getColor(R.color.grey));
        img_nav_drwr_expcoins.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_expcoins_unselected));
        txt_nav_drwr_expcoins.setTextColor(getResources().getColor(R.color.grey));
//        img_nav_drwr_deviant.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_x_unselected));
//        txt_nav_drwr_deviant.setTextColor(getResources().getColor(R.color.grey));
//        img_nav_drwr_help.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_help_unselected));
//        txt_nav_drwr_help.setTextColor(getResources().getColor(R.color.grey));

        Intent intent = new Intent(DashBoardActivity.this, AppSettingsActivity.class);
        startActivity(intent);
    }

    private void navDrawerExpCoins() {
        view_nav_drwr_expcoins.setVisibility(View.VISIBLE);
        view_nav_drwr_wallet.setVisibility(View.GONE);
        view_nav_drwr_settings.setVisibility(View.GONE);

        img_nav_drwr_expcoins.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_expcoins_selected));
        txt_nav_drwr_expcoins.setTextColor(getResources().getColor(R.color.yellow));

        img_nav_drwr_wallet.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_wallet_unselected));
        txt_nav_drwr_wallet.setTextColor(getResources().getColor(R.color.grey));
        img_nav_drwr_settings.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_settings_unselected));
        txt_nav_drwr_settings.setTextColor(getResources().getColor(R.color.grey));

        Intent intent = new Intent(DashBoardActivity.this, ExploreCoinsActivity.class);
        startActivity(intent);

    }

    private void navDrawerDeviant() {
//        view_nav_drwr_deviant.setVisibility(View.VISIBLE);
//        view_nav_drwr_wallet.setVisibility(View.GONE);
//        view_nav_drwr_settings.setVisibility(View.GONE);
//        view_nav_drwr_help.setVisibility(View.GONE);
//
//        img_nav_drwr_deviant.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_x_selected));
//        txt_nav_drwr_deviant.setTextColor(getResources().getColor(R.color.yellow));
//
//        img_nav_drwr_wallet.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_wallet_unselected));
//        txt_nav_drwr_wallet.setTextColor(getResources().getColor(R.color.grey));
//        img_nav_drwr_settings.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_settings_unselected));
//        txt_nav_drwr_settings.setTextColor(getResources().getColor(R.color.grey));
//        img_nav_drwr_help.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_help_unselected));
//        txt_nav_drwr_help.setTextColor(getResources().getColor(R.color.grey));
//
//        Intent intent = new Intent(DashBoardActivity.this, DeviantXActivity.class);
//        startActivity(intent);
    }

    private void navDrawerHelp() {
//        view_nav_drwr_help.setVisibility(View.VISIBLE);
//        view_nav_drwr_wallet.setVisibility(View.GONE);
//        view_nav_drwr_settings.setVisibility(View.GONE);
//        view_nav_drwr_deviant.setVisibility(View.GONE);
//
//        img_nav_drwr_help.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_help_selected));
//        txt_nav_drwr_help.setTextColor(getResources().getColor(R.color.yellow));
//
//        img_nav_drwr_wallet.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_wallet_unselected));
//        txt_nav_drwr_wallet.setTextColor(getResources().getColor(R.color.grey));
//        img_nav_drwr_settings.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_settings_unselected));
//        txt_nav_drwr_settings.setTextColor(getResources().getColor(R.color.grey));
//        img_nav_drwr_deviant.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_x_unselected));
//        txt_nav_drwr_deviant.setTextColor(getResources().getColor(R.color.grey));
//
//
//        Intent intent = new Intent(DashBoardActivity.this, HelpActivity.class);
//        startActivity(intent);
    }

    private void initMagicIndicator() {
        final CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return CHANNELSImage == null ? 0 : CHANNELSImage.length;
            }

            /*  @Override
              public IPagerTitleView getTitleView(Context context, final int index) {
                  return new DummyPagerTitleView(context);
              }*/
            @Override
            public IPagerTitleView getTitleView(final Context context, final int index) {
                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);

                // load custom layout
                View customLayout = LayoutInflater.from(context).inflate(R.layout.simple_pager_title_layout, null);
                final ImageView titleImg = (ImageView) customLayout.findViewById(R.id.title_img);
                titleImg.setImageResource(CHANNELSImage[index]);
                commonPagerTitleView.setContentView(customLayout);

                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

                    @Override
                    public void onSelected(int index, int totalCount) {
                        titleImg.setSelected(true);
                    }

                    @Override
                    public void onDeselected(int index, int totalCount) {
                        titleImg.setSelected(false);
                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
//                        titleImg.setScaleX(1.3f + (0.8f - 1.3f) * leavePercent);
//                        titleImg.setScaleY(1.3f + (0.8f - 1.3f) * leavePercent);
                    }

                    @Override
                    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
//                        titleImg.setScaleX(0.8f + (1.3f - 0.8f) * enterPercent);
//                        titleImg.setScaleY(0.8f + (1.3f - 0.8f) * enterPercent);
                    }
                });

                commonPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setAllSelection(index);
                    }
                });

                return commonPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                TriangularPagerIndicator indicator = new TriangularPagerIndicator(context);
                indicator.setReverse(false);
                float smallNavigatorHeight = context.getResources().getDimension(R.dimen.small_navigator_height);
                indicator.setLineHeight(UIUtil.dip2px(context, 5));
                indicator.setTriangleHeight((int) smallNavigatorHeight);
                indicator.setLineColor(Color.parseColor("#FBB03B"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
    }

    private void setAllSelection(int index) {
        setCurrentTabFragment(index);
        if (index != 4) {
            txt_btm_nav_lbl.setText(channelsName[index]);
            txt_tlbr_title.setText(channelTtlName[index]);
            magicIndicator.onPageSelected(index);
            magicIndicator.onPageScrollStateChanged(index);
            magicIndicator.onPageScrolled(index, 0, 0);
        }
    }

    /**
     * Back button listener.
     * Will close the application if the back button pressed twice.
     */
    @Override
    public void onBackPressed() {
        if (exit) {
            Intent intent = new Intent(DashBoardActivity.this, RefreshServiceReceiver.class);
            stopService(intent);
            finishAffinity(); // Close all activites
            System.exit(0);  // Releasing resources
//            Toast.makeText(this, "Exit.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 2 * 1000);
        }
    }

/*    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
//            Log.e("home key pressed", "****");
            // write your code here to stop the activity
            CommonUtilities.serviceStop(DashBoardActivity.this);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
//        Log.e("home key pressed on pause", "****");
        // write your code here to stop your service
        CommonUtilities.serviceStop(DashBoardActivity.this);
        super.onPause();
    }*/

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}

