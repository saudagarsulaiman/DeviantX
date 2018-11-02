package com.aequalis.deviantx.UI.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aequalis.deviantx.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangellyActivity extends AppCompatActivity {



    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changelly);

        ButterKnife.bind(this);

        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
