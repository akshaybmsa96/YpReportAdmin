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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.global.PreferencedData;
import yp.com.akki.ypreportadmin.network.ApiClientAddEmployee;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.employee.EmployeePojo;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeeAddFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private Button buttonAddEmployee;
    private EditText editTextName,editTextDesignation,editTextDateOfJoining,editTextPhoneNumber
            ,editTextAddress,editTextLeavesPerMonth,editTextAadhaarNumber,editTextSalary,editTextBalance;
    private int year,month,day;
    private String formattedDate;
    private ApiClientAddEmployee apiClientAddEmployee;
    private EmployeePojo employeePojo;


    public EmployeeAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_employee_add, container, false);

        buttonAddEmployee=(Button)view.findViewById(R.id.buttonAddEmployee);
        editTextName=(EditText)view.findViewById(R.id.editTextName);
        editTextDesignation=(EditText)view.findViewById(R.id.editTextDesignation);
        editTextDateOfJoining=(EditText)view.findViewById(R.id.editTextDate);
        editTextPhoneNumber=(EditText)view.findViewById(R.id.editTextPhoneNumber);
        editTextAddress=(EditText)view.findViewById(R.id.editTextAddress);
        editTextSalary=(EditText)view.findViewById(R.id.editTextSalary);
        editTextBalance=(EditText)view.findViewById(R.id.editTextBalance);
        editTextLeavesPerMonth=(EditText)view.findViewById(R.id.editTextLeavesPerMonth);
        editTextAadhaarNumber=(EditText)view.findViewById(R.id.editTextAadhaarNumber);
        employeePojo = new EmployeePojo();

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = sdf.format(c.getTime());
        editTextDateOfJoining.setText(formattedDate);

        buttonAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editTextName.getText().toString().equals(""))
                {
                    editTextName.setError("Enter Name");
                    editTextName.requestFocus();
                }

                else if(editTextDesignation.getText().toString().equals(""))
                {
                    editTextDesignation.setError("Enter Position");
                    editTextDesignation.requestFocus();
                }

                else if(editTextPhoneNumber.getText().toString().length()<10)
                {
                    editTextPhoneNumber.setError("10 Digits are Required");
                    editTextPhoneNumber.requestFocus();
                }

                else if(editTextAddress.getText().toString().equals(""))
                {
                    editTextAddress.setError("Enter Address");
                    editTextAddress.requestFocus();
                }

                else if(editTextSalary.getText().toString().equals(""))
                {
                    editTextSalary.setError("Enter Salary");
                    editTextSalary.requestFocus();
                }


                else if(editTextLeavesPerMonth.getText().toString().equals(""))
                {
                    editTextLeavesPerMonth.setError("Enter Leaves Per Month");
                    editTextLeavesPerMonth.requestFocus();
                }

                else
                    {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Add " + editTextName.getText().toString() + " ?");
                        builder.setMessage("Are You Sure?");
                        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                if(NetworkCheck.isNetworkAvailable(getContext())) {


                                    employeePojo.setEmployeeName(editTextName.getText().toString());
                                    employeePojo.setAddress(editTextAddress.getText().toString());
                                    employeePojo.setDateOfJoining(editTextDateOfJoining.getText().toString());
                                    employeePojo.setDesignation(editTextDesignation.getText().toString());
                                    employeePojo.setLeavesPerMonth(editTextLeavesPerMonth.getText().toString());
                                    employeePojo.setAadhaarNumber(editTextAadhaarNumber.getText().toString()+"");
                                    employeePojo.setPhoneNumber(editTextPhoneNumber.getText().toString());
                                    employeePojo.setSalary(editTextSalary.getText().toString());
                                    employeePojo.setCentre(PreferencedData.getPrefDeliveryCentre(getActivity()));
                                    employeePojo.setCentreId(PreferencedData.getPrefDeliveryCentreId(getActivity()));

                                    if(!editTextBalance.getText().toString().equals(""))
                                    employeePojo.setCurrentBalance(editTextBalance.getText().toString());

                                    else
                                        employeePojo.setCurrentBalance("0");

                                    System.out.println(new Gson().toJson(employeePojo));

                                    addEmployee();

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


        editTextDateOfJoining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dp=new DatePickerDialog(getActivity(),EmployeeAddFragment.this, year, month, day);
                dp.getDatePicker().setMaxDate(new Date().getTime());

                dp.show();

            }
        });





        return view;
    }

    private void addEmployee() {

        //send data in db

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientAddEmployee = ApiClientBase.getApiClient().create(ApiClientAddEmployee.class);
        Call<String> call= apiClientAddEmployee.addEmployee(new Gson().toJson(employeePojo));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String i=response.body();
                //  Toast.makeText(getActivity(),i+"",Toast.LENGTH_SHORT).show();

                if(i!=null&&i.equals("1"))
                {

                    Toast.makeText(getActivity(),"Employee Added",Toast.LENGTH_SHORT).show();
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

        editTextName.setText("");
        editTextName.requestFocus();
        editTextDesignation.setText("");
        editTextPhoneNumber.setText("");
        editTextAddress.setText("");
        editTextSalary.setText("");
        editTextLeavesPerMonth.setText("");
        editTextAadhaarNumber.setText("");
        editTextBalance.setText("");

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(c.getTime());
        // Toast.makeText(ReportActivity.this,formattedDate,Toast.LENGTH_SHORT).show();


            editTextDateOfJoining.setText(formattedDate);

    }
}
