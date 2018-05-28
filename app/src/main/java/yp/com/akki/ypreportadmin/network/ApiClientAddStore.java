package yp.com.akki.ypreportadmin.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by akshaybmsa96 on 15/03/18.
 */

public interface ApiClientAddStore {

    @POST("store/")
    @FormUrlEncoded
    Call<String> addStore(@Field("data") String data);

}
