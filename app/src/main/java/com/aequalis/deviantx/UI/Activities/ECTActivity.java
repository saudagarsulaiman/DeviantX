package com.aequalis.deviantx.UI.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.aequalis.deviantx.R;
import com.google.android.gms.vision.text.Line;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aequalis.deviantx.Utilities.MyApplication.myApplication;

public class ECTActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.lnr_shapeshift)
    LinearLayout lnr_shapeshift;
    @BindView(R.id.lnr_changelly)
    LinearLayout lnr_changelly;

    @Override
    protected void onRestart() {
        super.onRestart();
        myApplication.disableScreenCapture(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ect);

        ButterKnife.bind(this);

        lnr_changelly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ECTActivity.this, ChangellyActivity.class);
//                startActivity(intent);
            }
        });
        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        lnr_shapeshift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ECTActivity.this, ShapeShiftActivity.class);
//                startActivity(intent);
            }
        });


    }
}
