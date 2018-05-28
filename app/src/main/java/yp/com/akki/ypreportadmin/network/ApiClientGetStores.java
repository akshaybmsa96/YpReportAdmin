package yp.com.akki.ypreportadmin.network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import yp.com.akki.ypreportadmin.pojo.store.StorePojo;

/**
 * Created by akshaybmsa96 on 15/03/18.
 */

public interface ApiClientGetStores {
    @GET
    Call<ArrayList<StorePojo>> getStores(@Url String url);
}
