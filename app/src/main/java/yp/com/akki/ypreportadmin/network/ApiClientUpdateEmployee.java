package yp.com.akki.ypreportadmin.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PUT;

/**
 * Created by akshaybmsa96 on 21/02/18.
 */

public interface ApiClientUpdateEmployee {
    @PUT("employee/")
    @FormUrlEncoded
    Call<String> updateEmployee(@Field("data") String data);
}
