package yp.com.akki.ypreportadmin.network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import yp.com.akki.ypreportadmin.pojo.accounts.AccountLogPojo;


/**
 * Created by akshaybmsa96 on 06/03/18.
 */

public interface ApiClientGetAccountLog {
    @GET
    Call<ArrayList<AccountLogPojo>> getAccountLog(@Url String url);
}
