package yp.com.akki.ypreportadmin.pojo.employee;

/**
 * Created by akshaybmsa96 on 09/02/18.
 */

public class EmployeePojo {

    private String leavesPerMonth;

    private String _id;

    private String aadhaarNumber;

    private String phoneNumber;

    private String address;

    private String dateOfJoining;

    private String designation;

    private String salary;

    private String employeeName;

    private String centre;

    private String currentBalance;

    private String centreId;




    public String getCentreId() {
        return centreId;
    }

    public void setCentreId(String centreId) {
        this.centreId = centreId;
    }

    public String getLeavesPerMonth ()
    {
        return leavesPerMonth;
    }

    public void setLeavesPerMonth (String leavesPerMonth)
    {
        this.leavesPerMonth = leavesPerMonth;
    }

    public String getId ()
    {
        return _id;
    }

    public void setId (String _id)
    {
        this._id = _id;
    }

    public String getAadhaarNumber ()
    {
        return aadhaarNumber;
    }

    public void setAadhaarNumber (String aadhaarNumber)
    {
        this.aadhaarNumber = aadhaarNumber;
    }

    public String getPhoneNumber ()
    {
        return phoneNumber;
    }

    public void setPhoneNumber (String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getDateOfJoining ()
    {
        return dateOfJoining;
    }

    public void setDateOfJoining (String dateOfJoining)
    {
        this.dateOfJoining = dateOfJoining;
    }

    public String getDesignation ()
    {
        return designation;
    }

    public void setDesignation (String designation)
    {
        this.designation = designation;
    }

    public String getSalary ()
    {
        return salary;
    }

    public void setSalary (String salary)
    {
        this.salary = salary;
    }

    public String getEmployeeName ()
    {
        return employeeName;
    }

    public void setEmployeeName (String employeeName)
    {
        this.employeeName = employeeName;
    }

    public String getCentre() {
        return centre;
    }

    public void setCentre(String centre) {
        this.centre = centre;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [leavesPerMonth = "+leavesPerMonth+", id = "+_id+", aadhaarNumber = "+aadhaarNumber+", phoneNumber = "+phoneNumber+", address = "+address+", dateOfJoining = "+dateOfJoining+", designation = "+designation+", salary = "+salary+", employeeName = "+employeeName+"]";
    }
}
