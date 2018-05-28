package yp.com.akki.ypreportadmin.pojo.reportPojo;

import java.util.ArrayList;

/**
 * Created by akshaybmsa96 on 22/01/18.
 */

public class ReportPojo {
    private String error;

    private ArrayList<Items> items;

    public String getError ()
    {
        return error;
    }

    public void setError (String error)
    {
        this.error = error;
    }


    public ArrayList<Items> getItems() {
        return items;
    }

    public void setItems(ArrayList<Items> items) {
        this.items = items;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [error = "+error+", items = "+items+"]";
    }
}
