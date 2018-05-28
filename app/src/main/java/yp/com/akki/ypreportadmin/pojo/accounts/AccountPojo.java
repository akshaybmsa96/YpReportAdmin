package yp.com.akki.ypreportadmin.pojo.accounts;

/**
 * Created by akshaybmsa96 on 03/03/18.
 */

public class AccountPojo {

    private String _id;
    private String accountName;
    private String type;
    private String currentBalance;
    private String centre;
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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String cuurentBalance) {
        this.currentBalance = cuurentBalance;
    }

    public String getCentre() {
        return centre;
    }

    public void setCentre(String centre) {
        this.centre = centre;
    }
}
