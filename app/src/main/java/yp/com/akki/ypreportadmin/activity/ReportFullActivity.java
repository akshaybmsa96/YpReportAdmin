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

import java.util.ArrayList;

import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.adapter.CustomDetailAdapter;
import yp.com.akki.ypreportadmin.adapter.CustomSaleAdapter;
import yp.com.akki.ypreportadmin.pojo.history.CentreReportpojo;
import yp.com.akki.ypreportadmin.pojo.history.ItemUsagePojo;
import yp.com.akki.ypreportadmin.pojo.reportDetailPojo.ReportDetailPojo;
import yp.com.akki.ypreportadmin.pojo.reportPojo.ReportPojo;

public class ReportFullActivity extends AppCompatActivity {

    private Toolbar tb;
    private RecyclerView recyclerView;
    private String data;
    private ArrayList<ItemUsagePojo> itemUsagePojo;
    private CustomDetailAdapter customDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_full);

        tb=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        tb.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setTitle("Stock Usage");

        tb.setNavigationIcon(R.mipmap.ic_back);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);

        data=getIntent().getStringExtra("data");

        initialize();
        show();
    }

    private void show() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        customDetailAdapter=new CustomDetailAdapter(this,itemUsagePojo,this);
        recyclerView.setAdapter(customDetailAdapter);

    }

    private void initialize() {

        itemUsagePojo=new Gson().fromJson(data,new TypeToken<ArrayList<ItemUsagePojo>>(){}.getType());
    }
}
