package com.cryptowallet.deviantx.UI.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Activities.ECTActivity;
import com.cryptowallet.deviantx.UI.Adapters.ToolsAdapter;
import com.cryptowallet.deviantx.UI.Adapters.WalletListRAdapter;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToolsFragment extends Fragment implements DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder> {

    View view;

    @BindView(R.id.lnr_tools_ect)
    LinearLayout lnr_tools_ect;
    @BindView(R.id.txt_ect_cs)
    TextView txt_ect_cs;
    @BindView(R.id.item_picker)
    DiscreteScrollView itemPicker;

    ToolsAdapter toolsAdapter;

    ArrayList<String> title;
    ArrayList<String> slogan;
    ArrayList<Integer> icons;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.tools_fragment, container, false);

        ButterKnife.bind(this, view);
        title = new ArrayList<>();
        slogan = new ArrayList<>();
        icons = new ArrayList<>();

        title.add("Exchange");
        slogan.add("Transfer your coins from Exchanges.");
        icons.add(R.drawable.ic_tools);

        title.add("ICO Sales");
        slogan.add("Buy tokens and receive directly here.");
        icons.add(R.drawable.ic_ico);

            title.add("Shapeshift");
        slogan.add("Leading instant digital asset exchange.");
        icons.add(R.drawable.ic_shape_shift);

        title.add("Changelly");
        slogan.add("Exchange cryptocurrency at the best rate.");
        icons.add(R.drawable.ic_changelly);


        String s = getResources().getString(R.string.ect) + " (" + getResources().getString(R.string.coming_soon) + ")";
        SpannableString ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(2f), 0, 22, 0); // set size
//        ss1.setSpan(new ForegroundColorSpan(Color.RED), 0, 5, 0);// set color
        txt_ect_cs.setText(ss1);

        lnr_tools_ect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ECTActivity.class);
                startActivity(intent);
            }
        });
        toolsAdapter = new ToolsAdapter(getActivity(), title, slogan, icons);
        itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
//        itemPicker.addOnItemChangedListener(this);
        itemPicker.addOnItemChangedListener(this);
        itemPicker.setAdapter(toolsAdapter);
        itemPicker.setItemTransitionTimeMillis(150);
        itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());

//        txt_ect_cs.setText(Html.fromHtml(getString(R.string.ect_cs)));
//txt_ect_cs.setText(Html.fromHtml("<html><body><font size=22 > "+getResources().getString(R.string.ect)+" </font> <font size=10 > "+getResources().getString(R.string.coming_soon)+" </font> </body><html>"));
        return view;
    }


    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {

    }
}
