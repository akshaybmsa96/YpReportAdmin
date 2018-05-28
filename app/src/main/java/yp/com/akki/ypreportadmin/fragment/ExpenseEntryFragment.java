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
import yp.com.akki.ypreportadmin.network.ApiClientExpenseEntry;
import yp.com.akki.ypreportadmin.network.ApiClientGetAccount;
import yp.com.akki.ypreportadmin.network.ApiClientGetEmployee;
import yp.com.akki.ypreportadmin.network.ApiClientGetVendors;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.accounts.AccountPojo;
import yp.com.akki.ypreportadmin.pojo.employee.EmployeePojo;
import yp.com.akki.ypreportadmin.pojo.expense.ExpenseEntryPojo;
import yp.com.akki.ypreportadmin.pojo.vendor.VendorPojo;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseEntryFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private EditText editTextDate,editTextAmount, editTextTo;
    private AutoCompleteTextView autoCompleteTextViewList, autoCompleteTextViewFrom;
    private Spinner spinnerType,spinnerCategory,spinnerPayment;
    private Button buttonSubmit;
    private RelativeLayout relativeLayout;
    private ExpenseEntryPojo expenseEntryPojo;
    private ArrayList<String> item=new ArrayList<>();
    private ArrayList<String> vendors=new ArrayList<>();
    private ArrayList<String> employees=new ArrayList<>();
    private ApiClientGetVendors apiClientGetVendors;
    private ArrayList<VendorPojo> vendorPojo=new ArrayList<>();
    private ArrayList<EmployeePojo> employeePojo=new ArrayList<>();
    private ApiClientGetEmployee apiClientGetEmployee;
    private ApiClientExpenseEntry apiClientExpenseEntry;
    private int year,month,day,fromAcPos;
    private String type= "Vendor";
    private String formattedDate,fromAcId;
    private String id="";
    private ArrayList<AccountPojo> accountPojo;
    private ApiClientGetAccount apiClientGetAccount;
    private ArrayList<String> accounts = new ArrayList<>();


    public ExpenseEntryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense_entry, container, false);

        editTextDate = (EditText)view.findViewById(R.id.editTextDate);
        editTextAmount = (EditText)view.findViewById(R.id.editTextAmount);
        autoCompleteTextViewFrom = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextViewFrom);
        editTextTo = (EditText) view.findViewById(R.id.editTextTo);

        relativeLayout=(RelativeLayout)view.findViewById(R.id.layout);

        autoCompleteTextViewList = (AutoCompleteTextView)view.findViewById(R.id.autoCompleteTextViewList);

        spinnerType = (Spinner)view.findViewById(R.id.spinnerType);
        spinnerCategory = (Spinner)view.findViewById(R.id.spinnerCategory);
        spinnerPayment = (Spinner)view.findViewById(R.id.spinnerPayment);

        buttonSubmit = (Button)view.findViewById(R.id.buttonSubmit);

        if(NetworkCheck.isNetworkAvailable(getContext())) {
            getVendors();
            getEmployees();
            getAccounts();
        }

        else
        {
            Toast.makeText(getContext(),"Network Unavailable",Toast.LENGTH_LONG).show();
        }

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = sdf.format(c.getTime());
        editTextDate.setText(formattedDate);


        autoCompleteTextViewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(type.equals("Vendor")) {
                    String n = autoCompleteTextViewList.getText().toString();
                    int ide = vendors.indexOf(n);
                    id = vendorPojo.get(ide).get_id();
                }

                else if(type.equals("Employee"))
                {
                    String n = autoCompleteTextViewList.getText().toString();
                    int ide = employees.indexOf(n);
                    id = employeePojo.get(ide).get_id();
                }

                else
                {
                    id="x";
                }

            }
        });


        autoCompleteTextViewList.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if(!b)
                {
                    if(type.equals("Vendor")) {
                        String n = autoCompleteTextViewList.getText().toString();
                        if (vendors.contains(autoCompleteTextViewList.getText().toString())) {
                            int ide = vendors.indexOf(n);
                            id = vendorPojo.get(ide).get_id();

                        }

                    }

                    else if(type.equals("Employee")) {
                        String n = autoCompleteTextViewList.getText().toString();
                        if (employees.contains(autoCompleteTextViewList.getText().toString())) {
                            int ide = employees.indexOf(n);
                            id = employeePojo.get(ide).get_id();

                        }
                  }

                    else {
                        id="x";
                    }

                }

            }
        });


        autoCompleteTextViewFrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                String n = autoCompleteTextViewFrom.getText().toString();
                int id = accounts.indexOf(n);
                fromAcPos=id;
                fromAcId = accountPojo.get(id).get_id();


            }
        });


        autoCompleteTextViewFrom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if(!b)
                {
                    String n = autoCompleteTextViewFrom.getText().toString();
                    if (accounts.contains(autoCompleteTextViewFrom.getText().toString())) {
                        int id = accounts.indexOf(n);
                        fromAcPos=id;
                        fromAcId = accountPojo.get(id).get_id();

                    }

                }

            }
        });


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(NetworkCheck.isNetworkAvailable(getContext())) {
                    if (validate()) {
                        setPojo();
                        submit();
                    }
                }

            }
        });


        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dp=new DatePickerDialog(getActivity(),ExpenseEntryFragment.this, year, month, day);
                dp.getDatePicker().setMaxDate(new Date().getTime());

                dp.show();

            }
        });


        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selectedItem = adapterView.getItemAtPosition(i).toString();

                if(selectedItem.equals("Vendor"))
                {
                        setVendor();
                         type = "Vendor";
                }

                else if(selectedItem.equals("Employee"))
                {
                    setEmployee();
                    type = "Employee";
                }

                else if(selectedItem.equals("Others"))
                {
                    setNone();
                    type = "Others";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        return view;
    }

    private void setPojo() {

        expenseEntryPojo=new ExpenseEntryPojo();


        expenseEntryPojo.setDate(editTextDate.getText().toString());
        expenseEntryPojo.setType(spinnerType.getSelectedItem().toString());
        expenseEntryPojo.setName(autoCompleteTextViewList.getText().toString());
        expenseEntryPojo.setNameId(id);
        expenseEntryPojo.setCategory(spinnerCategory.getSelectedItem().toString());
        expenseEntryPojo.setAmount(editTextAmount.getText().toString());
        expenseEntryPojo.setModeOfPayment(spinnerPayment.getSelectedItem().toString());
        expenseEntryPojo.setFrom(autoCompleteTextViewFrom.getText().toString());
        expenseEntryPojo.setTo(editTextTo.getText().toString());
        expenseEntryPojo.setCentre(PreferencedData.getPrefDeliveryCentre(getContext()));
        expenseEntryPojo.setCentreId(PreferencedData.getPrefDeliveryCentreId(getActivity()));
        expenseEntryPojo.setFromAcId(fromAcId);


    }

    private boolean validate() {

        autoCompleteTextViewList.setError(null);
        editTextAmount.setError(null);
        autoCompleteTextViewFrom.setError(null);
        editTextTo.setError(null);


        if(autoCompleteTextViewList.getText().toString().equals(""))
        {
            autoCompleteTextViewList.setError("Enter Name");
            autoCompleteTextViewList.requestFocus();
            return false;
        }

        else if(type.equals("Employee")&&!employees.contains(autoCompleteTextViewList.getText().toString())) {

             autoCompleteTextViewList.setError("Invalid Employee");
             autoCompleteTextViewList.requestFocus();
             return false;


        }

         else if(type.equals("Vendor")&&!vendors.contains(autoCompleteTextViewList.getText().toString())) {

                autoCompleteTextViewList.setError("Invalid Vendor");
                autoCompleteTextViewList.requestFocus();
                return false;


        }

        else if(editTextAmount.getText().toString().length()<1)
        {
            editTextAmount.setError("Enter Amount");
            editTextAmount.requestFocus();
            return false;
        }

        else if(autoCompleteTextViewFrom.getText().toString().equals(""))
        {
            autoCompleteTextViewFrom.setError("SelectAccount");
            autoCompleteTextViewFrom.requestFocus();
            return false;
        }

        else if(!accounts.contains(autoCompleteTextViewFrom.getText().toString()))
        {
            autoCompleteTextViewFrom.setError("Invalid Account");
            autoCompleteTextViewFrom.requestFocus();
            return false;
        }

        /*
        else if(Double.parseDouble(accountPojo.get(fromAcPos).getCurrentBalance()) < Double.parseDouble(editTextAmount.getText().toString()))
        {
            autoCompleteTextViewFrom.setError("Insufficient Balance ( ₹ " + accountPojo.get(fromAcPos).getCurrentBalance() + " )" );
            autoCompleteTextViewFrom.requestFocus();
            return false;
        }
        */


        else if(editTextTo.getText().toString().equals("")) {

            editTextTo.setError("Provide Detail");
            editTextTo.requestFocus();
            return false;
        }


        else {
            return true;
        }
    }

    private void submit() {


        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientExpenseEntry = ApiClientBase.getApiClient().create(ApiClientExpenseEntry.class);

        String amount = String.valueOf(Double.parseDouble(editTextAmount.getText().toString())*-1.0);

        if(spinnerCategory.getSelectedItem().toString().equals("Incentive"))
        {
            type="Others";
        }

        String url = "expense/type="+type+"&id="+id+"&amount="+amount;
        Call<String> call= apiClientExpenseEntry.expenseEntry(url,new Gson().toJson(expenseEntryPojo));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String i=response.body();

                //   Toast.makeText(getActivity(),i+"",Toast.LENGTH_SHORT).show();
                System.out.println(new Gson().toJson(expenseEntryPojo));

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
        autoCompleteTextViewFrom.setText("");
        editTextTo.setText("");
        autoCompleteTextViewList.setText("");



    }

    private void setNone() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, item);
        autoCompleteTextViewList.setAdapter(adapter);
    }

    private void setEmployee() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, employees);

        autoCompleteTextViewList.setAdapter(adapter);
        autoCompleteTextViewList.setThreshold(1);

    }

    private void setVendor() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, vendors);

        autoCompleteTextViewList.setAdapter(adapter);
        autoCompleteTextViewList.setThreshold(1);

    }

    private void getEmployees() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Retrieving Employees...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientGetEmployee = ApiClientBase.getApiClient().create(ApiClientGetEmployee.class);
        Call<ArrayList<EmployeePojo>> call= apiClientGetEmployee.getEmployees("employee/centre="+ PreferencedData.getPrefDeliveryCentreId(getActivity()));
        call.enqueue(new Callback<ArrayList<EmployeePojo>>() {
            @Override
            public void onResponse(Call<ArrayList<EmployeePojo>> call, Response<ArrayList<EmployeePojo>> response) {


                employeePojo=response.body();

                // Toast.makeText(getActivity(),employeePojo.toString()+"",Toast.LENGTH_SHORT).show();
                if(employeePojo!=null&&employeePojo.size()>0) {

                    for (int i = 0; i < employeePojo.size(); i++) {
                        employees.add(i, employeePojo.get(i).getEmployeeName());
                    }



                    //  Toast.makeText(ReportActivity.this,itempojo.getItems().toString()+"",Toast.LENGTH_SHORT).show();

                }

                else {
                //    relativeLayout.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"No Employee Added",Toast.LENGTH_SHORT).show();
                }

                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ArrayList<EmployeePojo>> call, Throwable t) {
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

                    autoCompleteTextViewList.setAdapter(adapter);
                    autoCompleteTextViewList.setThreshold(1);



                    //  Toast.makeText(ReportActivity.this,itempojo.getItems().toString()+"",Toast.LENGTH_SHORT).show();

                }

                else {
                  //  relativeLayout.setVisibility(View.GONE);
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

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, accounts);
                    autoCompleteTextViewFrom.setAdapter(adapter);
                    autoCompleteTextViewFrom.setThreshold(1);



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
                pDialog.dismiss();

                relativeLayout.setVisibility(View.GONE);


                Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });

    }

}
