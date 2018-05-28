package yp.com.akki.ypreportadmin.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.adapter.CustomAdapterAttendanceReport;
import yp.com.akki.ypreportadmin.pojo.attendanceRecord.AttendanceRecordPojo;


public class AttendanceReportActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Boolean sameMonth=false;
    private String intentListData;
    private ArrayList<AttendanceRecordPojo> attendanceRecordPojo;
    private CustomAdapterAttendanceReport customAdapterAttendanceReport;
    private Toolbar tb;
    private String fromdate,todate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_report);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);


        tb=(Toolbar)findViewById(R.id.toolbar);
        tb.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(tb);
        getSupportActionBar().setTitle("Attendance Report");
        tb.setNavigationIcon(R.mipmap.ic_back);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sameMonth=getIntent().getBooleanExtra("sameMonth",false);
        intentListData=getIntent().getStringExtra("data");
        fromdate=getIntent().getStringExtra("fromdate");
        todate=getIntent().getStringExtra("todate");


        setList();
    }

    private void setList() {

        attendanceRecordPojo = new Gson().fromJson(intentListData,new TypeToken<ArrayList<AttendanceRecordPojo>>(){}.getType());

        customAdapterAttendanceReport = new CustomAdapterAttendanceReport(this,attendanceRecordPojo,this,fromdate,todate,sameMonth);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(customAdapterAttendanceReport);


    }
}
