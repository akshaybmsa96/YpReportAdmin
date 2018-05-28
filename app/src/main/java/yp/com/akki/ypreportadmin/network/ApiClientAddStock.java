package yp.com.akki.ypreportadmin.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by akshaybmsa96 on 01/04/18.
 */

public interface ApiClientAddStock {

    @POST("stock/")
    @FormUrlEncoded
    Call<String> addStock(@Field("data") String data);
}
