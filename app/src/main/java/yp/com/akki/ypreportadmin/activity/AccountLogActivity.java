package yp.com.akki.ypreportadmin.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;

import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.adapter.CustomAdapterAccountLog;
import yp.com.akki.ypreportadmin.pojo.accounts.AccountLogPojo;

public class AccountLogActivity extends AppCompatActivity {

    private Toolbar tb;
    private String intentData;
    private RecyclerView recyclerView;
    private ArrayList<AccountLogPojo> accountLogPojo;
    private StickyRecyclerHeadersDecoration headersDecor;
    private CustomAdapterAccountLog customAdapterAccountLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_log);

        recyclerView= (RecyclerView)findViewById(R.id.recyclerView);

        tb=(Toolbar)findViewById(R.id.toolbar);
        tb.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(tb);
        getSupportActionBar().setTitle("Accounts Log");
        tb.setNavigationIcon(R.mipmap.ic_back);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        intentData=getIntent().getStringExtra("data");

        setFields();
    }

    private void setFields() {

        accountLogPojo = new Gson().fromJson(intentData,new TypeToken<ArrayList<AccountLogPojo>>(){}.getType());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        customAdapterAccountLog= new CustomAdapterAccountLog(this,accountLogPojo,this);

        recyclerView.setAdapter(customAdapterAccountLog);
        headersDecor = new StickyRecyclerHeadersDecoration(customAdapterAccountLog);
        recyclerView.addItemDecoration(headersDecor);

    }
}