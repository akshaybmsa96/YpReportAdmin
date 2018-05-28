package yp.com.akki.ypreportadmin.pojo.attendanceRecord;

/**
 * Created by akshaybmsa96 on 28/02/18.
 */

public class EmployeeInfoPojo {

    private String employeeId;

    private String name;

    public String getEmployeeId ()
    {
        return employeeId;
    }

    public void setEmployeeId (String employeeId)
    {
        this.employeeId = employeeId;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [employeeId = "+employeeId+", name = "+name+"]";
    }
}
