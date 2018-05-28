package yp.com.akki.ypreportadmin.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by akshaybmsa96 on 09/02/18.
 */

public interface ApiClientAddVendor {

    @POST("vendor/")
    @FormUrlEncoded
    Call<String> addVendor(@Field("data") String data);

}
