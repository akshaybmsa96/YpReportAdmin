package yp.com.akki.ypreportadmin.fragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pchmn.materialchips.ChipsInput;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.adapter.CustomAdapterItem;
import yp.com.akki.ypreportadmin.global.PreferencedData;
import yp.com.akki.ypreportadmin.network.ApiClientAddStock;
import yp.com.akki.ypreportadmin.network.ApiClientAddStore;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.ApiClientGetItems;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.allItems.Items;
import yp.com.akki.ypreportadmin.pojo.allItems.ItemsPojo;
import yp.com.akki.ypreportadmin.pojo.stock.StockAssociatedItemPojo;
import yp.com.akki.ypreportadmin.pojo.stock.StockPojo;
import yp.com.akki.ypreportadmin.pojo.store.StorePojo;

/**
 * A simple {@link Fragment} subclass.
 */
public class StockAddFragment extends Fragment implements StockAddAssociatedItemFragment.OnCompleteListener {

    private AutoCompleteTextView autoCompleteTextViewItemName;
    private EditText editTextUnit,editTextLowerLimit,editTextCurrentStatus,editTextUpperLimit,editTextAssociatedItem;
    private ItemsPojo itempojo;
    private ApiClientGetItems apiClientGetItems;
    private ArrayList<String> item=new ArrayList<>();
    private String unit="",itemId="";
    private Button buttonAddItem;
    private StockPojo stockPojo;
    private ArrayList<StockAssociatedItemPojo> stockAssociatedItemPojo = new ArrayList<>();
    private ApiClientAddStock apiClientAddStock;


