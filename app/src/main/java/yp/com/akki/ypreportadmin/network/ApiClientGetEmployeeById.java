package yp.com.akki.ypreportadmin.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import yp.com.akki.ypreportadmin.pojo.employee.EmployeePojo;


/**
 * Created by akshaybmsa96 on 01/03/18.
 */

public interface ApiClientGetEmployeeById {
    @GET
    Call<EmployeePojo> getEmployeeById(@Url String url);
}
