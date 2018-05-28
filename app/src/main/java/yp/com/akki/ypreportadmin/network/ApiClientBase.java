package yp.com.akki.ypreportadmin.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by akshaybmsa96 on 29/10/17.
 */

public class ApiClientBase {

   // public static String url="http://139.59.5.188:8080/";
   //  public static String url="http://10.0.2.2:80/yp/adminReport/";

    //node url
  //  public static String url="http://10.0.2.2:3000/api/";
   //   public static String url="http://192.168.0.101:3000/api/";



    //ip yp office

   // public static String url="http://192.168.1.8:3000/api/";


    //aws ip
    public static String url ="http://52.66.5.226:3000/api/";


    //php url
  //  public static String url="http://ypoutlet.esy.es/yp/api/adminReport/";


    public static Retrofit retrofit = null;


    public static Retrofit getApiClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url).
                    addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }
}
