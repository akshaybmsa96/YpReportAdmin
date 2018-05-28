package yp.com.akki.ypreportadmin.network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import yp.com.akki.ypreportadmin.pojo.expense.ExpenseReportPojo;

/**
 * Created by akshaybmsa96 on 25/02/18.
 */

public interface ApiClientExpenseReport {
    @GET
    Call<ArrayList<ExpenseReportPojo>> getExpenseReport(@Url String url);
}
