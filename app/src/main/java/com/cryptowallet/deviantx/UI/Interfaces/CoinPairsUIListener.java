package com.cryptowallet.deviantx.UI.Interfaces;

import com.cryptowallet.deviantx.UI.Models.CoinPairs;

import java.util.ArrayList;

public interface CoinPairsUIListener {
    void onChangedCoinPairs(String selectedCoinName, ArrayList<CoinPairs> coinPairs);
}
