package yp.com.akki.ypreportadmin.network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import yp.com.akki.ypreportadmin.pojo.stock.StockPojo;

/**
 * Created by akshaybmsa96 on 01/04/18.
 */

public interface ApiClientGetStock {
    @GET
    Call<ArrayList<StockPojo>> getStock(@Url String url);
}
