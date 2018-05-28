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
import yp.com.akki.ypreportadmin.activity.ReportActivity;
import yp.com.akki.ypreportadmin.global.PreferencedData;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.ApiClientCentreReport;
import yp.com.akki.ypreportadmin.network.ApiClientCentreReportItemUsage;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.history.CentreReportpojo;
import yp.com.akki.ypreportadmin.pojo.history.ItemUsagePojo;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseDateFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private int i=0;
    private EditText editTextFromDate,editTextToDate;
    private Button button;
    private String formattedDate;
    private ApiClientCentreReport apiClientCentreReport;
    private ApiClientCentreReportItemUsage apiClientCentreReportItemUsage;
    private ArrayList<CentreReportpojo> historyPojo;
    private ArrayList<ItemUsagePojo> itemUsagePojo;
    private int year,month,day;


    public ChooseDateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_choose_date, container, false);

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

                DatePickerDialog dp=new DatePickerDialog(getActivity(),ChooseDateFragment.this, year, month, day);
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

                DatePickerDialog dp=new DatePickerDialog(getActivity(),ChooseDateFragment.this, year, month, day);
                dp.getDatePicker().setMaxDate(new Date().getTime());

                dp.show();
                i=1;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(NetworkCheck.isNetworkAvailable(getContext()))
                    getHistory();

                else {
                    Toast.makeText(getActivity(),"Network Unavilable",Toast.LENGTH_SHORT).show();
                }

            }
        });



        return view;
    }




    private void getHistory() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientCentreReport = ApiClientBase.getApiClient().create(ApiClientCentreReport.class);


        String url="reportAdmin/centreAdminId="+PreferencedData.getPrefDeliveryCentreId(getContext())+"&fromdate="+editTextFromDate.getText().toString()+"&todate="+editTextToDate.getText().toString();
       // url="report/centre=RDC Ghaziabad&fromdate=2018-02-23&todate=2018-02-23";
        System.out.println(url);
        Call<ArrayList<CentreReportpojo>> call= apiClientCentreReport.getHistory(url);
        call.enqueue(new Callback<ArrayList<CentreReportpojo>>() {
            @Override
            public void onResponse(Call<ArrayList<CentreReportpojo>> call, Response<ArrayList<CentreReportpojo>> response) {

                historyPojo=response.body();
               // Toast.makeText(getContext(),""+response.body(),Toast.LENGTH_SHORT).show();

                if(historyPojo!=null&&historyPojo.size()>0)
                {

               //   Toast.makeText(getContext(),""+historyPojo.get(0).toString(),Toast.LENGTH_SHORT).show();

                 //   getItemUsage();

                    //delete history Activity


                    Intent i=new Intent(getActivity(),ReportActivity.class);
                    i.putExtra("data",new Gson().toJson(historyPojo));
                    i.putExtra("fromDate",new Gson().toJson(editTextFromDate.getText().toString()));
                    i.putExtra("toDate",new Gson().toJson(editTextToDate.getText().toString()));
                    startActivity(i);
                }

                else
                {
                    Toast.makeText(getActivity(),"No Data Found",Toast.LENGTH_SHORT).show();
                }

                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ArrayList<CentreReportpojo>> call, Throwable t) {
                pDialog.dismiss();


                Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });
    }

    private void getItemUsage() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientCentreReportItemUsage = ApiClientBase.getApiClient().create(ApiClientCentreReportItemUsage.class);
        String url="itemUsagereport/centre="+PreferencedData.getPrefDeliveryCentre(getContext())+"&fromdate="+editTextFromDate.getText().toString()+"&todate="+editTextToDate.getText().toString();
        // url="report/centre=RDC Ghaziabad&fromdate=2018-02-23&todate=2018-02-23";
        System.out.println(url);
        Call<ArrayList<ItemUsagePojo>> call= apiClientCentreReportItemUsage.getHistoryItemUsage(url);
        call.enqueue(new Callback<ArrayList<ItemUsagePojo>>() {
            @Override
            public void onResponse(Call<ArrayList<ItemUsagePojo>> call, Response<ArrayList<ItemUsagePojo>> response) {

                itemUsagePojo=response.body();
                // Toast.makeText(getContext(),""+response.body(),Toast.LENGTH_SHORT).show();

                if(itemUsagePojo!=null&&itemUsagePojo.size()>0)
                {

                 //   Toast.makeText(getContext(),""+itemUsagePojo.toString(),Toast.LENGTH_SHORT).show();

                       Intent i=new Intent(getActivity(),ReportActivity.class);
                       i.putExtra("data",new Gson().toJson(historyPojo));
                       i.putExtra("fromdate",new Gson().toJson(editTextFromDate.getText().toString()));
                       i.putExtra("todate",new Gson().toJson(editTextToDate.getText().toString()));
                       startActivity(i);
                }

                else
                {
                    Toast.makeText(getActivity(),"No Data Found",Toast.LENGTH_SHORT).show();
                }

                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ArrayList<ItemUsagePojo>> call, Throwable t) {
                pDialog.dismiss();


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
