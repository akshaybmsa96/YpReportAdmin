package yp.com.akki.ypreportadmin.pojo.todayRecord;

import java.util.ArrayList;

/**
 * Created by akshaybmsa96 on 24/01/18.
 */

public class TodayRecordPojo {

    private String id;
    private String orders;
    private String sale;
    private ArrayList<TodayItems> itemUsage;



    public ArrayList<TodayItems> getItems() {
        return itemUsage;
    }

    public void setItems(ArrayList<TodayItems> itemUsage) {
        this.itemUsage = itemUsage;
    }

    public String getOrders ()
    {
        return orders;
    }

    public void setOrders (String orders)
    {
        this.orders = orders;
    }

    public String getSale ()
    {
        return sale;
    }

    public void setSale (String sale)
    {
        this.sale = sale;
    }


    @Override
    public String toString()
    {
        return "ClassPojo [ items = "+itemUsage+", orders = "+orders+", sale = "+sale+"]";
    }
}


