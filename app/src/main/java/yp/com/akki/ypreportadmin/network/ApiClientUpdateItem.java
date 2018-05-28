package yp.com.akki.ypreportadmin.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PUT;

/**
 * Created by akshaybmsa96 on 16/03/18.
 */

public interface ApiClientUpdateItem {
    @PUT("items/")
    @FormUrlEncoded
    Call<String> updateItem(@Field("data") String data);
}
