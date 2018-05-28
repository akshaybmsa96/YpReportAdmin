package yp.com.akki.ypreportadmin.pojo.MaterialDistributionPojo;

/**
 * Created by akshaybmsa96 on 07/02/18.
 */

public class MaterialDistributionPojo {

    private String _id;
    private String centre;
    private String centreId;
    private String centreAdminId;
    private String date;
    private String itemName;
    private String itemId;
    private String qty;
    private String costPerUnit;
    private String totalItemCost;
    private String unit;


    public void set_id(String _id) {
        this._id = _id;
    }


    public String get_id() {
        return _id;
    }

    public String getCentre() {
        return centre;
    }

    public void setCentre(String centre) {
        this.centre = centre;
    }

    public String getCentreId() {
        return centreId;
    }

    public void setCentreId(String centreId) {
        this.centreId = centreId;
    }

    public String getCentreAdminId() {
        return centreAdminId;
    }

    public void setCentreAdminId(String centreAdminId) {
        this.centreAdminId = centreAdminId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getCostPerUnit() {
        return costPerUnit;
    }

    public void setCostPerUnit(String costPerUnit) {
        this.costPerUnit = costPerUnit;
    }

    public String getTotalItemCost() {
        return totalItemCost;
    }

    public void setTotalItemCost(String totalItemCost) {
        this.totalItemCost = totalItemCost;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

}
