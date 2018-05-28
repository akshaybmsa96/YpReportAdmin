package yp.com.akki.ypreportadmin.pojo.attendanceRecord;

/**
 * Created by akshaybmsa96 on 28/02/18.
 */

public class AttendanceRecordPojo {

    private String attendanceTotal;

    private EmployeeInfoPojo _id;

    public String getAttendanceTotal ()
    {
        return attendanceTotal;
    }

    public void setAttendanceTotal (String attendanceTotal)
    {
        this.attendanceTotal = attendanceTotal;
    }

    public EmployeeInfoPojo get_id ()
    {
        return _id;
    }

    public void set_id (EmployeeInfoPojo _id)
    {
        this._id = _id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [attendanceTotal = "+attendanceTotal+", _id = "+_id+"]";
    }
}
