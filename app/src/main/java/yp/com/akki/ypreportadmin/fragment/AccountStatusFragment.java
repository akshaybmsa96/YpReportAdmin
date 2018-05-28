package yp.com.akki.ypreportadmin.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.adapter.CustomAdapterAccountStatus;
import yp.com.akki.ypreportadmin.global.PreferencedData;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.ApiClientGetAccount;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.accounts.AccountPojo;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountStatusFragment extends Fragment {

    private RecyclerView recyclerView;
    private CustomAdapterAccountStatus customAdapterAccountStatus;
    private ArrayList<AccountPojo> accountPojo;
    private ApiClientGetAccount apiClientGetAccount;


    public AccountStatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_account_status, container, false);


        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);

        if(NetworkCheck.isNetworkAvailable(getContext()))
        {
            getAccounts();
        }

        else
        {
            Toast.makeText(getContext(),"Network Unavailable",Toast.LENGTH_LONG).show();
        }



        return view;
    }

    private void getAccounts() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Fetching Accounts...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientGetAccount = ApiClientBase.getApiClient().create(ApiClientGetAccount.class);

        String url = "account/centre="+ PreferencedData.getPrefDeliveryCentreId(getActivity());
        Call<ArrayList<AccountPojo>> call= apiClientGetAccount.getAccounts(url);
        call.enqueue(new Callback<ArrayList<AccountPojo>>() {
            @Override
            public void onResponse(Call<ArrayList<AccountPojo>> call, Response<ArrayList<AccountPojo>> response) {


                accountPojo=response.body();

                // Toast.makeText(getActivity(),employeePojo.toString()+"",Toast.LENGTH_SHORT).show();
                if(accountPojo!=null&&accountPojo.size()>0) {

                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    customAdapterAccountStatus = new CustomAdapterAccountStatus(getContext(),accountPojo,getActivity());
                    recyclerView.setAdapter(customAdapterAccountStatus);


                    //  Toast.makeText(ReportActivity.this,itempojo.getItems().toString()+"",Toast.LENGTH_SHORT).show();

                }

                else
                    Toast.makeText(getContext(),"No Accounts Added",Toast.LENGTH_SHORT).show();

                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ArrayList<AccountPojo>> call, Throwable t) {
                pDialog.hide();


                Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });

    }

}
