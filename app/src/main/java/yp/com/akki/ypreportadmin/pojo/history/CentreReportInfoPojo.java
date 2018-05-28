package yp.com.akki.ypreportadmin.pojo.history;

/**
 * Created by akshaybmsa96 on 16/03/18.
 */

public class CentreReportInfoPojo {
    private String centreId;

    private String centre;

    public String getCentreId ()
    {
        return centreId;
    }

    public void setCentreId (String centreId)
    {
        this.centreId = centreId;
    }

    public String getCentre ()
    {
        return centre;
    }

    public void setCentre (String centre)
    {
        this.centre = centre;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [centreId = "+centreId+", centre = "+centre+"]";
    }
}
