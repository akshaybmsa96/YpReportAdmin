package yp.com.akki.ypreportadmin.fragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.ApiClientCreditEmployeeSalary;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.employee.EmployeePojo;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeeSalaryCalculateFragment extends DialogFragment {

    private Button buttonCreditSalary;
    private EditText editTextName,editTextDesignation,editTextAddress,editTextLeavesPerMonth,editTextSalary,editTextCalculatedSalary;
    private String data,calculstedSalary;
    private EmployeePojo employeePojo;
    private ApiClientCreditEmployeeSalary apiClientCreditEmployeeSalary;


    public EmployeeSalaryCalculateFragment() {
        // Required empty public constructor
    }

    static public EmployeeSalaryCalculateFragment newInstance (String data,String salary)
    {

        EmployeeSalaryCalculateFragment f = new EmployeeSalaryCalculateFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("data", data);
        args.putString("salary",salary);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Pick a style based on the num.
        setHasOptionsMenu(true);
        data = getArguments().getString("data");
        calculstedSalary=getArguments().getString("salary");
        initialize();

    }

    private void initialize() {

        employeePojo = new Gson().fromJson(data,EmployeePojo.class);

    }

    private void setFields() {

        editTextName.setText(employeePojo.getEmployeeName());
        editTextDesignation.setText(employeePojo.getDesignation());
        editTextAddress.setText(employeePojo.getAddress());
        editTextSalary.setText(employeePojo.getSalary());
        editTextCalculatedSalary.setText(calculstedSalary);
        editTextLeavesPerMonth.setText(employeePojo.getLeavesPerMonth());



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employee_salary_calculate, container, false);



        buttonCreditSalary=(Button)view.findViewById(R.id.buttonCreditSalary);
        editTextName=(EditText)view.findViewById(R.id.editTextName);
        editTextDesignation=(EditText)view.findViewById(R.id.editTextDesignation);
        editTextAddress=(EditText)view.findViewById(R.id.editTextAddress);
        editTextSalary=(EditText)view.findViewById(R.id.editTextSalary);
        editTextCalculatedSalary=(EditText)view.findViewById(R.id.editTextCalculatedSalary);
        editTextLeavesPerMonth=(EditText)view.findViewById(R.id.editTextLeavesPerMonth);


        setFields();


        buttonCreditSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    ///
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Credit  ₹ " + editTextCalculatedSalary.getText().toString() + " To " + editTextName.getText().toString() + " ?");
                    builder.setMessage("Are You Sure?");
                    builder.setPositiveButton("CREDIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            if(NetworkCheck.isNetworkAvailable(getContext())) {


                                creditSalary();

                            }

                            else {

                                Toast.makeText(getActivity(), "Network Unavailable", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();



                }

        });

        /*

        editTextCalculatedSalary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {

                if(editTextCalculatedSalary.getText().toString().equals(""))
                {
                    editTextCalculatedSalary.setText(calculstedSalary);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        */


        return view;
    }

    private void creditSalary() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientCreditEmployeeSalary = ApiClientBase.getApiClient().create(ApiClientCreditEmployeeSalary.class);

        String url = "employee/id=" + employeePojo.get_id() +"&amount=" + editTextCalculatedSalary.getText().toString();

        Call<String> call= apiClientCreditEmployeeSalary.creditSalary(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String i=response.body();
                //  Toast.makeText(getActivity(),i+"",Toast.LENGTH_SHORT).show();

                if(i!=null&&i.equals("1"))
                {

                    Toast.makeText(getActivity(),"₹ "+ editTextCalculatedSalary.getText().toString()+" Credited To " + editTextName.getText().toString(),Toast.LENGTH_SHORT).show();
                    dismiss();

                }

                else {
                    Toast.makeText(getActivity(),"Cannot credit salary",Toast.LENGTH_SHORT).show();
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

}