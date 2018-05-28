package yp.com.akki.ypreportadmin.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by akshaybmsa96 on 30/12/17.
 */

public class PreferencedData {

    static final String PREF_DELIVERY_CENTRE = "Delivery Centre Name";
    static final String PREF_DELIVERY_CENTRE_ID = "Delivery Centre ID";
    static final String PREF_LOGGED_IN_STATUS = "Login Status";
    static final String PREF_DELIVERY_CENTRE_CODE = "Centre Code";

    public static SharedPreferences getSharedPreferences(Context ctx)
    {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setLoggedIn(Context ctx, Boolean status)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_LOGGED_IN_STATUS, status);
        editor.commit();
    }

    public static Boolean getLoggedIn(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean(PREF_LOGGED_IN_STATUS,false);
    }

    public static void setPrefDeliveryCentre(Context ctx, String name)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_DELIVERY_CENTRE, name);
        editor.commit();
    }

    public static String getPrefDeliveryCentre(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_DELIVERY_CENTRE,"");
    }

    public static void setPrefDeliveryCentreId(Context ctx, String name)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_DELIVERY_CENTRE_ID, name);
        editor.commit();
    }

    public static String getPrefDeliveryCentreId(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_DELIVERY_CENTRE_ID,"");
    }


    public static void setPrefDeliveryCentreCode(Context ctx, String code)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_DELIVERY_CENTRE_CODE, code);
        editor.commit();
    }

    public static String getPrefDeliveryCentreCode(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_DELIVERY_CENTRE_CODE,"");
    }



    public static void clearPref(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_DELIVERY_CENTRE);
        editor.remove(PREF_LOGGED_IN_STATUS);
        editor.remove(PREF_DELIVERY_CENTRE_ID);
        editor.remove(PREF_DELIVERY_CENTRE_CODE);

        editor.commit();
    }

}
