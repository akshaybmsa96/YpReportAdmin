package yp.com.akki.ypreportadmin.pojo.loginpojo;

/**
 * Created by akshaybmsa96 on 31/12/17.
 */

public class LoginPojo {
    private String error;

    private LoginDetailPojo items;

    public String getError ()
    {
        return error;
    }

    public void setError (String error)
    {
        this.error = error;
    }

    public LoginDetailPojo getItems() {
        return items;
    }

    public void setItems(LoginDetailPojo items) {
        this.items = items;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [error = "+error+", items = "+items+"]";
    }
}