    public StockAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stock_add, container, false);

        autoCompleteTextViewItemName=(AutoCompleteTextView)view.findViewById(R.id.autoCompleteTextViewItemName);
        editTextUnit=(EditText)view.findViewById(R.id.editTextUnit);
        editTextLowerLimit=(EditText)view.findViewById(R.id.editTextLowerLimit);
        editTextUpperLimit=(EditText)view.findViewById(R.id.editTextUpperLimit);
        editTextCurrentStatus=(EditText)view.findViewById(R.id.editTextCurrentStatus);
        editTextAssociatedItem = (EditText) view.findViewById(R.id.editTextAssociatedItem);
        buttonAddItem=(Button)view.findViewById(R.id.buttonAddItem);

        if(NetworkCheck.isNetworkAvailable(getContext()))
        {
            getItems();
        }
        else {
            Toast.makeText(getContext(),"Network Unavailable",Toast.LENGTH_SHORT).show();
        }

        editTextAssociatedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(itempojo!=null&&itempojo.getItems().size()>0) {

                    android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    android.support.v4.app.Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
                    if (prev != null) {
                        fragmentTransaction.remove(prev);
                    }
                    fragmentTransaction.addToBackStack(null);

                    // Create and show the dialog.
                    Gson gson = new Gson();
                    android.support.v4.app.DialogFragment newFragment = StockAddAssociatedItemFragment.newInstance(gson.toJson(itempojo), stockAssociatedItemPojo);
                    newFragment.show(fragmentTransaction, "addAssocaiatedItem");
                }

                else {

                    Toast.makeText(getContext(),"Items not Found....Relaod Tab",Toast.LENGTH_SHORT).show();
                }

            }
        });


        /*

        autoCompleteTextViewItemName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String n= autoCompleteTextViewItemName.getText().toString();
                int id = item.indexOf(n);

                unit=itempojo.getItems().get(id).getUnit();
                itemId=itempojo.getItems().get(id).get_id();
                editTextUnit.setText(unit);

                //  Toast.makeText(getActivity(),""+id,Toast.LENGTH_SHORT).show();
            }
        });

        autoCompleteTextViewItemName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b)
                {
                    String n= autoCompleteTextViewItemName.getText().toString();
                    if(item.contains(autoCompleteTextViewItemName.getText().toString()))
                    {
                        int id = item.indexOf(n);

                        unit=itempojo.getItems().get(id).getUnit();
                        itemId=itempojo.getItems().get(id).get_id();
                        editTextUnit.setText(unit);


                    }

                }
            }
        });

        */

        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate())
                {
                    setpojo();


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Add " + autoCompleteTextViewItemName.getText().toString() + " ?");
                    builder.setMessage("Are You Sure?");
                    builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            if(NetworkCheck.isNetworkAvailable(getContext())) {

                                addToStock();

                            }

                            else {

                                Toast.makeText(getActivity(), "Network Unavailable", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }

            }
        });




        return view;
    }

    private void addToStock() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it

        System.out.println(new Gson().toJson(stockPojo));

        apiClientAddStock = ApiClientBase.getApiClient().create(ApiClientAddStock.class);
        Call<String> call= apiClientAddStock.addStock(new Gson().toJson(stockPojo));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String i=response.body();

                //   Toast.makeText(getActivity(),i+"",Toast.LENGTH_SHORT).show();

                if(i!=null&&i.equals("1"))
                {

                    Toast.makeText(getActivity(),"Item Added To Stock",Toast.LENGTH_SHORT).show();
                    clearFields();


                }

                else {
                    Toast.makeText(getActivity(),"Cannot Add",Toast.LENGTH_SHORT).show();
                }
                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pDialog.hide();


                Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });

    }

    private void clearFields() {

        autoCompleteTextViewItemName.setText("");
        editTextAssociatedItem.setText("");
        editTextUnit.setText("");
        editTextLowerLimit.setText("");
        editTextUpperLimit.setText("");
        editTextCurrentStatus.setText("");

        if(stockAssociatedItemPojo!=null&&stockAssociatedItemPojo.size()>0) {
            stockAssociatedItemPojo.clear();
        }

        autoCompleteTextViewItemName.requestFocus();


    }

    private void setpojo() {

        stockPojo = new StockPojo();

        stockPojo.setItemName(autoCompleteTextViewItemName.getText().toString());

        stockPojo.setAssociatedItem(stockAssociatedItemPojo);

        stockPojo.setLowerLimit(editTextLowerLimit.getText().toString());
        stockPojo.setUpperLimit(editTextUpperLimit.getText().toString());
        stockPojo.setUnit(editTextUnit.getText().toString());
        stockPojo.setCurrentStatus(editTextCurrentStatus.getText().toString());
        stockPojo.setCentreId(PreferencedData.getPrefDeliveryCentreId(getContext()));
    }

    private boolean validate() {

        if(autoCompleteTextViewItemName.getText().toString().equals("")){

            autoCompleteTextViewItemName.setError("Enter Item");
            autoCompleteTextViewItemName.requestFocus();
            return false;
        }

        else if(editTextAssociatedItem.getText().toString().equals(""))
        {
            editTextAssociatedItem.setError("Select Item");
            editTextAssociatedItem.requestFocus();
            return false;

        }



        else if(editTextLowerLimit.getText().toString().equals(""))
        {
            editTextLowerLimit.setError("Enter Limit");
            editTextLowerLimit.requestFocus();
            return false;
        }

        else if(editTextUpperLimit.getText().toString().equals(""))
        {
            editTextUpperLimit.setError("Enter Limit");
            editTextUpperLimit.requestFocus();
            return false;
        }

        else if(editTextCurrentStatus.getText().toString().equals(""))
        {
            editTextCurrentStatus.setError("Enter Current Status");
            editTextCurrentStatus.requestFocus();
            return false;
        }


        return true;

    }


    private void getItems() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Retrieving Items...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientGetItems = ApiClientBase.getApiClient().create(ApiClientGetItems.class);
        String url="items/centreId="+ PreferencedData.getPrefDeliveryCentreId(getActivity())+"&centreAdminId="+PreferencedData.getPrefDeliveryCentreId(getActivity());
        Call<ItemsPojo> call= apiClientGetItems.getInfo(url);
        call.enqueue(new Callback<ItemsPojo>() {
            @Override
            public void onResponse(Call<ItemsPojo> call, Response<ItemsPojo> response) {


                itempojo=response.body();
                if(itempojo!=null&&itempojo.getItems().size()>0) {

                    for (int i = 0; i < itempojo.getItems().size(); i++) {
                           item.add(itempojo.getItems().get(i).getItemName());

                    }

                    /*


                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, item);

                    autoCompleteTextViewItemName.setAdapter(adapter);
                    autoCompleteTextViewItemName.setThreshold(1);

                    */

                  //    Toast.makeText(getContext(),new Gson().toJson(item),Toast.LENGTH_SHORT).show();


                }

                else {
                    Toast.makeText(getContext(),"No Items Available",Toast.LENGTH_SHORT).show();
                }

                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ItemsPojo> call, Throwable t) {
                pDialog.dismiss();


                Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });

    }

    @Override
    public void onComplete() {

        setAssociatedItems();

    }

    private void setAssociatedItems() {

        editTextAssociatedItem.setText("");

        for(int i =0 ;i<stockAssociatedItemPojo.size();i++) {

            editTextAssociatedItem.append(stockAssociatedItemPojo.get(i).getItemName() + " ");

        }

        if(stockAssociatedItemPojo.size()>0)
        editTextUnit.setText(stockAssociatedItemPojo.get(0).getItemUnit());

        else {
            editTextUnit.setText("");
        }
    }

}
