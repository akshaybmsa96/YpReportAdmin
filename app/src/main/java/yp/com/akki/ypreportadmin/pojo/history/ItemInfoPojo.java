package yp.com.akki.ypreportadmin.pojo.history;

/**
 * Created by akshaybmsa96 on 23/02/18.
 */

public class ItemInfoPojo {
    private String unit;

    private String itemName;

    public String getUnit ()
    {
        return unit;
    }

    public void setUnit (String unit)
    {
        this.unit = unit;
    }

    public String getItemName ()
    {
        return itemName;
    }

    public void setItemName (String itemName)
    {
        this.itemName = itemName;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [unit = "+unit+", itemName = "+itemName+"]";
    }
}
