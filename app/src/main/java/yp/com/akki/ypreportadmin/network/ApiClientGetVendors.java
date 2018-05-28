package yp.com.akki.ypreportadmin.network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import yp.com.akki.ypreportadmin.pojo.vendor.VendorPojo;


/**
 * Created by akshaybmsa96 on 12/02/18.
 */

public interface ApiClientGetVendors {

    @GET
    Call<ArrayList<VendorPojo>> getVendors(@Url String url);
}
