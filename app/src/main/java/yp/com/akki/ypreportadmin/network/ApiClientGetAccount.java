package yp.com.akki.ypreportadmin.network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import yp.com.akki.ypreportadmin.pojo.accounts.AccountPojo;

/**
 * Created by akshaybmsa96 on 04/03/18.
 */

public interface ApiClientGetAccount {

    @GET
    Call<ArrayList<AccountPojo>> getAccounts(@Url String url);
}
