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
import yp.com.akki.ypreportadmin.global.PreferencedData;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.ApiClientGetVendors;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.vendor.VendorPojo;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendorEditFragment extends Fragment {

    private ApiClientGetVendors apiClientGetVendors;
    private ArrayList<VendorPojo> vendorPojo=new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomAdapterVendors customAdapterVendors;


    public VendorEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view =  inflater.inflate(R.layout.fragment_vendor_edit, container, false);

        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);

        if(NetworkCheck.isNetworkAvailable(getContext()))
        {
            getVendors();
        }

        else
        {
            Toast.makeText(getContext(),"Netork Unavailable",Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void getVendors() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientGetVendors = ApiClientBase.getApiClient().create(ApiClientGetVendors.class);
        Call<ArrayList<VendorPojo>> call= apiClientGetVendors.getVendors("vendor/centre="+ PreferencedData.getPrefDeliveryCentreId(getActivity()));
        call.enqueue(new Callback<ArrayList<VendorPojo>>() {
            @Override
            public void onResponse(Call<ArrayList<VendorPojo>> call, Response<ArrayList<VendorPojo>> response) {


                vendorPojo=response.body();

                //   Toast.makeText(getActivity(),vendorPojo.toString()+"",Toast.LENGTH_SHORT).show();
                if(vendorPojo!=null&&vendorPojo.size()>0) {


                    customAdapterVendors = new CustomAdapterVendors(getContext(),vendorPojo,getActivity());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(customAdapterVendors);


                    //  Toast.makeText(ReportActivity.this,itempojo.getItems().toString()+"",Toast.LENGTH_SHORT).show();

                }

                else
                    Toast.makeText(getContext(),"No Vendor Added",Toast.LENGTH_SHORT).show();

                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ArrayList<VendorPojo>> call, Throwable t) {
                pDialog.hide();


                Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });

    }

}
