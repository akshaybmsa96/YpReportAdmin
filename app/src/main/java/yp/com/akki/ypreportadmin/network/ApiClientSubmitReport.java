package yp.com.akki.ypreportadmin.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by akshaybmsa96 on 20/01/18.
 */

public interface ApiClientSubmitReport {
    @POST("report/")
    @FormUrlEncoded
    Call<String> submitReport(@Field("data") String data);
}
