package yp.com.akki.ypreportadmin.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PUT;

/**
 * Created by akshaybmsa96 on 02/04/18.
 */

public interface ApiClientUpdateStock {
    @PUT("stock/")
    @FormUrlEncoded
    Call<String> updateStock(@Field("data") String data);
}
