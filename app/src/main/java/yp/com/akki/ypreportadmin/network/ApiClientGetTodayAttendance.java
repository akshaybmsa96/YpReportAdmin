package yp.com.akki.ypreportadmin.network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import yp.com.akki.ypreportadmin.pojo.attendanceRecord.AttendanceRecordPojo;
import yp.com.akki.ypreportadmin.pojo.employee.EmployeeAttendancePojo;

/**
 * Created by akshaybmsa96 on 05/04/18.
 */

public interface ApiClientGetTodayAttendance {
    @GET
    Call<ArrayList<EmployeeAttendancePojo>> getTodayAttendance(@Url String url);
}
