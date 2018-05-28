package yp.com.akki.ypreportadmin.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import yp.com.akki.ypreportadmin.pojo.reportPojo.ReportPojo;

/**
 * Created by akshaybmsa96 on 22/01/18.
 */

public interface ApiClientGetSale {
    @POST("getSale.php")
    @FormUrlEncoded
    Call<ReportPojo> getSale(@Field("fromdate") String fromdate,
                             @Field("todate") String todate);
}
