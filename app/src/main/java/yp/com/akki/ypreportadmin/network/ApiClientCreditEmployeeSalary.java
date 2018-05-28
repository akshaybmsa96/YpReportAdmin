package yp.com.akki.ypreportadmin.network;

import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Url;

/**
 * Created by akshaybmsa96 on 01/03/18.
 */

public interface ApiClientCreditEmployeeSalary {

    @PUT
    Call<String> creditSalary(@Url String url);

}
