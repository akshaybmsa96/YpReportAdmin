package yp.com.akki.ypreportadmin.fragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import yp.com.akki.ypreportadmin.adapter.CustomItemAdapter;
import yp.com.akki.ypreportadmin.global.PreferencedData;
import yp.com.akki.ypreportadmin.lists.NonScrollListView;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.ApiClientGetItems;
import yp.com.akki.ypreportadmin.network.ApiClientGetStores;
import yp.com.akki.ypreportadmin.network.ApiClientMaterialDistributionEntry;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.allItems.ItemsPojo;
import yp.com.akki.ypreportadmin.pojo.MaterialDistributionPojo.MaterialDistributionPojo;
import yp.com.akki.ypreportadmin.pojo.store.StorePojo;

/**
 * A simple {@link Fragment} subclass.
 */

public class MaterialDistributionEntryFragment extends Fragment implements DatePickerDialog.OnDateSetListener,AddItem.OnCompleteListener {

    private EditText editTextDatePick;
    private TextView textViewTotalAmount;
    private String formattedDate;
    private Button buttonAddItem,buttonSubmit;
    private ItemsPojo itempojo;
    private ApiClientGetItems apiClientGetItems;
    private RelativeLayout relativeLayout;
    private Spinner spinnerCentre;
    private CustomItemAdapter customItemAdapter;
    private ArrayList<MaterialDistributionPojo> items = new ArrayList<>();
    private MaterialDistributionPojo materialDistributionPojo;
    private NonScrollListView listView;
    private ApiClientMaterialDistributionEntry apiClientMaterialDistributionEntry;
    private int year,month,day;


    private ArrayList<StorePojo> storePojo;
    private ApiClientGetStores apiClientGetStores;
    private ArrayList<String> centres = new ArrayList<>();
    private String toCentre,toCentreId;


