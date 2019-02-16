package com.cryptowallet.deviantx.UI.Interfaces;

import com.cryptowallet.deviantx.UI.Models.ExcOrders;

import java.util.ArrayList;

public interface CoinPairSelectableListener {
    public void PairSelected(ArrayList<ExcOrders> excOrdersList, int pos);
}
