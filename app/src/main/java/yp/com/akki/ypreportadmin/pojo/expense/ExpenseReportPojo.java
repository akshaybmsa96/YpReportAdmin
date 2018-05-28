package yp.com.akki.ypreportadmin.pojo.expense;

/**
 * Created by akshaybmsa96 on 25/02/18.
 */

public class ExpenseReportPojo {


    private String _id;
    private String date;
    private String type;
    private String centre;
    private String name;
    private String nameId;
    private String category;
    private String modeOfPayment;
    private String amount;
    private String from;
    private String to;
    private String fromAcId;
    private String centreId;






    public String getCentreId() {
        return centreId;
    }

    public void setCentreId(String centreId) {
        this.centreId = centreId;
    }

    public String getTo ()
    {
        return to;
    }

    public void setTo (String to)
    {
        this.to = to;
    }

    public String getAmount ()
    {
        return amount;
    }

    public void setAmount (String amount)
    {
        this.amount = amount;
    }

    public String getCategory ()
    {
        return category;
    }

    public void setCategory (String category)
    {
        this.category = category;
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

    public String getModeOfPayment ()
    {
        return modeOfPayment;
    }

    public void setModeOfPayment (String modeOfPayment)
    {
        this.modeOfPayment = modeOfPayment;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getNameId() {
        return nameId;
    }

    public void setNameId(String nameId) {
        this.nameId = nameId;
    }

    public String getFrom ()
    {
        return from;
    }

    public void setFrom (String from)
    {
        this.from = from;
    }

    public String getType ()
    {
        return type;
    }

    public String getFromAcId() {
        return fromAcId;
    }

    public void setFromAcId(String fromAcId) {
        this.fromAcId = fromAcId;
    }

    public void setType (String type)
    {
        this.type = type;
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
        return "ClassPojo [to = "+to+", amount = "+amount+", category = "+category+", _id = "+_id+", centre = "+centre+", modeOfPayment = "+modeOfPayment+", name = "+name+", from = "+from+", type = "+type+", date = "+date+"]";
    }
}
