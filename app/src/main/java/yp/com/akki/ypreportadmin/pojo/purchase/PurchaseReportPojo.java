package yp.com.akki.ypreportadmin.pojo.purchase;

/**
 * Created by akshaybmsa96 on 18/02/18.
 */

public class PurchaseReportPojo {

    private String amount;
    private String vendorName;
    private String vendorId;
    private String _id;
    private String centre;
    private String item;
    private String itemId;
    private String qty;
    private String unit;
    private String date;
    private String discount;
    private String centreId;




    public String getCentreId() {
        return centreId;
    }

    public void setCentreId(String centreId) {
        this.centreId = centreId;
    }

    public String getAmount ()
    {
        return amount;
    }

    public void setAmount (String amount)
    {
        this.amount = amount;
    }

    public String getVendorName ()
    {
        return vendorName;
    }

    public void setVendorName (String vendorName)
    {
        this.vendorName = vendorName;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
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

    public String getItem ()
    {
        return item;
    }

    public void setItem (String item)
    {
        this.item = item;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getQty ()
    {
        return qty;
    }

    public void setQty (String qty)
    {
        this.qty = qty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDate ()
    {
        return date;
    }

    public void setDate (String date)
    {
        this.date = date;
    }

    public String getDiscount ()
    {
        return discount;
    }

    public void setDiscount (String discount)
    {
        this.discount = discount;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [amount = "+amount+", vendorName = "+vendorName+", _id = "+_id+", centre = "+centre+", item = "+item+", qty = "+qty+", date = "+date+", discount = "+discount+"]";
    }
}
