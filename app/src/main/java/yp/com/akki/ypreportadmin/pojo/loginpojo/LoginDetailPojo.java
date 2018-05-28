package yp.com.akki.ypreportadmin.pojo.loginpojo;

/**
 * Created by akshaybmsa96 on 31/12/17.
 */

public class LoginDetailPojo {

    private String centre;
    private String _id;
    private String adminId;
    private String centreCode;

    public String getCentre()
    {
        return centre;
    }

    public void setCentre(String centre)
    {
        this.centre = centre;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getCentreCode() {
        return centreCode;
    }

    public void setCentreCode(String centreCode) {
        this.centreCode = centreCode;
    }

    @Override
    public String toString()
    {
        return "centre = "+ centre + " adminId = " + adminId+"";
    }
}
