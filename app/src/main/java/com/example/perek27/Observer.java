package com.example.perek27;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface Observer {

    public void SignInWithEmailAndPasswordCompleate(@NonNull Task<AuthResult> task);

    public void GetAllStocksInvested(List<Stock> stockInvested);

    public void GetTransactionHistory(ArrayList<Transaction> transactionsList);

    public void GetAllStocksInMarket(List<Stock> stocksList);

    public void getALLCash(float cash);

    public void GetAllUserData(UserData userData);

    public void OnStockInfoUpdate(StockInfo stockInf);
}
