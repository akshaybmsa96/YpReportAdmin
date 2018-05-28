package yp.com.akki.ypreportadmin.pojo.attendanceRecord;

/**
 * Created by akshaybmsa96 on 28/02/18.
 */

public class AttendanceDetailPojo {

    private String employeeId;

    private String _id;

    private String centre;

    private String name;

    private String attendance;

    private String date;

    private String centreId;




    public String getCentreId() {
        return centreId;
    }

    public void setCentreId(String centreId) {
        this.centreId = centreId;
    }

    public String getEmployeeId ()
    {
        return employeeId;
    }

    public void setEmployeeId (String employeeId)
    {
        this.employeeId = employeeId;
    }

    public String get_id ()
    {
        return _id;
    }

    public void set_id (String _id)
    {
        this._id = _id;
    }

    public String getCentre ()
    {
        return centre;
    }

    public void setCentre (String centre)
    {
        this.centre = centre;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getAttendance ()
    {
        return attendance;
    }

    public void setAttendance (String attendance)
    {
        this.attendance = attendance;
    }

    public String getDate ()
    {
        return date;
    }

    public void setDate (String date)
    {
        this.date = date;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [employeeId = "+employeeId+", _id = "+_id+", centre = "+centre+", name = "+name+", attendance = "+attendance+", date = "+date+"]";
    }
}
