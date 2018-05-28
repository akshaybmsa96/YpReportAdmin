package yp.com.akki.ypreportadmin.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.ApiClientUpdateEmployee;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.employee.EmployeePojo;


public class EmployeeEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Button buttonEditEmployee;
    private EditText editTextName,editTextDesignation,editTextDateOfJoining,editTextPhoneNumber,
            editTextAddress,editTextLeavesPerMonth,editTextAadhaarNumber,editTextSalary,editTextBalance;
    private ApiClientUpdateEmployee apiClientupdateEmployee;
    private EmployeePojo employeePojo;
    private String intentData;
    private int year,month,day;
    private String formattedDate;
    private Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_edit);

        buttonEditEmployee=(Button)findViewById(R.id.buttonEditEmployee);
        editTextName=(EditText)findViewById(R.id.editTextName);
        editTextDesignation=(EditText)findViewById(R.id.editTextDesignation);
        editTextDateOfJoining=(EditText)findViewById(R.id.editTextDate);
        editTextPhoneNumber=(EditText)findViewById(R.id.editTextPhoneNumber);
        editTextAddress=(EditText)findViewById(R.id.editTextAddress);
        editTextSalary=(EditText)findViewById(R.id.editTextSalary);
        editTextLeavesPerMonth=(EditText)findViewById(R.id.editTextLeavesPerMonth);
        editTextAadhaarNumber=(EditText)findViewById(R.id.editTextAadhaarNumber);
        editTextBalance=(EditText)findViewById(R.id.editTextBalance);


        tb=(Toolbar)findViewById(R.id.toolbar);
        tb.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(tb);
        getSupportActionBar().setTitle("Edit Information");
        tb.setNavigationIcon(R.mipmap.ic_back);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        intentData=getIntent().getStringExtra("data");


        setDataToFields();

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        c.set(year, month, day);

        editTextDateOfJoining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dp=new DatePickerDialog(EmployeeEditActivity.this,EmployeeEditActivity.this, year, month, day);
                dp.getDatePicker().setMaxDate(new Date().getTime());

                dp.show();

            }
        });



        buttonEditEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(NetworkCheck.isNetworkAvailable(EmployeeEditActivity.this))
                {
                    if(validate())
                    {
                        setpojo();
                        update();
                    }
                }

                else
                {
                    Toast.makeText(EmployeeEditActivity.this,"Network Unavailable",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void setpojo() {

        employeePojo.setEmployeeName(editTextName.getText().toString());
        employeePojo.setAddress(editTextAddress.getText().toString());
        employeePojo.setDateOfJoining(editTextDateOfJoining.getText().toString());
        employeePojo.setDesignation(editTextDesignation.getText().toString());
        employeePojo.setLeavesPerMonth(editTextLeavesPerMonth.getText().toString());
        employeePojo.setAadhaarNumber(editTextAadhaarNumber.getText().toString()+"");
        employeePojo.setPhoneNumber(editTextPhoneNumber.getText().toString());
        employeePojo.setSalary(editTextSalary.getText().toString());
        employeePojo.setCentre(PreferencedData.getPrefDeliveryCentre(this));

        if(!editTextBalance.getText().toString().equals(""))
            employeePojo.setCurrentBalance(editTextBalance.getText().toString());

        else
            employeePojo.setCurrentBalance("0");
    }

    private void update() {

        final ProgressDialog pDialog = new ProgressDialog(EmployeeEditActivity.this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        System.out.println(new Gson().toJson(employeePojo));


        // show it
        apiClientupdateEmployee = ApiClientBase.getApiClient().create(ApiClientUpdateEmployee.class);
        Call<String> call= apiClientupdateEmployee.updateEmployee(new Gson().toJson(employeePojo));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String i=response.body();
                //  Toast.makeText(getActivity(),i+"",Toast.LENGTH_SHORT).show();

                if(i!=null&&i.equals("1"))
                {

                    Toast.makeText(EmployeeEditActivity.this,"Employee Updated",Toast.LENGTH_SHORT).show();
                    Intent in =new Intent(EmployeeEditActivity.this,Dashboard.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(in);

                }

                else {
                    Toast.makeText(EmployeeEditActivity.this,"Cannot update",Toast.LENGTH_SHORT).show();
                }
                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pDialog.hide();


                Toast.makeText(EmployeeEditActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });

    }

    private boolean validate() {

        if(editTextName.getText().toString().equals(""))
        {
            editTextName.setError("Enter Name");
            editTextName.requestFocus();
            return false;
        }

        else if(editTextDesignation.getText().toString().equals(""))
        {
            editTextDesignation.setError("Enter Position");
            editTextDesignation.requestFocus();
            return false;
        }

        else if(editTextPhoneNumber.getText().toString().length()<10)
        {
            editTextPhoneNumber.setError("10 Digits are Required");
            editTextPhoneNumber.requestFocus();
            return false;
        }

        else if(editTextAddress.getText().toString().equals(""))
        {
            editTextAddress.setError("Enter Address");
            editTextAddress.requestFocus();
            return false;
        }

        else if(editTextSalary.getText().toString().equals(""))
        {
            editTextSalary.setError("Enter Salary");
            editTextSalary.requestFocus();
            return false;
        }


        else if(editTextLeavesPerMonth.getText().toString().equals(""))
        {
            editTextLeavesPerMonth.setError("Enter Leaves Per Month");
            editTextLeavesPerMonth.requestFocus();
            return false;
        }

        else {

            return true;
        }

    }

    private void setDataToFields() {

        employeePojo= new Gson().fromJson(intentData,EmployeePojo.class);

        editTextName.setText(employeePojo.getEmployeeName());
        editTextAddress.setText(employeePojo.getAddress());
        editTextDateOfJoining.setText(employeePojo.getDateOfJoining());
        editTextDesignation.setText(employeePojo.getDesignation());
        editTextLeavesPerMonth.setText(employeePojo.getLeavesPerMonth());
        editTextAadhaarNumber.setText(employeePojo.getAadhaarNumber());
        editTextPhoneNumber.setText(employeePojo.getPhoneNumber());
        editTextSalary.setText(employeePojo.getSalary());
        editTextBalance.setText(employeePojo.getCurrentBalance());

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
