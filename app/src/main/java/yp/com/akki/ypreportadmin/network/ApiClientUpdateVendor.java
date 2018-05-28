package yp.com.akki.ypreportadmin.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PUT;

/**
 * Created by akshaybmsa96 on 21/02/18.
 */

public interface ApiClientUpdateVendor {

        @PUT("vendor/")
        @FormUrlEncoded
        Call<String> updateVendor(@Field("data") String data);

}
