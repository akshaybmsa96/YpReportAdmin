package yp.com.akki.ypreportadmin.fragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.global.PreferencedData;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.ApiClientGetItems;
import yp.com.akki.ypreportadmin.network.ApiClientGetVendors;
import yp.com.akki.ypreportadmin.network.ApiClientPurchaseEntry;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.allItems.ItemsPojo;
import yp.com.akki.ypreportadmin.pojo.purchase.PurchaseEntryPojo;
import yp.com.akki.ypreportadmin.pojo.vendor.VendorPojo;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseEntryFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private EditText editTextDate,editTextQty,editTextDiscount,editTextAmount;
    private AutoCompleteTextView autoCompleteTextViewItem,autoCompleteTextViewVendor;
    private Button button;
    private int year,month,day;
    private String formattedDate;
    private ApiClientGetItems apiClientGetItems;
    private ApiClientGetVendors apiClientGetVendors;
    private ArrayList<String> item=new ArrayList<>();
    private ArrayList<String> vendors=new ArrayList<>();
    private ItemsPojo itempojo;
    private RelativeLayout relativeLayout;
    private ArrayList<VendorPojo> vendorPojo=new ArrayList<>();
    private String costPerUnit="";
    private PurchaseEntryPojo purchaseEntryPojo;
    private ApiClientPurchaseEntry apiClientPurchaseEntry;
    private String unit="",itemId;
    private String vendorId;


    public PurchaseEntryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_purchase_entry, container, false);

        button=(Button)view.findViewById(R.id.button);

        editTextDate=(EditText)view.findViewById(R.id.editTextDate);
        autoCompleteTextViewItem=(AutoCompleteTextView)view.findViewById(R.id.autoCompleteTextViewItem);
        autoCompleteTextViewVendor=(AutoCompleteTextView)view.findViewById(R.id.autoCompleteTextViewVendor);
        editTextQty=(EditText)view.findViewById(R.id.editTextQty);
        editTextDiscount=(EditText)view.findViewById(R.id.editTextDiscount);
        editTextAmount=(EditText)view.findViewById(R.id.editTextAmount);
        relativeLayout=(RelativeLayout)view.findViewById(R.id.layout);



        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = sdf.format(c.getTime());
        editTextDate.setText(formattedDate);

        if(NetworkCheck.isNetworkAvailable(getActivity()))
        {
            getVendors();
            getItems();


        }

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dp=new DatePickerDialog(getActivity(),PurchaseEntryFragment.this, year, month, day);
                dp.getDatePicker().setMaxDate(new Date().getTime());

                dp.show();

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(NetworkCheck.isNetworkAvailable(getContext()))
                if(validate())
                {
                    setPojo();
                    makeEntry();

                }


            }
        });


        editTextQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {

                if(!costPerUnit.equals("")) {
                    getAmount();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        editTextDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                if(!costPerUnit.equals("")) {
                    getAmount();
                }
                System.out.println("this");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        autoCompleteTextViewVendor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String n= autoCompleteTextViewVendor.getText().toString();
                int id = vendors.indexOf(n);

                vendorId =vendorPojo.get(id).get_id();
            }
        });


        autoCompleteTextViewVendor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if(!b)
                {
                    String n= autoCompleteTextViewVendor.getText().toString();
                    if(vendors.contains(autoCompleteTextViewVendor.getText().toString()))
                    {
                        int id = vendors.indexOf(n);

                        vendorId =vendorPojo.get(id).get_id();

                        getAmount();

                    }

                }

            }
        });



        autoCompleteTextViewItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String n= autoCompleteTextViewItem.getText().toString();
                int id = item.indexOf(n);

                Double purchasePrice = Double.parseDouble(itempojo.getItems().get(id).getPurchasePrice());
                Double tax = Double.parseDouble(itempojo.getItems().get(id).getTax());
                Double total = purchasePrice*(1+(tax/100));
                costPerUnit = String.valueOf(total);
                unit=itempojo.getItems().get(id).getUnit();
                itemId=itempojo.getItems().get(id).get_id();
                editTextQty.setHint("Quantity in "+unit);

                getAmount();

               //   Toast.makeText(getActivity(),""+itemId,Toast.LENGTH_SHORT).show();
            }
        });

        autoCompleteTextViewItem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b)
                {
                    String n= autoCompleteTextViewItem.getText().toString();
                    if(item.contains(autoCompleteTextViewItem.getText().toString()))
                    {
                        int id = item.indexOf(n);
                        Double purchasePrice = Double.parseDouble(itempojo.getItems().get(id).getPurchasePrice());
                        Double tax = Double.parseDouble(itempojo.getItems().get(id).getTax());
                        Double total = purchasePrice*(1+(tax/100));
                        costPerUnit = String.valueOf(total);
                        unit=itempojo.getItems().get(id).getUnit();
                        itemId=itempojo.getItems().get(id).get_id();
                        editTextQty.setHint("Quantity in "+unit);


                        getAmount();

                    }

                }
            }
        });




        return view;
    }

    private void makeEntry() {


        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientPurchaseEntry = ApiClientBase.getApiClient().create(ApiClientPurchaseEntry.class);

      //  System.out.println(new Gson().toJson(purchaseEntryPojo));

        String url = "purchase/id="+vendorId+"&amount="+editTextAmount.getText().toString()+"&itemId="+itemId;
        Call<String> call= apiClientPurchaseEntry.purchaseEntry(url,new Gson().toJson(purchaseEntryPojo));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String i=response.body();

                //   Toast.makeText(getActivity(),i+"",Toast.LENGTH_SHORT).show();
                System.out.println(new Gson().toJson(purchaseEntryPojo));

                if(i!=null&&i.equals("1"))
                {

                    Toast.makeText(getActivity(),"Entry Success",Toast.LENGTH_SHORT).show();
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


        autoCompleteTextViewItem.setText("");
        autoCompleteTextViewVendor.setText("");
        editTextQty.setText("");
        editTextDiscount.setText("");
        editTextAmount.setText("");

    }

    private void setPojo() {

        purchaseEntryPojo=new PurchaseEntryPojo();

        purchaseEntryPojo.setCentre(PreferencedData.getPrefDeliveryCentre(getActivity()));

//
        purchaseEntryPojo.setVendorName(autoCompleteTextViewVendor.getText().toString());
//
        purchaseEntryPojo.setDate(editTextDate.getText().toString());
        purchaseEntryPojo.setItem(autoCompleteTextViewItem.getText().toString());
        purchaseEntryPojo.setItemId(itemId);
        purchaseEntryPojo.setQty(editTextQty.getText().toString());
        purchaseEntryPojo.setUnit(unit);
        purchaseEntryPojo.setDiscount(editTextDiscount.getText().toString());
        purchaseEntryPojo.setAmount(editTextAmount.getText().toString());
        purchaseEntryPojo.setCentreId(PreferencedData.getPrefDeliveryCentreId(getActivity()));
        purchaseEntryPojo.setVendorId(vendorId);


    }

    private boolean validate() {


         if(autoCompleteTextViewVendor.getText().toString().equals(""))
        {

            autoCompleteTextViewVendor.setError("Enter Vendor");
            autoCompleteTextViewVendor.requestFocus();

            return false;
        }

        else if(!vendors.contains(autoCompleteTextViewVendor.getText().toString()))
        {
            autoCompleteTextViewVendor.setError("Invalid Vendor");
            autoCompleteTextViewVendor.requestFocus();
            return false;
        }

        else if(autoCompleteTextViewItem.getText().toString().equals(""))
        {
            autoCompleteTextViewItem.setError("Enter Item");
            autoCompleteTextViewItem.requestFocus();

            return false;
        }

        else if(!item.contains(autoCompleteTextViewItem.getText().toString()))
        {
            autoCompleteTextViewItem.setError("Invalid Item");
            autoCompleteTextViewItem.requestFocus();
            return false;
        }


        else if(editTextQty.getText().toString().equals(""))
        {
            editTextQty.setError("Enter Qty");
            editTextQty.requestFocus();

            return false;
        }



        else {
            return true;
        }

    }

    private void getAmount() {

        if(!editTextQty.getText().toString().equals("")&&!editTextDiscount.getText().toString().equals("")) {
            double totalcost = Double.parseDouble(costPerUnit) * Double.parseDouble(editTextQty.getText().toString());
            totalcost = totalcost * (1 - Double.parseDouble(editTextDiscount.getText().toString())/100);

            System.out.println(totalcost);

            editTextAmount.setText(""+totalcost);

        }

        else if(!editTextQty.getText().toString().equals(""))
        {
            double totalcost = Double.parseDouble(costPerUnit) * Double.parseDouble(editTextQty.getText().toString());
            editTextAmount.setText(""+totalcost);

            System.out.println("met 2");
        }

        else
        {
            editTextAmount.setText(costPerUnit);
        }

    }

    private void getItems() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Retrieving Items...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientGetItems = ApiClientBase.getApiClient().create(ApiClientGetItems.class);
        String url="items/centreId="+PreferencedData.getPrefDeliveryCentreId(getActivity())+"&centreAdminId="+PreferencedData.getPrefDeliveryCentreId(getActivity());
        Call<ItemsPojo> call= apiClientGetItems.getInfo(url);
        call.enqueue(new Callback<ItemsPojo>() {
            @Override
            public void onResponse(Call<ItemsPojo> call, Response<ItemsPojo> response) {


                itempojo=response.body();
                if(itempojo!=null&&itempojo.getItems().size()>0) {

                    for (int i = 0; i < itempojo.getItems().size(); i++) {
                        item.add(i, itempojo.getItems().get(i).getItemName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, item);

                    autoCompleteTextViewItem.setAdapter(adapter);
                    autoCompleteTextViewItem.setThreshold(1);



                    //  Toast.makeText(ReportActivity.this,itempojo.getItems().toString()+"",Toast.LENGTH_SHORT).show();

                }

                else {
                    relativeLayout.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"No Items Available",Toast.LENGTH_SHORT).show();
                }

                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ItemsPojo> call, Throwable t) {
                pDialog.dismiss();

                relativeLayout.setVisibility(View.GONE);


                  Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });

    }

    private void getVendors() {


        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Retrieving Vendors...");
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

                    for (int i = 0; i < vendorPojo.size(); i++) {
                        vendors.add(i, vendorPojo.get(i).getVendorName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, vendors);

                    autoCompleteTextViewVendor.setAdapter(adapter);
                    autoCompleteTextViewVendor.setThreshold(1);



                    //  Toast.makeText(ReportActivity.this,itempojo.getItems().toString()+"",Toast.LENGTH_SHORT).show();

                }

                else {
                        relativeLayout.setVisibility(View.GONE);
                        Toast.makeText(getContext(),"No Vendors Added",Toast.LENGTH_SHORT).show();
                }

                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ArrayList<VendorPojo>> call, Throwable t) {
                pDialog.dismiss();

                    relativeLayout.setVisibility(View.GONE);



                Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });




    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(c.getTime());
        // Toast.makeText(ReportActivity.this,formattedDate,Toast.LENGTH_SHORT).show();


        editTextDate.setText(formattedDate);

    }

}
