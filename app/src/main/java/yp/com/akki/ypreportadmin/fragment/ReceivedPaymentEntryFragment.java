package yp.com.akki.ypreportadmin.fragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.Spinner;
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
import yp.com.akki.ypreportadmin.adapter.CustomCentreAdapter;
import yp.com.akki.ypreportadmin.global.PreferencedData;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.ApiClientExpenseEntry;
import yp.com.akki.ypreportadmin.network.ApiClientGetAccount;
import yp.com.akki.ypreportadmin.network.ApiClientGetStores;
import yp.com.akki.ypreportadmin.network.ApiClientReceivedPaymentEntry;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.accounts.AccountPojo;
import yp.com.akki.ypreportadmin.pojo.receivedPayment.ReceivedPaymentPojo;
import yp.com.akki.ypreportadmin.pojo.store.StorePojo;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReceivedPaymentEntryFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    private ArrayList<StorePojo> storePojo;
    private ApiClientGetStores apiClientGetStores;
    private Spinner spinnerCentre,spinnerPayment;
    private ArrayList<String> centres = new ArrayList<>();
    private String toAc,toAcId,toCentre,toCentreId;
    private EditText editTextDate,editTextAmount,editTextFrom;
    private AutoCompleteTextView autoCompleteTextViewTo;
    private Button buttonSubmit;
    private int year,month,day;
    private ArrayList<AccountPojo> accountPojo;
    private ApiClientGetAccount apiClientGetAccount;
    private ArrayList<String> accounts = new ArrayList<>();
    private String formattedDate;
    private RelativeLayout relativeLayout;
    private ReceivedPaymentPojo receivedPaymentPojo;
    private ApiClientReceivedPaymentEntry apiClientReceivedPaymentEntry;


    public ReceivedPaymentEntryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_received_payment_entry, container, false);


        spinnerCentre = (Spinner)view.findViewById(R.id.spinnerCentre);
        spinnerPayment = (Spinner)view.findViewById(R.id.spinnerPayment);
        buttonSubmit = (Button)view.findViewById(R.id.buttonSubmit);
        editTextAmount = (EditText)view.findViewById(R.id.editTextAmount);
        editTextFrom = (EditText)view.findViewById(R.id.editTextFrom);
        editTextDate = (EditText)view.findViewById(R.id.editTextDate);
        relativeLayout= (RelativeLayout)view.findViewById(R.id.layout);
        autoCompleteTextViewTo = (AutoCompleteTextView)view.findViewById(R.id.autoCompleteTextViewTo);

        relativeLayout.setVisibility(View.GONE);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = sdf.format(c.getTime());
        editTextDate.setText(formattedDate);


        if(NetworkCheck.isNetworkAvailable(getContext())) {
            getCentres();
            getAccounts();
        }

        else {

            Toast.makeText(getContext(),"Network Unavailable",Toast.LENGTH_SHORT).show();
        }



        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dp=new DatePickerDialog(getActivity(),ReceivedPaymentEntryFragment.this, year, month, day);
                dp.getDatePicker().setMaxDate(new Date().getTime());

                dp.show();

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


        autoCompleteTextViewTo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String n = autoCompleteTextViewTo.getText().toString();
                toAc = n;
                int id = accounts.indexOf(n);
                toAcId = accountPojo.get(id).get_id();

            }
        });


        autoCompleteTextViewTo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if(!b)
                {

                    String n = autoCompleteTextViewTo.getText().toString();
                    if (accounts.contains(editTextFrom.getText().toString())) {
                        toAc = n;
                        int id = accounts.indexOf(n);
                        toAcId = accountPojo.get(id).get_id();

                    }

                }


            }
        });



        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (validate()) {

                    if (NetworkCheck.isNetworkAvailable(getContext())) {
                        setPojo();
                        submit();
                    } else {

                        Toast.makeText(getContext(), "Network Unavailable", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });




        return view;
    }

    private boolean validate() {


        if(editTextAmount.getText().toString().equals(""))
        {
            editTextAmount.setError("Enter Amount");
            editTextAmount.requestFocus();

            return false;
        }

        else if(autoCompleteTextViewTo.getText().toString().equals(""))
        {

            autoCompleteTextViewTo.setError("Enter Account");
            autoCompleteTextViewTo.requestFocus();

            return false;
        }


        else if(!accounts.contains(autoCompleteTextViewTo.getText().toString()))
        {

            autoCompleteTextViewTo.setError("Invalid Account");
            autoCompleteTextViewTo.requestFocus();

            return false;
        }

        else if(editTextFrom.getText().toString().equals(""))
        {

            editTextFrom.setError("Provide Information");
            editTextFrom.requestFocus();

            return false;
        }



        return true;
    }

    private void setPojo() {

        receivedPaymentPojo = new ReceivedPaymentPojo();



        receivedPaymentPojo.setDate(editTextDate.getText().toString());
        receivedPaymentPojo.setAmount(editTextAmount.getText().toString());
        receivedPaymentPojo.setCentreId(PreferencedData.getPrefDeliveryCentreId(getActivity()));
        receivedPaymentPojo.setFromCentre(spinnerCentre.getSelectedItem().toString());
        receivedPaymentPojo.setFromCentreId(toCentreId);
        receivedPaymentPojo.setModeOfPayment(spinnerPayment.getSelectedItem().toString());
        receivedPaymentPojo.setTo(autoCompleteTextViewTo.getText().toString());
        receivedPaymentPojo.setFrom(editTextFrom.getText().toString());
        receivedPaymentPojo.setToAcId(toAcId);


    }


    private void submit() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientReceivedPaymentEntry = ApiClientBase.getApiClient().create(ApiClientReceivedPaymentEntry.class);

      //  String amount = String.valueOf(Double.parseDouble(editTextAmount.getText().toString())*-1.0);

        System.out.println(new Gson().toJson(receivedPaymentPojo));


        String url = "receviedpayment/centreId="+toCentreId+"&toAcId="+toAcId;
        Call<String> call= apiClientReceivedPaymentEntry.receivedPaymentEntry(url,new Gson().toJson(receivedPaymentPojo));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String i=response.body();

                //   Toast.makeText(getActivity(),i+"",Toast.LENGTH_SHORT).show();

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

        editTextAmount.setText("");
        editTextFrom.setText("");
        autoCompleteTextViewTo.setText("");



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


    private void getAccounts() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
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

                    for(int i =0 ; i<accountPojo.size();i++)
                    {
                        accounts.add(accountPojo.get(i).getAccountName());
                    }

                    relativeLayout.setVisibility(View.VISIBLE);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, accounts);

                    autoCompleteTextViewTo.setAdapter(adapter);
                    autoCompleteTextViewTo.setThreshold(1);


                    //  Toast.makeText(ReportActivity.this,itempojo.getItems().toString()+"",Toast.LENGTH_SHORT).show();

                }

                else {
                    Toast.makeText(getContext(), "No Accounts Added", Toast.LENGTH_SHORT).show();
                    relativeLayout.setVisibility(View.GONE);

                }

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
