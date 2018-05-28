package yp.com.akki.ypreportadmin.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by akshaybmsa96 on 16/03/18.
 */

public interface ApiClientAddItem {

    @POST("items/")
    @FormUrlEncoded
    Call<String> addItem(@Field("data") String data);

}
