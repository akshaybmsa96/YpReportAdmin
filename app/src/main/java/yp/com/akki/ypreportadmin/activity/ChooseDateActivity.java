package yp.com.akki.ypreportadmin.activity;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.fragment.DateDualFragment;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.ApiClientGetSale;
import yp.com.akki.ypreportadmin.pojo.reportPojo.ReportPojo;

public class ChooseDateActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private int i=0;
    private EditText editTextFromDate,editTextToDate;
    private Button button;
    private String formattedDate;
    private Toolbar tb;
    private ApiClientGetSale apiClientGetSale;
    private ReportPojo reportPojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_date);

        editTextFromDate=(EditText)findViewById(R.id.editTextFromDate);
        editTextToDate=(EditText)findViewById(R.id.editTextToDate);
        button=(Button)findViewById(R.id.button);

        tb=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        tb.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setTitle("Yp Report Admin");

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = sdf.format(c.getTime());
        editTextFromDate.setText(formattedDate);
        editTextToDate.setText(formattedDate);



        editTextFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment picker = new DateDualFragment();
                picker.show(getFragmentManager(), "datePicker");
                i=0;

            }
        });

        editTextToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment picker = new DateDualFragment();
                picker.show(getFragmentManager(), "datePicker");
                i=1;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isNetworkAvailable())
                    getHistory();

                else {
                    Toast.makeText(ChooseDateActivity.this,"Network Unavilable",Toast.LENGTH_SHORT).show();
                }

            }
        });




    }

    private void getHistory() {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientGetSale = ApiClientBase.getApiClient().create(ApiClientGetSale.class);
        Call<ReportPojo> call= apiClientGetSale.getSale(editTextFromDate.getText().toString(),editTextToDate.getText().toString());
        call.enqueue(new Callback<ReportPojo>() {
            @Override
            public void onResponse(Call<ReportPojo> call, Response<ReportPojo> response) {

                reportPojo=response.body();

                if(reportPojo.getItems().size()>0)
                {

                    // Toast.makeText(ChooseDateActivity.this,""+reportPojo.toString(),Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(ChooseDateActivity.this,ReportActivity.class);
                    i.putExtra("data",new Gson().toJson(reportPojo));
                    i.putExtra("fromDate",editTextFromDate.getText().toString());
                    i.putExtra("toDate",editTextToDate.getText().toString());
                    startActivity(i);
                }

                else
                {
                    Toast.makeText(ChooseDateActivity.this,"No Data Found",Toast.LENGTH_SHORT).show();
                }

                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ReportPojo> call, Throwable t) {
                pDialog.dismiss();


                Toast.makeText(ChooseDateActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
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

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
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

    public boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}