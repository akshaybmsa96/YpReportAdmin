package yp.com.akki.ypreportadmin.pojo.accounts;

/**
 * Created by akshaybmsa96 on 05/03/18.
 */

public class AccountLogPojo {

    private String _id;
    private String date;
    private String fromAc;
    private String fromAcId;
    private String toAcId;
    private String toAc;
    private String amount;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFromAc() {
        return fromAc;
    }

    public void setFromAc(String fromAc) {
        this.fromAc = fromAc;
    }

    public String getFromAcId() {
        return fromAcId;
    }

    public void setFromAcId(String fromAcId) {
        this.fromAcId = fromAcId;
    }

    public String getToAcId() {
        return toAcId;
    }

    public void setToAcId(String toAcId) {
        this.toAcId = toAcId;
    }

    public String getToAc() {
        return toAc;
    }

    public void setToAc(String toAc) {
        this.toAc = toAc;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCentre() {
        return centre;
    }

    public void setCentre(String centre) {
        this.centre = centre;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [toAc = "+toAc+", amount = "+amount+", _id = "+_id+", centre = "+centre+", date = "+date+", fromAc = "+fromAc+"]";
    }

}
