package com.cryptowallet.deviantx.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Adapters.ExchangeDashboardSlideAdapter;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExchangeDashboardFragment extends Fragment implements DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder> {


    View view;
    ExchangeDashboardSlideAdapter dashboardSlideAdapter;

    @BindView(R.id.item_picker)
    DiscreteScrollView itemPicker;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.exchange_dashboard_fragment, container, false);
        ButterKnife.bind(this, view);


        dashboardSlideAdapter = new ExchangeDashboardSlideAdapter(getActivity());
        itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
//        itemPicker.addOnItemChangedListener(this);
        itemPicker.addOnItemChangedListener(this);
        itemPicker.setAdapter(dashboardSlideAdapter);
        itemPicker.setItemTransitionTimeMillis(150);
        itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());
        itemPicker.scrollToPosition(1);

        return view;
    }


    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {

    }

}
