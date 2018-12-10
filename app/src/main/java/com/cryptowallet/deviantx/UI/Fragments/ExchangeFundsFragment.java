package com.cryptowallet.deviantx.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cryptowallet.deviantx.R;

import butterknife.ButterKnife;

public class ExchangeFundsFragment extends Fragment {


    View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.exchange_funds_fragment, container, false);
        ButterKnife.bind(this, view);


        return view;
    }


}
