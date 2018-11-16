package com.cryptowallet.deviantx.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Fragments.StartedAFragment;
import com.cryptowallet.deviantx.UI.Fragments.StartedBFragment;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.sliderDotspanel)
    LinearLayout sliderDotspanel;
    @BindView(R.id.btn_get_started)
    Button btn_get_started;

    final Context context = this;
    private int dotscount;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ButterKnife.bind(this);


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new StartedAFragment(), "");
        viewPagerAdapter.addFragment(new StartedBFragment(), "");
        viewPager.setAdapter(viewPagerAdapter);

//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
//        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDotspanel.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btn_get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Creating A Custom Dialog Using DialogPlus
                ViewHolder viewHolder = new ViewHolder(R.layout.dialog_get_started);
                final DialogPlus dialog = DialogPlus.newDialog(WelcomeActivity.this)
                        .setContentHolder(viewHolder)
                        .setGravity(Gravity.BOTTOM)
                        .setCancelable(true)
                        .setInAnimation(R.anim.slide_in_bottom)
                        .setOutAnimation(R.anim.slide_out_bottom)
                        .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                        .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .create();

//                Initializing Widgets
                View view = dialog.getHolderView();
                Button signup_email_btn = view.findViewById(R.id.signup_email_btn);
                LinearLayout facebook_lnr_lyt = view.findViewById(R.id.facebook_lnr_lyt);
                LinearLayout google_lnr_lyt = view.findViewById(R.id.google_lnr_lyt);
                TextView login_txt = view.findViewById(R.id.login_txt);

//                Signing Up With Email
                signup_email_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent signup = new Intent(WelcomeActivity.this, SignUpEmailActivity.class);
                        startActivity(signup);
                    }
                });

//                Signing With Facebook
                facebook_lnr_lyt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent sign_fb = new Intent(WelcomeActivity.this, DashBoardActivity.class);
//                        startActivity(sign_fb);
                    }
                });

//                Signing With Google Plus
                google_lnr_lyt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent sign_google = new Intent(WelcomeActivity.this, DashBoardActivity.class);
//                        startActivity(sign_google);
                    }
                });

//                Signing With Existing Account
                login_txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sign_in = new Intent(WelcomeActivity.this, LoginActivity.class);
                        startActivity(sign_in);
                    }
                });

//                Displaying DialogPlus
                dialog.show();
            }
        });

    }


    public class ViewPagerAdapter  extends FragmentPagerAdapter {
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