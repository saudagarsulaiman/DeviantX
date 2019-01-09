package com.cryptowallet.deviantx.UI.Fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Activities.SetPinActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExchangeSettingsFragment extends Fragment {


    @BindView(R.id.lnr_def_wallet)
    LinearLayout lnr_def_wallet;
    @BindView(R.id.scompat_asswal)
    SwitchCompat scompat_asswal;
    @BindView(R.id.scompat_dev_fees)
    SwitchCompat scompat_dev_fees;
    @BindView(R.id.lnr_pin)
    LinearLayout lnr_pin;
    @BindView(R.id.scompat_pin)
    SwitchCompat scompat_pin;
    @BindView(R.id.scompat_fingerprint)
    SwitchCompat scompat_fingerprint;
    @BindView(R.id.lnr_fingerprint)
    LinearLayout lnr_fingerprint;
    @BindView(R.id.lnr_frequency)
    LinearLayout lnr_frequency;
    @BindView(R.id.lnr_color_pref)
    LinearLayout lnr_color_pref;
//    @BindView(R.id.)
//    ;

    View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.exchange_settings_fragment, container, false);
        ButterKnife.bind(this, view);


/*
        if (myApplication.get()) {
            scompat_.setChecked(true);
            scompat_.setBackground(getResources().getDrawable(R.drawable.rec_white_white_c16));
            scompat_.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        } else {
            scompat_.setBackground(getResources().getDrawable(R.drawable.rec_white_trans_c16));
            scompat_.setChecked(false);
            scompat_.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
        }
*/

        if (scompat_asswal.isChecked()) {
//            scompat_.setChecked(true);
            scompat_asswal.setBackground(getResources().getDrawable(R.drawable.rec_white_white_c16));
            scompat_asswal.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        } else {
            scompat_asswal.setBackground(getResources().getDrawable(R.drawable.rec_white_trans_c16));
            scompat_asswal.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
        }

        if (scompat_dev_fees.isChecked()) {
            //            scompat_.setChecked(true);
            scompat_dev_fees.setBackground(getResources().getDrawable(R.drawable.rec_white_white_c16));
            scompat_dev_fees.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        } else {
//            scompat_.setChecked(false);
            scompat_dev_fees.setBackground(getResources().getDrawable(R.drawable.rec_white_trans_c16));
            scompat_dev_fees.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
        }

        if (scompat_pin.isChecked()) {
            //            scompat_.setChecked(true);
            scompat_pin.setBackground(getResources().getDrawable(R.drawable.rec_white_white_c16));
            scompat_pin.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        } else {
            //            scompat_.setChecked(false);
            scompat_pin.setBackground(getResources().getDrawable(R.drawable.rec_white_trans_c16));
            scompat_pin.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
        }

        if (scompat_fingerprint.isChecked()) {
            //            scompat_.setChecked(true);
            scompat_fingerprint.setBackground(getResources().getDrawable(R.drawable.rec_white_white_c16));
            scompat_fingerprint.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        } else {
            scompat_fingerprint.setBackground(getResources().getDrawable(R.drawable.rec_white_trans_c16));
            //            scompat_.setChecked(false);
            scompat_fingerprint.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
        }

        scompat_asswal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    scompat_asswal.setBackground(getResources().getDrawable(R.drawable.rec_white_white_c16));
                    scompat_asswal.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                } else {
                    scompat_asswal.setBackground(getResources().getDrawable(R.drawable.rec_white_trans_c16));
                    scompat_asswal.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
                }
            }
        });

        scompat_dev_fees.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    scompat_dev_fees.setBackground(getResources().getDrawable(R.drawable.rec_white_white_c16));
                    scompat_dev_fees.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                } else {
                    scompat_dev_fees.setBackground(getResources().getDrawable(R.drawable.rec_white_trans_c16));
                    scompat_dev_fees.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
                }
            }
        });

        lnr_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetPinActivity.class);
                startActivity(intent);
            }
        });
        scompat_pin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intent = new Intent(getActivity(), SetPinActivity.class);
                startActivity(intent);
                if (isChecked) {
                    scompat_pin.setBackground(getResources().getDrawable(R.drawable.rec_white_white_c16));
                    scompat_pin.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                } else {
                    scompat_pin.setBackground(getResources().getDrawable(R.drawable.rec_white_trans_c16));
                    scompat_pin.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
                }
            }
        });

        scompat_fingerprint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    scompat_fingerprint.setBackground(getResources().getDrawable(R.drawable.rec_white_white_c16));
                    scompat_fingerprint.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                } else {
                    scompat_fingerprint.setBackground(getResources().getDrawable(R.drawable.rec_white_trans_c16));
                    scompat_fingerprint.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
                }
            }
        });


        return view;
    }


}
