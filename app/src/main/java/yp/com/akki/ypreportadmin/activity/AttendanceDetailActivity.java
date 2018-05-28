package yp.com.akki.ypreportadmin.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.adapter.CustomAdapterAttendanceDetail;
import yp.com.akki.ypreportadmin.fragment.EmployeeSalaryCalculateFragment;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.ApiClientGetEmployeeById;
import yp.com.akki.ypreportadmin.pojo.attendanceRecord.AttendanceDetailPojo;
import yp.com.akki.ypreportadmin.pojo.employee.EmployeePojo;


public class AttendanceDetailActivity extends AppCompatActivity {

    private Boolean sameMonth = false;
    private String intentData,fromdate,todate;
    private RecyclerView recyclerView;
    private Button buttonCalculateSalary;
    private LinearLayout linearLayout;
    private ArrayList<AttendanceDetailPojo> attendanceDetailPojo;
    private CustomAdapterAttendanceDetail customAdapterAttendanceDetail;
    private ApiClientGetEmployeeById apiClientGetEmployeeById;
    private EmployeePojo employeePojo;
    private int fromday,today,totalDays;
    private Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_detail);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        buttonCalculateSalary=(Button)findViewById(R.id.buttonCalculateSalary);
        linearLayout=(LinearLayout)findViewById(R.id.belowLayout);

        tb=(Toolbar)findViewById(R.id.toolbar);
        tb.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(tb);
        tb.setNavigationIcon(R.mipmap.ic_back);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        sameMonth=getIntent().getBooleanExtra("sameMonth",false);
        intentData=getIntent().getStringExtra("data");
        fromdate=getIntent().getStringExtra("fromdate");
        todate=getIntent().getStringExtra("todate");

        if(sameMonth)
        {
            linearLayout.setVisibility(View.VISIBLE);
            fromday=Integer.parseInt(fromdate.substring(8,10));
            today=Integer.parseInt(todate.substring(8,10));
            totalDays = today-fromday+1;
          //  System.out.println("                  "+fromday+  "                  "  + today );
        }


        setList();

        getSupportActionBar().setTitle(attendanceDetailPojo.get(0).getName()+" Attendance Detail");


        buttonCalculateSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getEmployee();

            }
        });

    }

    private void getEmployee() {

        final ProgressDialog pDialog = new ProgressDialog(AttendanceDetailActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientGetEmployeeById = ApiClientBase.getApiClient().create(ApiClientGetEmployeeById.class);

        String url = "employee/id="+ attendanceDetailPojo.get(0).getEmployeeId();

        Call<EmployeePojo> call= apiClientGetEmployeeById.getEmployeeById(url);
        call.enqueue(new Callback<EmployeePojo>() {
            @Override
            public void onResponse(Call<EmployeePojo> call, Response<EmployeePojo> response) {


                employeePojo=response.body();

                // Toast.makeText(getActivity(),employeePojo.toString()+"",Toast.LENGTH_SHORT).show();
                if(employeePojo!=null) {

                    calculateSalary();

                    //  Toast.makeText(ReportActivity.this,itempojo.getItems().toString()+"",Toast.LENGTH_SHORT).show();

                }

                else
                    Toast.makeText(AttendanceDetailActivity.this,"Employee Not Found",Toast.LENGTH_SHORT).show();

                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<EmployeePojo> call, Throwable t) {
                pDialog.hide();


                Toast.makeText(AttendanceDetailActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });

    }

    private void calculateSalary() {

        Double presentDays = 0.0;
        Double basicSalary,salaryPerDay,totalSalary = 0.0,absents=0.0;

        for (int i =0;i<attendanceDetailPojo.size();i++)

        {
            presentDays=presentDays+Double.parseDouble(attendanceDetailPojo.get(i).getAttendance());
        }

        absents=totalDays-presentDays;
        basicSalary=Double.parseDouble(employeePojo.getSalary());
        salaryPerDay = basicSalary/30.0;


        if(presentDays<=22.0 || absents>=Double.parseDouble(employeePojo.getLeavesPerMonth()))
        {
            totalSalary = presentDays*salaryPerDay;

        //    Toast.makeText(this,""+totalSalary,Toast.LENGTH_SHORT).show();

        }

        else if(presentDays>22.0)
        {
            if(absents==0.0) {
                presentDays = presentDays + Double.parseDouble(employeePojo.getLeavesPerMonth());
                totalSalary = presentDays * salaryPerDay;
            }

            else
            {

                presentDays = presentDays + Double.parseDouble(employeePojo.getLeavesPerMonth()) - absents;
                totalSalary = presentDays * salaryPerDay;
            }

        //    Toast.makeText(this,""+totalSalary,Toast.LENGTH_SHORT).show();
        }

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        android.support.v4.app.Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        fragmentTransaction.addToBackStack(null);

        // Create and show the dialog.
        Gson gson=new Gson();
        android.support.v4.app.DialogFragment newFragment = EmployeeSalaryCalculateFragment.newInstance(gson.toJson(employeePojo),String.valueOf(Math.round(totalSalary)));
        newFragment.show(fragmentTransaction, "dialog");


    }

    private void setList() {

        attendanceDetailPojo = new Gson().fromJson(intentData,new TypeToken<ArrayList<AttendanceDetailPojo>>(){}.getType());

        customAdapterAttendanceDetail = new CustomAdapterAttendanceDetail(this,attendanceDetailPojo,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(customAdapterAttendanceDetail);

    }
}
