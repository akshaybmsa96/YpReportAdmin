package yp.com.akki.ypreportadmin.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import yp.com.akki.ypreportadmin.pojo.loginpojo.LoginPojo;

/**
 * Created by akshaybmsa96 on 31/12/17.
 */

public interface ApiClientAdminLogin {
    @GET
    Call<LoginPojo> getItems(@Url String url);
}
