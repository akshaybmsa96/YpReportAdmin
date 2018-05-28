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
import yp.com.akki.ypreportadmin.activity.PurchaseReportActivity;
import yp.com.akki.ypreportadmin.global.PreferencedData;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.ApiClientPurchaseReport;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.purchase.PurchaseReportPojo;


/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseReportFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private ApiClientPurchaseReport apiClientPurchaseReport;
    private ArrayList<PurchaseReportPojo> purchaseReportPojo=new ArrayList<>();
    private String fromdate,todate;
    private EditText editTextFromDate,editTextToDate;
    private Button button;
    private String formattedDate;
    private int i=0;
    private int year,month,day;



    public PurchaseReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_purchase_report, container, false);

        button=(Button) view.findViewById(R.id.button);
        editTextFromDate=(EditText)view.findViewById(R.id.editTextFromDate);
        editTextToDate=(EditText)view.findViewById(R.id.editTextToDate);

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

                DatePickerDialog dp=new DatePickerDialog(getActivity(),PurchaseReportFragment.this, year, month, day);
                dp.getDatePicker().setMaxDate(new Date().getTime());

                dp.show();

                i=0;

            }
        });

        editTextToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dp=new DatePickerDialog(getActivity(),PurchaseReportFragment.this, year, month, day);
                dp.getDatePicker().setMaxDate(new Date().getTime());

                dp.show();

                i=1;
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(NetworkCheck.isNetworkAvailable(getContext()))
                {
                    getPurchaseReport();
                }

                else
                {
                    Toast.makeText(getContext(),"Network Connection Error",Toast.LENGTH_LONG).show();
                }

            }
        });



        return view;
    }

    private void getPurchaseReport() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());


            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();

        // show it
        apiClientPurchaseReport = ApiClientBase.getApiClient().create(ApiClientPurchaseReport.class);
        Call<ArrayList<PurchaseReportPojo>> call= apiClientPurchaseReport.getPurchaseReport("purchase/centre="+ PreferencedData.getPrefDeliveryCentreId(getActivity())+"&fromdate="+editTextFromDate.getText().toString()+"&todate="+editTextToDate.getText().toString());
        call.enqueue(new Callback<ArrayList<PurchaseReportPojo>>() {
            @Override
            public void onResponse(Call<ArrayList<PurchaseReportPojo>> call, Response<ArrayList<PurchaseReportPojo>> response) {


                purchaseReportPojo=response.body();

                //   Toast.makeText(getContext(),purchaseReportPojo.toString(),Toast.LENGTH_SHORT).show();
                if(purchaseReportPojo!=null&&purchaseReportPojo.size()>0) {

                    Intent intent = new Intent(getContext(), PurchaseReportActivity.class);
                    intent.putExtra("data",new Gson().toJson(purchaseReportPojo));
                    startActivity(intent);

                  //    Toast.makeText(getContext(),purchaseReportPojo.toString(),Toast.LENGTH_SHORT).show();


                }

                else if(purchaseReportPojo.size()==0)
                {
                    Toast.makeText(getContext(),"No Data Found",Toast.LENGTH_SHORT).show();
                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<ArrayList<PurchaseReportPojo>> call, Throwable t) {

                pDialog.dismiss();


               // if(skip==0)
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
