package com.cryptowallet.deviantx.UI.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.UserControllerApi;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.greenfrvr.hashtagview.HashtagView;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPhraseActivity extends AppCompatActivity implements HashtagView.TagsSelectListener {

    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar;
    @BindView(R.id.hashtag_add_phrase)
    HashtagView hashtag_add_phrase;
    @BindView(R.id.btn_next)
    Button btn_next;


    String allTags[] = null;
    ArrayList<String> selectedTags;

    private ProgressDialog progressDialog;
    private String loginResponseMsg, loginResponseStatus, loginResponseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phrase);

        ButterKnife.bind(this);
        selectedTags = new ArrayList<>();

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        hashtag_add_phrase.addOnTagSelectListener(this);

        getSeed();


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allTags != null) {
                    if (selectedTags.size() == hashtag_add_phrase.getSelectionLimit()) {
                        //  List<String> DATA = hashtags.getSelectedItems();
                        //  ArrayList<String> arrayList = new ArrayList<String>(DATA);
                        Intent intent = new Intent(AddPhraseActivity.this, ConfirmPhraseActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putStringArray(CONSTANTS.allTags, allTags);
                        bundle.putStringArrayList(CONSTANTS.selectedTags, selectedTags);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        CommonUtilities.ShowToastMessage(AddPhraseActivity.this, getResources().getString(R.string.please_select) + " " + hashtag_add_phrase.getSelectionLimit() + " " + getResources().getString(R.string.words));
                    }
                }
            }
        });


    }

    private void getSeed() {
        try {
            progressDialog = ProgressDialog.show(AddPhraseActivity.this, "", getResources().getString(R.string.please_wait), true);
            UserControllerApi apiService = DeviantXApiClient.getClient().create(UserControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getSeed();
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
                                loginResponseData = jsonObject.getString("data");
                                displayTags(loginResponseData);
                            } else {
                                CommonUtilities.ShowToastMessage(AddPhraseActivity.this, loginResponseMsg);
                            }
                        } else {
                            progressDialog.dismiss();
                            CommonUtilities.ShowToastMessage(AddPhraseActivity.this, loginResponseMsg);
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AddPhraseActivity.this, getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AddPhraseActivity.this, getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AddPhraseActivity.this, getResources().getString(R.string.networkerror));
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AddPhraseActivity.this, getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(AddPhraseActivity.this, getResources().getString(R.string.errortxt));
        }
    }

    public void displayTags(String values) {
        allTags = values.split(" ");
        List<String> DATA = Arrays.asList(allTags);
        hashtag_add_phrase.setData(DATA);
        hashtag_add_phrase.setRowCount(5);
        hashtag_add_phrase.setSelectionLimit(12);
        hashtag_add_phrase.invalidate();

    }

    @Override
    public void onItemSelected(Object item, boolean selected) {
        if (selected)
            selectedTags.add(item.toString());
        else
            selectedTags.remove(item.toString());
    }
}
