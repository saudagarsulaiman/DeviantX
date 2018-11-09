package com.cryptowallet.deviantx.UI.Activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Fragments.AccountListFragment;
import com.cryptowallet.deviantx.UI.Fragments.DashboardFragment;
import com.cryptowallet.deviantx.UI.Fragments.ExploreCoinsFragment;
import com.cryptowallet.deviantx.UI.Fragments.ToolsFragment;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
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
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
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

    //    Activity DashBoard
    @Nullable
    @BindView(R.id.nav_drwr)
    NavigationView nav_drwr;
    @Nullable
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;

    //    Navigation Drawer Layout (nav_drwr_layout) Widgets
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

    int[] CHANNELSImage = new int[]{R.drawable.selector_btm_nav_dashboard, R.drawable.selector_btm_nav_exp_coins, R.drawable.selector_btm_nav_acc_list, R.drawable.selector_btm_nav_tools};
    int[] channelsName = new int[]{R.string.dashboard, R.string.explore_coins, R.string.account_list, R.string.tools};
    int[] channelTtlName = new int[]{R.string.app_name, R.string.devx_coin_list, R.string.devx_wallet, R.string.devx_tools};

    @Nullable
    @BindView(R.id.lnr_nav_drwr_logout)
    LinearLayout lnr_nav_drwr_logout;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        ButterKnife.bind(this);
        txt_btm_nav_lbl.setText(channelsName[0]);
        txt_tlbr_title.setText(channelTtlName[0]);


        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        txt_nav_lbl.setText(sharedPreferences.getString(CONSTANTS.usrnm, "MiniDeviant"));
        txt_nav_email.setText(sharedPreferences.getString(CONSTANTS.email, "test@deviantcoin.io"));

//        BottomNavigationViewHelper.disableShiftMode(btm_nav);
//      Icon Tint Mode
        //  btm_nav.setItemIconTintList(null);

