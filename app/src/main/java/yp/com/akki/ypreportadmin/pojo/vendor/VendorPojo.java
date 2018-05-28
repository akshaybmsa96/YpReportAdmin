package yp.com.akki.ypreportadmin.pojo.vendor;

/**
 * Created by akshaybmsa96 on 09/02/18.
 */

public class VendorPojo {


    private  String _id;

    private String vendorName;

    private String address;

    private String creditLimit;

    private String gstNumber;

    private String phoneNumber;

    private String centre;

    private String currentBalance;

    private String centreId;




    public String getCentreId() {
        return centreId;
    }

    public void setCentreId(String centreId) {
        this.centreId = centreId;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }



    public String getVendorName ()
    {
        return vendorName;
    }

    public void setVendorName (String vendorName)
    {
        this.vendorName = vendorName;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getCreditLimit()
    {
        return creditLimit;
    }

    public void setCreditLimit(String creditLimit)
    {
        this.creditLimit = creditLimit;
    }

    public String getGstNumber ()
    {
        return gstNumber;
    }

    public void setGstNumber (String gstNumber)
    {
        this.gstNumber = gstNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    @Override
    public String toString()
    {
        return "ClassPojo [vendorName = "+vendorName+", address = "+address+", creditLimit = "+ creditLimit +", gstNumber = "+gstNumber+"]";
    }
}
