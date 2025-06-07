package com.example.perek27;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.ArrayList;
import java.util.List;

public interface Observer {

    public void SignInWithEmailAndPasswordCompleate(@NonNull Task<AuthResult> task);

    public void GetAllStocksInvested(List<StockInfo> stockInvested);

    public void GetTransactionHistory(ArrayList<Transaction> transactionsList);

    public void GetAllStocksInMarket(List<StockInfo> stocksList);

    public void GetAllCash(float cash);

    public void GetAllUserData(UserData userData);

    public void OnStockInfoUpdate(StockInfo stockInf);

    public void OnBuyStockCompleted(Boolean success, String reason);

    public void OnSellStockCompleted(Boolean seccess, String reason);

    public void OnUpdateCashCompleted(Boolean success);
}
