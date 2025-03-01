package com.example.perek27;

public class StockModel {

    private static StockController mStockController;
    private static StockModel mStockModel;
    public static StockModel GetInstance(){
        if(mStockModel == null){
            mStockModel = new StockModel();
        }
        return mStockModel;
    }

    private StockModel() {
        mStockController = StockController.GetInstance();
        mStockController.Init();
    }
}
