package yp.com.akki.ypreportadmin.network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import yp.com.akki.ypreportadmin.pojo.history.CentreReportpojo;


/**
 * Created by akshaybmsa96 on 21/01/18.
 */

public interface ApiClientCentreReport {
    @GET
    Call<ArrayList<CentreReportpojo>> getHistory(@Url String url);
}
