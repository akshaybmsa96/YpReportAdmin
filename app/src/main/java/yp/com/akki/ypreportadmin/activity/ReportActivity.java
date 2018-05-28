package yp.com.akki.ypreportadmin.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.util.ArrayList;

import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.adapter.CustomSaleAdapter;
import yp.com.akki.ypreportadmin.pojo.history.CentreReportpojo;
import yp.com.akki.ypreportadmin.pojo.reportPojo.ReportPojo;

public class ReportActivity extends AppCompatActivity  {

    private Toolbar tb;
    private RecyclerView recyclerView;
    private String data;
    private ArrayList<CentreReportpojo> reportPojo;
    private CustomSaleAdapter customSaleAdapter;
    private String fromDate,toDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        tb=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        tb.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setTitle("Sale");

        tb.setNavigationIcon(R.mipmap.ic_back);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);

        data=getIntent().getStringExtra("data");
        fromDate=getIntent().getStringExtra("fromDate");
        toDate=getIntent().getStringExtra("toDate");

        initialize();
        show();
    }

    private void show() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        customSaleAdapter=new CustomSaleAdapter(this,reportPojo,this,fromDate,toDate);
        recyclerView.setAdapter(customSaleAdapter);

    }

    private void initialize() {

        reportPojo=new Gson().fromJson(data,new TypeToken<ArrayList<CentreReportpojo>>(){}.getType());
       // Toast.makeText(this,reportPojo.getItems().toString(),Toast.LENGTH_LONG).show();
    }
}
