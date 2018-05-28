package yp.com.akki.ypreportadmin.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import yp.com.akki.ypreportadmin.pojo.reportDetailPojo.ReportDetailPojo;

/**
 * Created by akshaybmsa96 on 21/01/18.
 */

public interface ApiClientHistory {
    @POST("history.php")
    @FormUrlEncoded
    Call<ReportDetailPojo> getHistory(@Field("fromdate") String fromdate,
                                      @Field("todate") String todate,
                                      @Field("centre") String centre);
}
