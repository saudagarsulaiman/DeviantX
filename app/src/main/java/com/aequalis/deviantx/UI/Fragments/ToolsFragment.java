package com.aequalis.deviantx.UI.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
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

        lnr_tools_ect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ECTActivity.class);
                startActivity(intent);
            }
        });

        txt_ect_cs.setText(Html.fromHtml(getString(R.string.ect_cs)));

        return view;
    }


}
