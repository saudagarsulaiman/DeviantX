package com.cryptowallet.deviantx.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.cryptowallet.deviantx.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class SelectADCampaignsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.spnr_ad_type)
    Spinner spnr_ad_type;
    @BindView(R.id.btn_continue)
    Button btn_continue;
    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;

    String selected_item = "";

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
//        CommonUtilities.serviceStart(WithdrawFundsAirdropActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_ad_campaigns);
        ButterKnife.bind(this);

        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        spnr_ad_type.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("Social");
//        categories.add("");
//        categories.add("");
//        categories.add("");
//        categories.add("");
//        categories.add("");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_ad_type.setAdapter(dataAdapter);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected_item.equals("Social")) {

                    Intent intent = new Intent(SelectADCampaignsActivity.this, CreateADCampaignsActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("SelectType", selected_item);
//                    intent.putExtras(bundle);
                    intent.putExtra("SelectType", selected_item);
                    startActivity(intent);

                }/* else {

                }*/
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        selected_item = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
