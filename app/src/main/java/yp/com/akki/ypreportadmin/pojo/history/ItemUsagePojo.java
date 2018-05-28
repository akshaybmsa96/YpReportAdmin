package yp.com.akki.ypreportadmin.pojo.history;

/**
 * Created by akshaybmsa96 on 21/01/18.
 */

public class ItemUsagePojo {

    private ItemInfoPojo _id;

    private String totalItemCost;

    private String qty;

    public ItemInfoPojo get_id() {
        return _id;
    }

    public void set_id(ItemInfoPojo _id) {
        this._id = _id;
    }

    public String getTotalItemCost ()
    {
        return totalItemCost;
    }

    public void setTotalItemCost (String totalItemCost)
    {
        this.totalItemCost = totalItemCost;
    }

    public String getQty ()
    {
        return qty;
    }

    public void setQty (String qty)
    {
        this.qty = qty;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [_id = "+_id+", totalItemCost = "+totalItemCost+", qty = "+qty+"]";
    }
    }
