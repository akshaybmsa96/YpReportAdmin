package yp.com.akki.ypreportadmin.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PUT;
import retrofit2.http.Url;

/**
 * Created by akshaybmsa96 on 11/04/18.
 */

public interface ApiClientMaterialDistributionDelete {
    @PUT
    @FormUrlEncoded
    Call<String> materialDistributionDelete(@Url String url , @Field("data") String data);

}
