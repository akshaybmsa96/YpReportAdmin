package yp.com.akki.ypreportadmin.pojo.receivedPayment;

/**
 * Created by akshaybmsa96 on 23/03/18.
 */

public class ReceivedPaymentPojo {

    private String _id;
    private String date;
    private String  amount;
    private String fromCentre;
    private String fromCentreId;
    private String centreId;
    private String modeOfPayment;
    private String from;
    private String to;
    private String toAcId;


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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFromCentre() {
        return fromCentre;
    }

    public void setFromCentre(String fromCentre) {
        this.fromCentre = fromCentre;
    }

    public String getFromCentreId() {
        return fromCentreId;
    }

    public void setFromCentreId(String fromCentreId) {
        this.fromCentreId = fromCentreId;
    }

    public String getCentreId() {
        return centreId;
    }

    public void setCentreId(String centreId) {
        this.centreId = centreId;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getToAcId() {
        return toAcId;
    }

    public void setToAcId(String toAcId) {
        this.toAcId = toAcId;
    }
}
