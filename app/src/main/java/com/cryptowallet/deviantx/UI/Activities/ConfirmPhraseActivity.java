package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.AuthenticationApi;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.greenfrvr.hashtagview.HashtagView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmPhraseActivity extends AppCompatActivity implements HashtagView.TagsSelectListener {

    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.hashtag_confirm_phrase)
    HashtagView hashtag_confirm_phrase;
    @BindView(R.id.txt_selected_text)
    TextView txt_selected_text;
    @BindView(R.id.btn_confirm)
    Button btn_confirm;

    ArrayList<String> selectedTags;
    ArrayList<String> cfmSelectedTags;

    private ProgressDialog progressDialog;
    private String loginResponseMsg, loginResponseStatus, loginResponseData;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_phrase);

        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        cfmSelectedTags = new ArrayList<>();
        hashtag_confirm_phrase.addOnTagSelectListener(this);

        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();
        String allTags[] = bundle.getStringArray(CONSTANTS.allTags);
        selectedTags = bundle.getStringArrayList(CONSTANTS.selectedTags);
        displayTags(allTags);


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hashtag_confirm_phrase.getSelectedItems().size() == hashtag_confirm_phrase.getSelectionLimit()) {
                    if (cfmSelectedTags.containsAll(selectedTags)) {
                        if (cfmSelectedTags.equals(selectedTags)) {
                            onLoad();
                        } else {
                            CommonUtilities.ShowToastMessage(ConfirmPhraseActivity.this, getResources().getString(R.string.select_same_words_order));
                        }
                    } else {
                        CommonUtilities.ShowToastMessage(ConfirmPhraseActivity.this, getResources().getString(R.string.select_same_words));
                    }
                } else {
                    CommonUtilities.ShowToastMessage(ConfirmPhraseActivity.this, getResources().getString(R.string.please_select) + " " + hashtag_confirm_phrase.getSelectionLimit() + " " + getResources().getString(R.string.words));
                }
            }
        });


    }

    private void onLoad() {
        try {
            String pswd = sharedPreferences.getString(CONSTANTS.pswd, "");
            String email = sharedPreferences.getString(CONSTANTS.email, "");
            JSONObject params = new JSONObject();
            try {
                params.put("email", email);
                params.put("password", pswd);
                params.put("seed", txt_selected_text.getText().toString().trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog = ProgressDialog.show(ConfirmPhraseActivity.this, "", getResources().getString(R.string.please_wait), true);
            AuthenticationApi apiService = DeviantXApiClient.getClient().create(AuthenticationApi.class);
            Call<ResponseBody> apiResponse = apiService.addSeed(params.toString());
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(responsevalue);
                            loginResponseMsg = jsonObject.getString("msg");
                            loginResponseStatus = jsonObject.getString("status");
                            if (loginResponseStatus.equals("true")) {
                                editor.putBoolean(CONSTANTS.seed, true);
                                editor.apply();
                                CommonUtilities.ShowToastMessage(ConfirmPhraseActivity.this, getResources().getString(R.string.prof_updated_success));
                                Intent intent = new Intent(ConfirmPhraseActivity.this, DashBoardActivity.class);
                                startActivity(intent);
                            } else {
                                CommonUtilities.ShowToastMessage(ConfirmPhraseActivity.this, loginResponseMsg);
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(ConfirmPhraseActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(ConfirmPhraseActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(ConfirmPhraseActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(ConfirmPhraseActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(ConfirmPhraseActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(ConfirmPhraseActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }

    }

    public void displayTags(String allTags[]) {
        List<String> DATA = new ArrayList<>();
        DATA = Arrays.asList(allTags);
        Collections.shuffle(DATA, new Random());
        hashtag_confirm_phrase.setSelectionLimit(12);
        hashtag_confirm_phrase.setData(DATA);
        hashtag_confirm_phrase.invalidate();

    }

    @Override
    public void onItemSelected(Object item, boolean isSelected) {
        if (isSelected)
            cfmSelectedTags.add(item.toString());
        else
            cfmSelectedTags.remove(item.toString());
        dispSelectedTags(cfmSelectedTags);
    }

    public void dispSelectedTags(ArrayList<String> selectedTags) {
        String selValue = null;
        for (String val : selectedTags) {
            if (selValue == null) selValue = val;
            else selValue = selValue + "  " + val;
        }
        txt_selected_text.setText(selValue);
        txt_selected_text.invalidate();
    }


}
