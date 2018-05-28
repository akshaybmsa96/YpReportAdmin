package yp.com.akki.ypreportadmin.network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import yp.com.akki.ypreportadmin.pojo.MaterialDistributionPojo.MaterialDistributionPojo;
import yp.com.akki.ypreportadmin.pojo.receivedPayment.ReceivedPaymentPojo;

/**
 * Created by akshaybmsa96 on 25/03/18.
 */

public interface ApiClientMaterialDistributionReport {

    @GET
    Call<ArrayList<MaterialDistributionPojo>> getmaterialDistributionReport(@Url String url);

}
