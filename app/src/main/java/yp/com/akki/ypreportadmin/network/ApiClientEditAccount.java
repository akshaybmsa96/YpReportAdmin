package yp.com.akki.ypreportadmin.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PUT;

/**
 * Created by akshaybmsa96 on 04/03/18.
 */

public interface ApiClientEditAccount {

    @PUT("account/")
    @FormUrlEncoded
    Call<String> editAccount(@Field("data") String data);

}
