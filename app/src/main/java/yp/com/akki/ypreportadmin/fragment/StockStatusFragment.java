package yp.com.akki.ypreportadmin.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.adapter.CustomAdapterStockStatus;
import yp.com.akki.ypreportadmin.global.PreferencedData;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.ApiClientGetStock;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.allItems.Items;
import yp.com.akki.ypreportadmin.pojo.stock.StockPojo;

/**
 * A simple {@link Fragment} subclass.
 */
public class StockStatusFragment extends Fragment implements SearchView.OnQueryTextListener{

    private RecyclerView recyclerView;
    private ArrayList<StockPojo> stockPojo;
    private CustomAdapterStockStatus customAdapterStockStatus;
    private ApiClientGetStock apiClientGetStock;
    private RelativeLayout relativeLayout;
    private SearchView searchView;



    public StockStatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_stock_status, container, false);

        relativeLayout=(RelativeLayout) view.findViewById(R.id.relativeLayout);
        relativeLayout.setVisibility(View.GONE);
        searchView=(SearchView)view.findViewById(R.id.searchView);

        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setupSearchView();

        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);

        if(NetworkCheck.isNetworkAvailable(getContext()))
        {
            getStock();
        }

        else {

            Toast.makeText(getContext(),"Network Unavailable",Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    private void getStock()
    {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Retrieving Items...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientGetStock = ApiClientBase.getApiClient().create(ApiClientGetStock.class);
        String url="stock/centreId="+ PreferencedData.getPrefDeliveryCentreId(getActivity());
        Call<ArrayList<StockPojo>> call= apiClientGetStock.getStock(url);
        call.enqueue(new Callback<ArrayList<StockPojo>>() {
            @Override
            public void onResponse(Call<ArrayList<StockPojo>> call, Response<ArrayList<StockPojo>> response) {


                stockPojo=response.body();
                if(stockPojo!=null&&stockPojo.size()>0) {

                    relativeLayout.setVisibility(View.VISIBLE);

                 //   Toast.makeText(getContext(),new Gson().toJson(stockPojo)+"",Toast.LENGTH_SHORT).show();

                    customAdapterStockStatus=new CustomAdapterStockStatus(getContext(),stockPojo,getActivity());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(customAdapterStockStatus);

                }

                else {
                    relativeLayout.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"No Items Added Yet",Toast.LENGTH_SHORT).show();
                }

                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ArrayList<StockPojo>> call, Throwable t) {
                pDialog.dismiss();

                relativeLayout.setVisibility(View.GONE);


                Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });

    }

    private void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Item");
    }


    @Override
    public boolean onQueryTextSubmit(String newText) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        customAdapterStockStatus.filter(newText);
        // Toast.makeText(this,newText,Toast.LENGTH_SHORT).show();
        return true;
    }


}