//        Fragments Replacements
        // loadFragment(new DashboardFragment());
        img_tlbr_search.setVisibility(View.GONE);
        setupViewPager(mViewPager);
        initMagicIndicator();


       /* btm_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.item_btm_nav_dashboard:
                        img_tlbr_search.setVisibility(View.GONE);
                        txt_btm_nav_lbl.setText(R.string.dashboard);
                        view_line.setBackgroundColor(getResources().getColor(R.color.yellow));
                        loadFragment(new DashboardFragment());
                        return true;
                    case R.id.item_btm_nav_exp_coins:
                        img_tlbr_search.setVisibility(View.VISIBLE);
                        txt_btm_nav_lbl.setText(R.string.explore_coins);
                        view_line.setBackgroundColor(getResources().getColor(R.color.mar_red));
                        loadFragment(new ExploreCoinsFragment());
                        return true;
                    case R.id.item_btm_nav_acc_list:
                        img_tlbr_search.setVisibility(View.VISIBLE);
                        txt_btm_nav_lbl.setText(R.string.account_list);
                        view_line.setBackgroundColor(getResources().getColor(R.color.l_blue));
                        loadFragment(new AccountListFragment());
                        return true;
                    case R.id.item_btm_nav_tools:
                        img_tlbr_search.setVisibility(View.GONE);
                        txt_btm_nav_lbl.setText(R.string.tools);
                        view_line.setBackgroundColor(getResources().getColor(R.color.brinjal));
                        loadFragment(new ToolsFragment());
                        return true;
                }
                return false;
            }
        });*/

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
        lnr_nav_drwr_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtilities.sessionExpired(DashBoardActivity.this, getResources().getString(R.string.logout_success));
            }
        });

        img_tlbr_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.openDrawer(Gravity.START);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DashboardFragment(), "");
        adapter.addFragment(new ExploreCoinsFragment(), "");
        adapter.addFragment(new AccountListFragment(), "");
        adapter.addFragment(new ToolsFragment(), "");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                txt_btm_nav_lbl.setText(channelsName[i]);
                txt_tlbr_title.setText(channelTtlName[i]);
                switch (i) {
                    case 0:
                        txt_btm_nav_lbl.setTextColor(getResources().getColor(R.color.yellow));
                        break;
                    case 1:
                        txt_btm_nav_lbl.setTextColor(getResources().getColor(R.color.mar_red));
                        break;
                    case 2:
                        txt_btm_nav_lbl.setTextColor(getResources().getColor(R.color.sky_blue));
                        break;
                    case 3:
                        txt_btm_nav_lbl.setTextColor(getResources().getColor(R.color.brinjal));
                        break;
                    default:
                        txt_btm_nav_lbl.setTextColor(getResources().getColor(R.color.grey));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    private void navDrawerWallet() {
        view_nav_drwr_wallet.setVisibility(View.VISIBLE);
        view_nav_drwr_settings.setVisibility(View.GONE);
        view_nav_drwr_deviant.setVisibility(View.GONE);
        view_nav_drwr_help.setVisibility(View.GONE);


        img_nav_drwr_wallet.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_wallet_selected));
        txt_nav_drwr_wallet.setTextColor(getResources().getColor(R.color.yellow));

        img_nav_drwr_settings.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_settings_unselected));
        txt_nav_drwr_settings.setTextColor(getResources().getColor(R.color.grey));
        img_nav_drwr_deviant.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_x_unselected));
        txt_nav_drwr_deviant.setTextColor(getResources().getColor(R.color.grey));
        img_nav_drwr_help.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_help_unselected));
        txt_nav_drwr_help.setTextColor(getResources().getColor(R.color.grey));


        Intent intent = new Intent(DashBoardActivity.this, WalletListActivity.class);
        startActivity(intent);

    }

    private void navDrawerSettings() {
        view_nav_drwr_settings.setVisibility(View.VISIBLE);
        view_nav_drwr_wallet.setVisibility(View.GONE);
        view_nav_drwr_deviant.setVisibility(View.GONE);
        view_nav_drwr_help.setVisibility(View.GONE);

        img_nav_drwr_settings.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_settings_selected));
        txt_nav_drwr_settings.setTextColor(getResources().getColor(R.color.yellow));

        img_nav_drwr_wallet.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_wallet_unselected));
        txt_nav_drwr_wallet.setTextColor(getResources().getColor(R.color.grey));
        img_nav_drwr_deviant.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_x_unselected));
        txt_nav_drwr_deviant.setTextColor(getResources().getColor(R.color.grey));
        img_nav_drwr_help.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_help_unselected));
        txt_nav_drwr_help.setTextColor(getResources().getColor(R.color.grey));

        Intent intent = new Intent(DashBoardActivity.this, AppSettingsActivity.class);
        startActivity(intent);
    }

    private void navDrawerDeviant() {
        view_nav_drwr_deviant.setVisibility(View.VISIBLE);
        view_nav_drwr_wallet.setVisibility(View.GONE);
        view_nav_drwr_settings.setVisibility(View.GONE);
        view_nav_drwr_help.setVisibility(View.GONE);

        img_nav_drwr_deviant.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_x_selected));
        txt_nav_drwr_deviant.setTextColor(getResources().getColor(R.color.yellow));

        img_nav_drwr_wallet.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_wallet_unselected));
        txt_nav_drwr_wallet.setTextColor(getResources().getColor(R.color.grey));
        img_nav_drwr_settings.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_settings_unselected));
        txt_nav_drwr_settings.setTextColor(getResources().getColor(R.color.grey));
        img_nav_drwr_help.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_help_unselected));
        txt_nav_drwr_help.setTextColor(getResources().getColor(R.color.grey));

        Intent intent = new Intent(DashBoardActivity.this, DeviantXActivity.class);
        startActivity(intent);
    }

    private void navDrawerHelp() {
        view_nav_drwr_help.setVisibility(View.VISIBLE);
        view_nav_drwr_wallet.setVisibility(View.GONE);
        view_nav_drwr_settings.setVisibility(View.GONE);
        view_nav_drwr_deviant.setVisibility(View.GONE);

        img_nav_drwr_help.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_help_selected));
        txt_nav_drwr_help.setTextColor(getResources().getColor(R.color.yellow));

        img_nav_drwr_wallet.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_wallet_unselected));
        txt_nav_drwr_wallet.setTextColor(getResources().getColor(R.color.grey));
        img_nav_drwr_settings.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_settings_unselected));
        txt_nav_drwr_settings.setTextColor(getResources().getColor(R.color.grey));
        img_nav_drwr_deviant.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_drwr_x_unselected));
        txt_nav_drwr_deviant.setTextColor(getResources().getColor(R.color.grey));


        Intent intent = new Intent(DashBoardActivity.this, HelpActivity.class);
        startActivity(intent);
    }


    //  Fragments Replacements
   /* private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }*/

    /**
     * Back button listener.
     * Will close the application if the back button pressed twice.
     */
    @Override
    public void onBackPressed() {
        if (exit) {
/*
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
*/
            finishAffinity(); // Close all activites
            System.exit(0);  // Releasing resources
            Toast.makeText(this, "Logged Out Successfully.", Toast.LENGTH_SHORT).show();
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
                        titleImg.setScaleX(1.3f + (0.8f - 1.3f) * leavePercent);
                        titleImg.setScaleY(1.3f + (0.8f - 1.3f) * leavePercent);
                    }

                    @Override
                    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
                        titleImg.setScaleX(0.8f + (1.3f - 0.8f) * enterPercent);
                        titleImg.setScaleY(0.8f + (1.3f - 0.8f) * enterPercent);
                    }
                });

                commonPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
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
                indicator.setLineColor(Color.parseColor("#ed871a"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

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
