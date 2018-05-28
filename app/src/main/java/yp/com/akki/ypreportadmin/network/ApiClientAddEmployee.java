package yp.com.akki.ypreportadmin.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by akshaybmsa96 on 09/02/18.
 */

public interface ApiClientAddEmployee {

        @POST("employee/")
        @FormUrlEncoded
        Call<String> addEmployee(@Field("data") String data);

}
