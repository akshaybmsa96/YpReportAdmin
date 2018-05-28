package yp.com.akki.ypreportadmin.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.adapter.CustomAdapterVendorStatus;
import yp.com.akki.ypreportadmin.global.PreferencedData;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.ApiClientGetVendors;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.vendor.VendorPojo;


public class VendorStatusFragment extends Fragment implements SearchView.OnQueryTextListener {

    private RecyclerView recyclerView;
    private CustomAdapterVendorStatus customAdapterVendorStatus;
    private ArrayList<VendorPojo> vendorPojo;
    private ApiClientGetVendors apiClientGetVendors;
    private SearchView searchView;
    private TextView textViewAmount;
    private LinearLayout layout;


    public VendorStatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vendor_status, container, false);

        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        searchView=(SearchView)view.findViewById(R.id.searchView);
        textViewAmount=(TextView)view.findViewById(R.id.textViewAmount);
        layout=(LinearLayout)view.findViewById(R.id.layout);
        layout.setVisibility(View.GONE);
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        setupSearchView();

        if(NetworkCheck.isNetworkAvailable(getContext())) {
            getVendors();
        }

        else {

            Toast.makeText(getContext(),"Network Unavailable",Toast.LENGTH_SHORT).show();
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

                    layout.setVisibility(View.VISIBLE);
                    customAdapterVendorStatus = new CustomAdapterVendorStatus(getContext(),vendorPojo,getActivity(),textViewAmount);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(customAdapterVendorStatus);

                    setTotal();



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

    private void setTotal() {

        Double total = 0.0;

        for (int i =0; i<vendorPojo.size();i++)
        {
            total=total+Double.parseDouble(vendorPojo.get(i).getCurrentBalance());
        }

        textViewAmount.setText("₹" + Math.ceil(total));
    }

    private void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search");
    }


    @Override
    public boolean onQueryTextSubmit(String newText) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        customAdapterVendorStatus.filter(newText);
        // Toast.makeText(this,newText,Toast.LENGTH_SHORT).show();
        return true;
    }



}
