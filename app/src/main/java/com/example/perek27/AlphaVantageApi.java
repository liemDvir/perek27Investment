package com.example.perek27;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AlphaVantageApi {
    @GET("query")
    Call<StockResponse> getStockData(@Query("function") String function,
                                     @Query("symbol") String symbol,
                                     @Query("apikey") String apiKey);


}
