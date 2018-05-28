package yp.com.akki.ypreportadmin.pojo.stock;

import java.util.ArrayList;

/**
 * Created by akshaybmsa96 on 01/04/18.
 */

public class StockPojo {

    private String _id;
    private String itemName;
    private ArrayList<StockAssociatedItemPojo> associatedItem ;
    private String centreId;
    private String unit;
    private String lowerLimit;
    private String upperLimit;
    private String currentStatus;



    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public ArrayList<StockAssociatedItemPojo> getAssociatedItem() {
        return associatedItem;
    }

    public void setAssociatedItem(ArrayList<StockAssociatedItemPojo> associatedItem) {
        this.associatedItem = associatedItem;
    }

    public String getCentreId() {
        return centreId;
    }

    public void setCentreId(String centreId) {
        this.centreId = centreId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(String lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public String getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(String upperLimit) {
        this.upperLimit = upperLimit;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }
}
