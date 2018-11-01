package com.aequalis.deviantx.UI.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aequalis.deviantx.R;
import com.aequalis.deviantx.Utilities.CONSTANTS;
import com.aequalis.deviantx.Utilities.CommonUtilities;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aequalis.deviantx.Utilities.MyApplication.myApplication;

public class AppSettingsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.lnr_language)
    LinearLayout lnr_language;
    @BindView(R.id.lnr_screen_lock)
    LinearLayout lnr_screen_lock;
    @BindView(R.id.lnr_rec_phrase)
    LinearLayout lnr_rec_phrase;
    @BindView(R.id.lnr_change_pswd)
    LinearLayout lnr_change_pswd;
    @BindView(R.id.lnr_restore_wallet)
    LinearLayout lnr_restore_wallet;
    @BindView(R.id.scompat_light_mode)
    SwitchCompat scompat_light_mode;
    @BindView(R.id.scompat_privacy)
    SwitchCompat scompat_privacy;
    @BindView(R.id.scompat_hide_bal)
    SwitchCompat scompat_hide_bal;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);

        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (myApplication.getHideBalance())
            scompat_hide_bal.setChecked(true);
        else
            scompat_hide_bal.setChecked(false);

        if (myApplication.getScreenShot())
            scompat_privacy.setChecked(true);
        else
            scompat_privacy.setChecked(false);

        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lnr_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LanguageDialog();
            }
        });

        lnr_change_pswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordDialog();
            }
        });

        scompat_hide_bal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean(CONSTANTS.hideBal, true);
                    editor.apply();
                    myApplication.setHideBalance(true);
                    CommonUtilities.ShowToastMessage(AppSettingsActivity.this, getResources().getString(R.string.hide_bal_active));
                } else {
                    editor.putBoolean(CONSTANTS.hideBal, false);
                    editor.apply();
                    myApplication.setHideBalance(false);
                    CommonUtilities.ShowToastMessage(AppSettingsActivity.this, getResources().getString(R.string.hide_bal_inactive));
                }
            }
        });

        scompat_privacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean(CONSTANTS.screenshot, true);
                    editor.apply();
                    myApplication.setScreenShot(true);
                    onResume();
//                    CommonUtilities.ShowToastMessage(AppSettingsActivity.this,getResources().getString(R.string.screenshots_active));
                } else {
                    editor.putBoolean(CONSTANTS.screenshot, false);
                    editor.apply();
                    myApplication.setScreenShot(false);
                    onResume();
//                    CommonUtilities.ShowToastMessage(AppSettingsActivity.this,getResources().getString(R.string.screenshots_inactive));
                }
            }
        });

        scompat_light_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean(CONSTANTS.lightmode, true);
                    editor.apply();
//                    CommonUtilities.ShowToastMessage(AppSettingsActivity.this,getResources().getString(R.string.nightmode_active));
                } else {
                    editor.putBoolean(CONSTANTS.lightmode, false);
                    editor.apply();
//                    CommonUtilities.ShowToastMessage(AppSettingsActivity.this,getResources().getString(R.string.nightmode_inactive));
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
    }

    private void PasswordDialog() {
        ViewHolder viewHolder = new ViewHolder(R.layout.dialog_change_password);
        final DialogPlus dialog = DialogPlus.newDialog(AppSettingsActivity.this)
                .setContentHolder(viewHolder)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(true)
                .setInAnimation(R.anim.slide_in_bottom)
                .setOutAnimation(R.anim.slide_out_bottom)
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
//                        .setOnDismissListener(new OnDismissListener() {
//                            @Override
//                            public void onDismiss(DialogPlus dialog) {
//
//                            }
//                        })
//                        .setExpanded(true) // default is false, only works for grid and list
                .create();

//                Initializing Widgets
        View view = dialog.getHolderView();

        EditText edt_old_pswd = view.findViewById(R.id.edt_old_pswd);
        EditText edt_new_pswd = view.findViewById(R.id.edt_new_pswd);
        TextView txt_lower_case = view.findViewById(R.id.txt_lower_case);
        TextView txt_upper_case = view.findViewById(R.id.txt_upper_case);
        TextView txt_number = view.findViewById(R.id.txt_number);
        TextView txt_chars = view.findViewById(R.id.txt_chars);
        EditText edt_confirm_pswd = view.findViewById(R.id.edt_confirm_pswd);
        Button btn_change_pswd = view.findViewById(R.id.btn_change_pswd);


        btn_change_pswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

//                Displaying DialogPlus
        dialog.show();


    }


    private void LanguageDialog() {
        ViewHolder viewHolder = new ViewHolder(R.layout.dialog_languages);
        final DialogPlus dialog = DialogPlus.newDialog(AppSettingsActivity.this)
                .setContentHolder(viewHolder)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(true)
                .setInAnimation(R.anim.slide_in_bottom)
                .setOutAnimation(R.anim.slide_out_bottom)
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
//                        .setOnDismissListener(new OnDismissListener() {
//                            @Override
//                            public void onDismiss(DialogPlus dialog) {
//
//                            }
//                        })
//                        .setExpanded(true) // default is false, only works for grid and list
                .create();

//                Initializing Widgets
        View view = dialog.getHolderView();
        LinearLayout lnr_sys_lang = view.findViewById(R.id.lnr_sys_lang);
        LinearLayout lnr_english = view.findViewById(R.id.lnr_english);
        LinearLayout lnr_arabic = view.findViewById(R.id.lnr_arabic);
        LinearLayout lnr_chinese_simp = view.findViewById(R.id.lnr_chinese_simp);
        LinearLayout lnr_chinese_trad = view.findViewById(R.id.lnr_chinese_trad);
        LinearLayout lnr_czesh = view.findViewById(R.id.lnr_czesh);
        LinearLayout lnr_dutch = view.findViewById(R.id.lnr_dutch);

        lnr_sys_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        lnr_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        lnr_arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        lnr_chinese_simp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        lnr_chinese_trad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        lnr_czesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        lnr_dutch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


//                Displaying DialogPlus
        dialog.show();

    }

}
