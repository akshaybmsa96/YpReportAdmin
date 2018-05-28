package yp.com.akki.ypreportadmin.fragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import yp.com.akki.ypreportadmin.adapter.CustomAdaptderAttendance;
import yp.com.akki.ypreportadmin.global.PreferencedData;
import yp.com.akki.ypreportadmin.lists.NonScrollRecyclerView;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.ApiClientGetEmployee;
import yp.com.akki.ypreportadmin.network.ApiClientGetTodayAttendance;
import yp.com.akki.ypreportadmin.network.ApiClientRemoveTodayAttendance;
import yp.com.akki.ypreportadmin.network.ApiClientSubmitAttendance;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.employee.EmployeeAttendancePojo;
import yp.com.akki.ypreportadmin.pojo.employee.EmployeePojo;


/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceEntryFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private NonScrollRecyclerView recyclerView;
    private ApiClientGetEmployee apiClientGetEmployee;
    private ArrayList<EmployeePojo> employeePojo;
    private CustomAdaptderAttendance customAdapterAttendance;
    private ApiClientSubmitAttendance apiClientSubmitAttendance;
    private ApiClientRemoveTodayAttendance apiClientRemoveTodayAttendance;
    private RelativeLayout relativeLayout;
    private int year,month,day;
    private String formattedDate;
    private EditText editTextDate;
    private Button buttonSubmit;
    private EmployeeAttendancePojo temp;
    private ArrayList<EmployeeAttendancePojo> employeeAttendancePojo = new ArrayList<>();
    private ArrayList<EmployeeAttendancePojo> employeeAttendancePojoToday = new ArrayList<>();;
    private ApiClientGetTodayAttendance apiClientGetTodayAttendance;


    public AttendanceEntryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance_entry, container, false);

        relativeLayout=(RelativeLayout)view.findViewById(R.id.fragmentAttendance);
        relativeLayout.setVisibility(View.GONE);

        if(NetworkCheck.isNetworkAvailable(getActivity()))
        {
            getEmployees();
        }

        recyclerView = (NonScrollRecyclerView)view.findViewById(R.id.recyclerView);
        editTextDate = (EditText)view.findViewById(R.id.editTextDate);
        buttonSubmit = (Button)view.findViewById(R.id.buttonSubmit);


        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = sdf.format(c.getTime());
        editTextDate.setText(formattedDate);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //      Toast.makeText(getContext(),new Gson().toJson(employeeAttendancePojo)+"",Toast.LENGTH_LONG).show();


                //       employeeAttendancePojo.size() < employeePojo.size()


                //      Toast.makeText(getContext(),""+employeeAttendancePojo.size(),Toast.LENGTH_SHORT).show();

                if(checkAllMarked())
                {
                    Toast.makeText(getContext(),"All Attendance Are not marked",Toast.LENGTH_SHORT).show();
                }

                else if(employeeAttendancePojo.size()<1||employeeAttendancePojo==null)
                {
                    Toast.makeText(getContext(),"Nothing Marked",Toast.LENGTH_SHORT).show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Summit Attendance " + editTextDate.getText().toString() + " ?");
                    builder.setMessage("Are You Sure?");
                    builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            System.out.println(new Gson().toJson(employeePojo));

                            if(NetworkCheck.isNetworkAvailable(getContext())) {


                                System.out.println(new Gson().toJson(employeeAttendancePojo));
                                removeTodayAttendance();

                            }

                            else {

                                Toast.makeText(getContext(),"Network Unavailable",Toast.LENGTH_LONG).show();
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


        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dp=new DatePickerDialog(getActivity(),AttendanceEntryFragment.this, year, month, day);
                dp.getDatePicker().setMaxDate(new Date().getTime());

                dp.show();
            }
        });


        return view;
    }

    private void removeNonCheckedEntry() {

        //   int size = employeeAttendancePojo.size();

        for(int i = 0 ; i < employeeAttendancePojo.size() ; i++)
        {
            if(employeeAttendancePojo.get(i).getAttendance().equals("") || employeeAttendancePojo.get(i).getAttendance().equals(null) )
            {
                //     Toast.makeText(getContext(),"Removed called",Toast.LENGTH_SHORT).show();
                employeeAttendancePojo.remove(i);

                i--;

                System.out.println("Removed " + i);
            }
        }

        //   Toast.makeText(getContext(),""+employeeAttendancePojo.size(),Toast.LENGTH_SHORT).show();

        //   System.out.println(new Gson().toJson(employeeAttendancePojo));

        //   Toast.makeText(getContext(),""+employeeAttendancePojo.size(),Toast.LENGTH_SHORT).show();

    }


    private boolean checkAllMarked() {

//        for(int i =0 ; i<employeeAttendancePojo.size() ; i++)
//        {
//            if(employeeAttendancePojo.get(i).getAttendance().equals(""))
//            {
//                return true;
//            }
//        }
//
//        return false;

        return false;
    }

    private void removeTodayAttendance() {

        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientRemoveTodayAttendance = ApiClientBase.getApiClient().create(ApiClientRemoveTodayAttendance.class);
        String url="attendance/date="+editTextDate.getText().toString()+"&centre="+ PreferencedData.getPrefDeliveryCentreId(getContext());
        Call<String> call= apiClientRemoveTodayAttendance.removeTodayAttendance(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String i=response.body();
                //  Toast.makeText(ReportActivity.this,itempojo.getItems().toString()+"",Toast.LENGTH_SHORT).show();

                if(i!=null && i.equals("1"))
                {

                    //   Toast.makeText(getContext(),"Attendance Submitted",Toast.LENGTH_SHORT).show();


                    submit();



                }

                else {
                    Toast.makeText(getContext(),"Something went wrong...Try Again",Toast.LENGTH_SHORT).show();
                }
                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pDialog.hide();


                Toast.makeText(getContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });

    }

    private void initArray() {


        for(int i = 0;i<employeePojo.size();i++) {

            temp= new EmployeeAttendancePojo();
            temp.setName(employeePojo.get(i).getEmployeeName());
            temp.setDate(editTextDate.getText().toString());
            temp.setEmployeeId(employeePojo.get(i).getId());
            temp.setCentre(PreferencedData.getPrefDeliveryCentre(getActivity()));
            temp.setAttendance("");
            temp.setCentreId(PreferencedData.getPrefDeliveryCentreId(getActivity()));
            temp.setEmployeeId(employeePojo.get(i).get_id());


            employeeAttendancePojo.add(temp);
        }
    }

    private void submit() {


        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        //   System.out.println("send json "+new Gson().toJson(employeeAttendancePojo));

        // show it
        apiClientSubmitAttendance = ApiClientBase.getApiClient().create(ApiClientSubmitAttendance.class);
        // String url="attendance/date="+editTextDate.getText().toString()+"&centre="+PreferencedData.getPrefDeliveryCentre(getContext());
        Call<String> call= apiClientSubmitAttendance.submitAttendance(new Gson().toJson(employeeAttendancePojo));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String i=response.body();
                //  Toast.makeText(ReportActivity.this,itempojo.getItems().toString()+"",Toast.LENGTH_SHORT).show();

                if(i!=null && i.equals("1"))
                {

                    Toast.makeText(getContext(),"Attendance Submitted",Toast.LENGTH_SHORT).show();


                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.home_layout_id, new StockStatusFragment(), "NewFragmentTag");
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(PreferencedData.getPrefDeliveryCentre(getActivity()));
                    ft.commit();
                    getActivity().recreate();



                }

                else {
                    Toast.makeText(getContext(),"Cannot Submit",Toast.LENGTH_SHORT).show();
                }
                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pDialog.hide();


                Toast.makeText(getContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });


    }

    private void getEmployees() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
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


                    //       initArray();


                    getTodayAttendance();

                    //  Toast.makeText(ReportActivity.this,itempojo.getItems().toString()+"",Toast.LENGTH_SHORT).show();

                    relativeLayout.setVisibility(View.VISIBLE);

                }

                else {
                    Toast.makeText(getContext(),"No Employee Added",Toast.LENGTH_SHORT).show();
                }

                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ArrayList<EmployeePojo>> call, Throwable t) {
                pDialog.hide();


                Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });
    }

    private void getTodayAttendance() {


        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientGetTodayAttendance = ApiClientBase.getApiClient().create(ApiClientGetTodayAttendance.class);

        String url = "attendance/centre="+PreferencedData.getPrefDeliveryCentreId(getContext())+"&date="+editTextDate.getText().toString();

        System.out.println("URL " + url);

        Call<ArrayList<EmployeeAttendancePojo>> call= apiClientGetTodayAttendance.getTodayAttendance(url);
        call.enqueue(new Callback<ArrayList<EmployeeAttendancePojo>>() {
            @Override
            public void onResponse(Call<ArrayList<EmployeeAttendancePojo>> call, Response<ArrayList<EmployeeAttendancePojo>> response) {


                if(response.body()!=null) {
                    employeeAttendancePojoToday = response.body();
                }

                // Toast.makeText(getActivity(),employeePojo.toString()+"",Toast.LENGTH_SHORT).show();

                //   Toast.makeText(getContext(),new Gson().toJson(employeeAttendancePojoToday)+"",Toast.LENGTH_SHORT).show();

                customAdapterAttendance = new CustomAdaptderAttendance(getContext(), employeePojo, getActivity(), employeeAttendancePojo, editTextDate, employeeAttendancePojoToday);
                recyclerView.setAdapter(customAdapterAttendance);

                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ArrayList<EmployeeAttendancePojo>> call, Throwable t) {
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

        employeeAttendancePojo.clear();
        employeeAttendancePojoToday.clear();
        employeePojo.clear();

        customAdapterAttendance.notifyDataSetChanged();
        getEmployees();

    }

}