    public MaterialDistributionEntryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_material_distribution_entry, container, false);


        editTextDatePick=(EditText)view.findViewById(R.id.editTextDatePick);
        buttonAddItem=(Button)view.findViewById(R.id.buttonAddItem);
        buttonSubmit=(Button)view.findViewById(R.id.buttonSubmit);
        relativeLayout=(RelativeLayout)view.findViewById(R.id.relativeLayout);
        listView=(NonScrollListView)view.findViewById(R.id.listView);
        spinnerCentre=(Spinner)view.findViewById(R.id.spinnerCentre);
        textViewTotalAmount=(TextView)view.findViewById(R.id.textViewTotalAmount);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = sdf.format(c.getTime());
        editTextDatePick.setText(formattedDate);




        editTextDatePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dp=new DatePickerDialog(getActivity(),MaterialDistributionEntryFragment.this, year, month, day);
                dp.getDatePicker().setMaxDate(new Date().getTime());

                dp.show();



            }
        });

        relativeLayout.setVisibility(View.INVISIBLE);


        if(NetworkCheck.isNetworkAvailable(getContext())) {
            getCentres();
            getItems();
        }

        else {

            Toast.makeText(getContext(),"Network Unavailable",Toast.LENGTH_SHORT).show();
        }

        customItemAdapter=new CustomItemAdapter(getContext(),getActivity(),items,textViewTotalAmount);
        listView.setAdapter(customItemAdapter);

        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                android.support.v4.app.Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    fragmentTransaction.remove(prev);
                }
                fragmentTransaction.addToBackStack(null);

                // Create and show the dialog.
                Gson gson=new Gson();
                android.support.v4.app.DialogFragment newFragment = AddItem.newInstance(gson.toJson(itempojo),items,customItemAdapter);
                newFragment.show(fragmentTransaction, "dialog");
            }
        });

        spinnerCentre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selectedItem = adapterView.getItemAtPosition(i).toString();

                toCentre = selectedItem;

                int id = centres.indexOf(toCentre);
                toCentreId = storePojo.get(id).get_id();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 if(items.size()<1) {

                         Toast.makeText(getContext(),"No Item Added",Toast.LENGTH_SHORT).show();
                 }

                 else if(spinnerCentre.getSelectedItem().toString().equals("- Select Centre -")){

                     Toast.makeText(getContext(),"Select Centre",Toast.LENGTH_SHORT).show();
                 }

                 else
                     {

                    setPojo();
                    if(NetworkCheck.isNetworkAvailable(getContext()))
                    {


                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Distribute Material To " + spinnerCentre.getSelectedItem().toString() + " ?");
                        builder.setMessage("Are You Sure?");
                        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                System.out.println(new Gson().toJson(items));

                                if(NetworkCheck.isNetworkAvailable(getContext())) {


                                    System.out.println(new Gson().toJson(items));
                                    submit();

                                }

                                else {

                                    Toast.makeText(getContext(),"Network Unavailable",Toast.LENGTH_SHORT).show();
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
                    else
                    {
                        Toast.makeText(getContext(),"Network Unavailable",Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });



        return view;
    }

    private void submit() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientMaterialDistributionEntry = ApiClientBase.getApiClient().create(ApiClientMaterialDistributionEntry.class);

        //  String amount = String.valueOf(Double.parseDouble(editTextAmount.getText().toString())*-1.0);

        System.out.println(new Gson().toJson(items));

        String url = "materialdistribution/centreId="+toCentreId+"&amount="+totalAmount();

        Call<String> call= apiClientMaterialDistributionEntry.materialDistributionEntry(url,new Gson().toJson(items));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String i=response.body();

                //   Toast.makeText(getActivity(),i+"",Toast.LENGTH_SHORT).show();

                if(i!=null&&i.equals("1"))
                {

                    Toast.makeText(getActivity(),"Entry Success",Toast.LENGTH_SHORT).show();
                    clear();

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

    private void clear() {

        items.clear();
        customItemAdapter.notifyDataSetChanged();
        textViewTotalAmount.setText("Amount : ₹ " + totalAmount());
        spinnerCentre.setSelection(0);
    }

    private void setPojo() {

        for(int i = 0 ; i< items.size();i++)
        {
            items.get(i).setDate(editTextDatePick.getText().toString());
            items.get(i).setCentreAdminId(PreferencedData.getPrefDeliveryCentreId(getContext()));
            items.get(i).setCentre(spinnerCentre.getSelectedItem().toString());
            items.get(i).setCentreId(toCentreId);
        }

        System.out.println(new Gson().toJson(items));

    }

    private void getItems() {


        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
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

                relativeLayout.setVisibility(View.VISIBLE);
              //    Toast.makeText(getContext(),itempojo.getItems().toString()+"",Toast.LENGTH_SHORT).show();

                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ItemsPojo> call, Throwable t) {
                pDialog.hide();

                relativeLayout.setVisibility(View.GONE);

                  Toast.makeText(getContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });

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

                    relativeLayout.setVisibility(View.VISIBLE);

                    StorePojo temp = new StorePojo();
                    temp.setCentre("- Select Centre -");

                    storePojo.add(0,temp);


                    for(int i =0 ; i<storePojo.size();i++)
                    {
                        centres.add(storePojo.get(i).getCentre());
                    }

                    //  relativeLayout.setVisibility(View.VISIBLE);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, centres);


                    spinnerCentre.setAdapter(adapter);

                    toCentre = centres.get(0);
                    int id = centres.indexOf(toCentre);
                    toCentreId = storePojo.get(id).get_id();

                    //  Toast.makeText(ReportActivity.this,itempojo.getItems().toString()+"",Toast.LENGTH_SHORT).show();

                }

                else {
                    Toast.makeText(getContext(), "No Centre Added", Toast.LENGTH_SHORT).show();
                    relativeLayout.setVisibility(View.GONE);
                }

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


    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(c.getTime());
        // Toast.makeText(ReportActivity.this,formattedDate,Toast.LENGTH_SHORT).show();

        editTextDatePick.setText(formattedDate);


    }



    @Override
    public void onComplete() {

        customItemAdapter.notifyDataSetChanged();
        textViewTotalAmount.setText("Amount : ₹ " + totalAmount());

    }


    private String totalAmount()
    {
        Double total=0.0;

        for(int i=0;i<items.size();i++)
        {
            total=total+Double.parseDouble(items.get(i).getTotalItemCost());
        }

        return String.valueOf(total);
    }



}
