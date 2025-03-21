package com.example.perek27;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IexApi {
    @GET("stock/market/batch")
    Call<Map<String, StockData>> getStockBatch(
            @Query("symbols") String symbols, // comma-separated list
            @Query("types") String types,     // e.g., "quote"
            @Query("token") String token      // your API key
    );
}
