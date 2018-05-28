package yp.com.akki.ypreportadmin.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by akshaybmsa96 on 23/03/18.
 */

public interface ApiClientReceivedPaymentEntry {
    @POST
    @FormUrlEncoded
    Call<String> receivedPaymentEntry(@Url String url, @Field("data") String data);
}
