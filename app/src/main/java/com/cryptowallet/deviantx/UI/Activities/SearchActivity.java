package com.cryptowallet.deviantx.UI.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cryptowallet.deviantx.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.img_center_back)
    ImageView img_center_back;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.toolbar_back_search)
    Toolbar toolbar_back_search;
    @BindView(R.id.lnr_bg)
    LinearLayout lnr_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        img_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
