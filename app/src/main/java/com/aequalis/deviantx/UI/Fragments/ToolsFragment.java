package com.aequalis.deviantx.UI.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aequalis.deviantx.R;
import com.aequalis.deviantx.UI.Activities.ECTActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToolsFragment extends Fragment {

    View view;

    @BindView(R.id.lnr_tools_ect)
    LinearLayout lnr_tools_ect;
    @BindView(R.id.txt_ect_cs)
    TextView txt_ect_cs;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.tools_fragment, container, false);

        ButterKnife.bind(this, view);

        String s= getResources().getString(R.string.ect)+" ("+getResources().getString(R.string.coming_soon)+")";
        SpannableString ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(2f), 0,22, 0); // set size
//        ss1.setSpan(new ForegroundColorSpan(Color.RED), 0, 5, 0);// set color
        txt_ect_cs.setText(ss1);

        lnr_tools_ect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ECTActivity.class);
                startActivity(intent);
            }
        });

//        txt_ect_cs.setText(Html.fromHtml(getString(R.string.ect_cs)));
//txt_ect_cs.setText(Html.fromHtml("<html><body><font size=22 > "+getResources().getString(R.string.ect)+" </font> <font size=10 > "+getResources().getString(R.string.coming_soon)+" </font> </body><html>"));
        return view;
    }


}
