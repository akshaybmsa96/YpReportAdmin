package yp.com.akki.ypreportadmin.pojo.reportDetailPojo;

import java.util.ArrayList;

/**
 * Created by akshaybmsa96 on 22/01/18.
 */

public class ReportDetailPojo {
    private String error;
    private ArrayList<ReportDetailItems> items;


    public String getError ()
    {
        return error;
    }

    public void setError (String error)
    {
        this.error = error;
    }

    public ArrayList<ReportDetailItems> getItems() {
        return items;
    }

    public void setItems(ArrayList<ReportDetailItems> items) {
        this.items = items;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [error = "+error+", items = "+items+"]";
    }
}


