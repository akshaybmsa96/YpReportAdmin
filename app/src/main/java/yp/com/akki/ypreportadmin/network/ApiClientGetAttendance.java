package yp.com.akki.ypreportadmin.network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import yp.com.akki.ypreportadmin.pojo.attendanceRecord.AttendanceRecordPojo;

/**
 * Created by akshaybmsa96 on 28/02/18.
 */

public interface ApiClientGetAttendance {
    @GET
    Call<ArrayList<AttendanceRecordPojo>> getAttendance(@Url String url);
}
