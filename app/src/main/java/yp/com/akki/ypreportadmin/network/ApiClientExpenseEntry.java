package yp.com.akki.ypreportadmin.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by akshaybmsa96 on 13/02/18.
 */

public interface ApiClientExpenseEntry {

    @POST
    @FormUrlEncoded
    Call<String> expenseEntry(@Url String url, @Field("data") String data);

}
