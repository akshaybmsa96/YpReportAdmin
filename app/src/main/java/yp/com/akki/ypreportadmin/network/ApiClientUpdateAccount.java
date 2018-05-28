package yp.com.akki.ypreportadmin.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by akshaybmsa96 on 05/03/18.
 */

public interface ApiClientUpdateAccount {

    @POST("accountlog/")
    @FormUrlEncoded
    Call<String> updateAccount(@Field("data") String data);

}
