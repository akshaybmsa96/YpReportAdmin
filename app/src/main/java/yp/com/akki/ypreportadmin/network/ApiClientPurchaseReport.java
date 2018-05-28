package yp.com.akki.ypreportadmin.network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import yp.com.akki.ypreportadmin.pojo.purchase.PurchaseReportPojo;


/**
 * Created by akshaybmsa96 on 18/02/18.
 */

public interface ApiClientPurchaseReport {

    @GET
    Call<ArrayList<PurchaseReportPojo>> getPurchaseReport(@Url String url);
}
