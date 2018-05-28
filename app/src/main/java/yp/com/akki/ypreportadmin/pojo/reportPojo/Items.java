package yp.com.akki.ypreportadmin.pojo.reportPojo;

/**
 * Created by akshaybmsa96 on 22/01/18.
 */

public class Items {
    private String material_cost;

    private String centre;

    private String orders;

    private String sale;

    public String getMaterial_cost ()
    {
        return material_cost;
    }

    public void setMaterial_cost (String material_cost)
    {
        this.material_cost = material_cost;
    }

    public String getCentre ()
    {
        return centre;
    }

    public void setCentre (String centre)
    {
        this.centre = centre;
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
        return "ClassPojo [material_cost = "+material_cost+", centre = "+centre+", orders = "+orders+", sale = "+sale+"]";
    }
}
