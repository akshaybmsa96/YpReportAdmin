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
import yp.com.akki.ypreportadmin.adapter.CustomAdapterVendors;
import yp.com.akki.ypreportadmin.adapter.CustomCentreAdapter;
import yp.com.akki.ypreportadmin.global.PreferencedData;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.ApiClientGetStores;
import yp.com.akki.ypreportadmin.network.ApiClientGetVendors;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.store.StorePojo;
import yp.com.akki.ypreportadmin.pojo.vendor.VendorPojo;

/**
 * A simple {@link Fragment} subclass.
 */
public class CentreEditFragment extends Fragment {

    private RecyclerView recyclerView;
    private CustomCentreAdapter customCentreAdapter;
    private ArrayList<StorePojo> storePojo;
    private ApiClientGetStores apiClientGetStores;


    public CentreEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_centre_edit, container, false);

        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);

        if(NetworkCheck.isNetworkAvailable(getContext())) {
            getCentres();
        }

        else
        {
            Toast.makeText(getContext(),"Network Unavailable",Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    private void getCentres() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientGetStores = ApiClientBase.getApiClient().create(ApiClientGetStores.class);
        Call<ArrayList<StorePojo>> call= apiClientGetStores.getStores("store/adminId="+ PreferencedData.getPrefDeliveryCentreId(getActivity()));
        call.enqueue(new Callback<ArrayList<StorePojo>>() {
            @Override
            public void onResponse(Call<ArrayList<StorePojo>> call, Response<ArrayList<StorePojo>> response) {


                storePojo=response.body();

                //   Toast.makeText(getActivity(),vendorPojo.toString()+"",Toast.LENGTH_SHORT).show();
                if(storePojo!=null&&storePojo.size()>0) {


                    customCentreAdapter = new CustomCentreAdapter(getContext(),storePojo,getActivity());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(customCentreAdapter);


                    //  Toast.makeText(ReportActivity.this,itempojo.getItems().toString()+"",Toast.LENGTH_SHORT).show();

                }

                else
                    Toast.makeText(getContext(),"No Centre Added",Toast.LENGTH_SHORT).show();

                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ArrayList<StorePojo>> call, Throwable t) {
                pDialog.hide();


                Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });

    }

}
