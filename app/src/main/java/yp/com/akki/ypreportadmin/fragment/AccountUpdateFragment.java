package yp.com.akki.ypreportadmin.fragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import yp.com.akki.ypreportadmin.global.PreferencedData;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.ApiClientGetAccount;
import yp.com.akki.ypreportadmin.network.ApiClientUpdateAccount;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.accounts.AccountLogPojo;
import yp.com.akki.ypreportadmin.pojo.accounts.AccountPojo;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountUpdateFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private EditText editTextDate,editTextBalance;
    private AutoCompleteTextView editTextFrom;
    private Spinner spinnerToAc;
    private Button buttonUpdateAcount;
    private RelativeLayout relativeLayout;
    private int year,month,day,fromAcPos;
    private String formattedDate,toAc,fromAcId="x",toAcId;
    private ArrayList<AccountPojo> accountPojo;
    private ApiClientGetAccount apiClientGetAccount;
    private ArrayList<String> accounts = new ArrayList<>();
    private AccountLogPojo accountLogPojo;
    private ApiClientUpdateAccount apiClientUpdateAccount;


    public AccountUpdateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_update, container, false);

        editTextDate=(EditText)view.findViewById(R.id.editTextDate);
        editTextFrom=(AutoCompleteTextView) view.findViewById(R.id.editTextFrom);
        editTextBalance=(EditText) view.findViewById(R.id.editTextBalance);
        spinnerToAc=(Spinner) view.findViewById(R.id.spinnerToAc);
        relativeLayout=(RelativeLayout)view.findViewById(R.id.layout);

        relativeLayout.setVisibility(View.GONE);

        buttonUpdateAcount = (Button)view.findViewById(R.id.buttonUpdateAcount);


        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = sdf.format(c.getTime());
        editTextDate.setText(formattedDate);


        if(NetworkCheck.isNetworkAvailable(getContext()))
        {
            getAccounts();
        }

        else
        {
            Toast.makeText(getContext(),"Network Unavailable",Toast.LENGTH_LONG).show();
        }


        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dp=new DatePickerDialog(getActivity(),AccountUpdateFragment.this, year, month, day);
                dp.getDatePicker().setMaxDate(new Date().getTime());

                dp.show();

            }
        });

        buttonUpdateAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate())
                {

                    if(NetworkCheck.isNetworkAvailable(getContext()))
                    {
                        setPojo();
                        updateAccount();
                    }

                    else {
                        Toast.makeText(getContext(),"Network Unavailable",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });


        editTextFrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                    String n = editTextFrom.getText().toString();
                     int id = accounts.indexOf(n);
                     fromAcPos=id;
                      fromAcId = accountPojo.get(id).get_id();

            }
        });


        editTextFrom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if(!b)
                {
                        String n = editTextFrom.getText().toString();
                        if (accounts.contains(editTextFrom.getText().toString())) {
                            int id = accounts.indexOf(n);
                            fromAcPos=id;
                            fromAcId = accountPojo.get(id).get_id();

                        }


                    else {
                        fromAcId="x";
                    }

                }

            }
        });


        spinnerToAc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selectedItem = adapterView.getItemAtPosition(i).toString();

                toAc = selectedItem;

                int id = accounts.indexOf(toAc);
                toAcId = accountPojo.get(id).get_id();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        return view;
    }

    private void updateAccount() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientUpdateAccount = ApiClientBase.getApiClient().create(ApiClientUpdateAccount.class);

        System.out.println(new Gson().toJson(accountLogPojo));

        Call<String> call= apiClientUpdateAccount.updateAccount(new Gson().toJson(accountLogPojo));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {


                String i =response.body();

                // Toast.makeText(getActivity(),employeePojo.toString()+"",Toast.LENGTH_SHORT).show();
                if(i!=null&&i.equals("1"))
                {
                    Toast.makeText(getContext(),"Account Updated",Toast.LENGTH_SHORT).show();
                    resetFields();
                }

                else
                    Toast.makeText(getContext(),"Cannot Update",Toast.LENGTH_SHORT).show();

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

    private void resetFields() {

        editTextFrom.setText("");
        editTextBalance.setText("");
        editTextFrom.requestFocus();
    }

    private void setPojo() {
        accountLogPojo=new AccountLogPojo();

        accountLogPojo.setCentre(PreferencedData.getPrefDeliveryCentre(getContext()));
        accountLogPojo.setAmount(editTextBalance.getText().toString());
        accountLogPojo.setDate(editTextDate.getText().toString());
        accountLogPojo.setFromAc(editTextFrom.getText().toString());
        accountLogPojo.setToAc(toAc);
        accountLogPojo.setFromAcId(fromAcId);
        accountLogPojo.setToAcId(toAcId);
        accountLogPojo.setCentreId(PreferencedData.getPrefDeliveryCentreId(getActivity()));
    }

    private boolean validate() {

        editTextFrom.setError(null);
        editTextBalance.setError(null);

        if(editTextFrom.getText().toString().equals(""))
        {
            editTextFrom.setError("Enter account");
            editTextFrom.requestFocus();
            return false;
        }

        /*

        else if(!accounts.contains(spinnerToAc.getSelectedItem().toString()))  {

            editTextFrom.setError("Invalid account");
            editTextFrom.requestFocus();
            return false;

        }
        */


        else if(editTextBalance.getText().toString().equals(""))
        {
            editTextBalance.setError("Enter Amount");
            editTextBalance.requestFocus();
            return false;
        }

        else if(!fromAcId.equals("x"))
        {
            if(Double.parseDouble(accountPojo.get(fromAcPos).getCurrentBalance()) < Double.parseDouble(editTextBalance.getText().toString()))
            {
                editTextFrom.setError("Insufficient Balance ( ₹ " + accountPojo.get(fromAcPos).getCurrentBalance() + " )" );
                editTextFrom.requestFocus();
                return false;
            }
        }






        return true;
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

                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, accounts);

                    spinnerToAc.setAdapter(adapter);
                    editTextFrom.setAdapter(adapter2);
                    editTextFrom.setThreshold(1);

                    toAc = accounts.get(0);
                    int id = accounts.indexOf(toAc);
                    toAcId = accountPojo.get(id).get_id();

                    //  Toast.makeText(ReportActivity.this,itempojo.getItems().toString()+"",Toast.LENGTH_SHORT).show();

                }

                else {
                    Toast.makeText(getContext(), "No Accounts Added", Toast.LENGTH_SHORT).show();

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

}
