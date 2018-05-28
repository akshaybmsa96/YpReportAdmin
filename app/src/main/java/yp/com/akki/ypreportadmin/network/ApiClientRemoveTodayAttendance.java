package yp.com.akki.ypreportadmin.network;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Url;

/**
 * Created by akshaybmsa96 on 22/02/18.
 */

public interface ApiClientRemoveTodayAttendance {
    @DELETE
    Call<String> removeTodayAttendance(@Url String url);

}
