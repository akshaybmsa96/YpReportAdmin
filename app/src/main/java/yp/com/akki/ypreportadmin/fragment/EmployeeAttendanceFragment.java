package yp.com.akki.ypreportadmin.fragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import yp.com.akki.ypreportadmin.activity.AttendanceReportActivity;
import yp.com.akki.ypreportadmin.global.PreferencedData;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.ApiClientGetAttendance;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.attendanceRecord.AttendanceRecordPojo;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeeAttendanceFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private int i=0;
    private EditText editTextFromDate,editTextToDate;
    private Button button;
    private String formattedDate;
    private int year,month,day;
    private ApiClientGetAttendance apiClientGetAttendance;
    private ArrayList<AttendanceRecordPojo> attendanceRecordPojo;
    private boolean sameMonthCheck=false;


    public EmployeeAttendanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employee_attendance, container, false);

        editTextFromDate=(EditText)view.findViewById(R.id.editTextFromDate);
        editTextToDate=(EditText)view.findViewById(R.id.editTextToDate);
        button=(Button)view.findViewById(R.id.button);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = sdf.format(c.getTime());
        editTextFromDate.setText(formattedDate);
        editTextToDate.setText(formattedDate);



        editTextFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //      DialogFragment picker = new DateDualFragment();
                //      picker.show(getActivity().getFragmentManager(), "datePicker");

                DatePickerDialog dp=new DatePickerDialog(getActivity(),EmployeeAttendanceFragment.this, year, month, day);
                dp.getDatePicker().setMaxDate(new Date().getTime());

                dp.show();

                i=0;
            }
        });

        editTextToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //   DialogFragment picker = new DateDualFragment();
                //   picker.show(getActivity().getFragmentManager(), "datePicker");

                DatePickerDialog dp=new DatePickerDialog(getActivity(),EmployeeAttendanceFragment.this, year, month, day);
                dp.getDatePicker().setMaxDate(new Date().getTime());

                dp.show();
                i=1;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(NetworkCheck.isNetworkAvailable(getContext())) {
                    checkmonth();
                    getEmployeesAttendance();
                }

                else {
                    Toast.makeText(getActivity(),"Network Unavailable",Toast.LENGTH_SHORT).show();
                }

            }
        });



        return view;
    }

    private void checkmonth() {

        String date1=editTextFromDate.getText().toString().substring(0,7);
        String date2=editTextToDate.getText().toString().substring(0,7);

      //  System.out.println("date1 = "+date1 + "  date2=" + date2);

        if(date1.equals(date2)) {
            sameMonthCheck = true;
            // Toast.makeText(getContext(),""+true,Toast.LENGTH_SHORT).show();
        }

        else
        {
            sameMonthCheck=false;
        }





    }

    private void getEmployeesAttendance() {


        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientGetAttendance = ApiClientBase.getApiClient().create(ApiClientGetAttendance.class);

        String url = "attendance/centre="+ PreferencedData.getPrefDeliveryCentre(getActivity())+"&fromdate="+editTextFromDate.getText().toString()+"&todate="+editTextToDate.getText().toString();

        Call<ArrayList<AttendanceRecordPojo>> call= apiClientGetAttendance.getAttendance(url);
        call.enqueue(new Callback<ArrayList<AttendanceRecordPojo>>() {
            @Override
            public void onResponse(Call<ArrayList<AttendanceRecordPojo>> call, Response<ArrayList<AttendanceRecordPojo>> response) {


                attendanceRecordPojo=response.body();

                // Toast.makeText(getActivity(),employeePojo.toString()+"",Toast.LENGTH_SHORT).show();
                if(attendanceRecordPojo!=null&&attendanceRecordPojo.size()>0) {


                    //  Toast.makeText(getContext(),attendanceRecordPojo.toString()+"",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity(), AttendanceReportActivity.class);
                    intent.putExtra("data",new Gson().toJson(attendanceRecordPojo));
                    intent.putExtra("sameMonth",sameMonthCheck);
                    intent.putExtra("fromdate",editTextFromDate.getText().toString());
                    intent.putExtra("todate",editTextToDate.getText().toString());
                    getContext().startActivity(intent);

                }

                else
                    Toast.makeText(getContext(),"No Record Found",Toast.LENGTH_SHORT).show();

                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ArrayList<AttendanceRecordPojo>> call, Throwable t) {
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

        if(i==0)
        {
            editTextFromDate.setText(formattedDate);
        }

        else if(i==1)
        {
            editTextToDate.setText(formattedDate);
        }
    }

}
