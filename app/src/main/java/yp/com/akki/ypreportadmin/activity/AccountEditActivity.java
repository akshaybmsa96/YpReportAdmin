package yp.com.akki.ypreportadmin.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.global.PreferencedData;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.ApiClientEditAccount;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.accounts.AccountPojo;


public class AccountEditActivity extends AppCompatActivity {

     private Toolbar tb;
     private String intentData;
     private EditText editTextName,editTextBalance;
     private AutoCompleteTextView editTextAccountType;
     private Button buttonEditAccount;
     private AccountPojo acccountPojo;
     private ApiClientEditAccount apiClientEditAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);

        editTextName=(EditText)findViewById(R.id.editTextName);
        editTextAccountType=(AutoCompleteTextView)findViewById(R.id.editTextAccountType);
        editTextBalance=(EditText)findViewById(R.id.editTextBalance);

        buttonEditAccount=(Button)findViewById(R.id.buttonEditAccount);

        tb=(Toolbar)findViewById(R.id.toolbar);
        tb.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(tb);
        getSupportActionBar().setTitle("Edit Information");
        tb.setNavigationIcon(R.mipmap.ic_back);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        intentData=getIntent().getStringExtra("data");

        setFields();


        buttonEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate()) {

                    setPojo();

                    if(NetworkCheck.isNetworkAvailable(AccountEditActivity.this))
                    editAccount();

                    else
                        Toast.makeText(AccountEditActivity.this,"Network Unavailable",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void setPojo() {

        acccountPojo.setCentre(PreferencedData.getPrefDeliveryCentre(this));
        acccountPojo.setAccountName(editTextName.getText().toString());
        acccountPojo.setType(editTextAccountType.getText().toString());
        acccountPojo.setCurrentBalance(editTextBalance.getText().toString());
    }

    private boolean validate() {

        if(editTextName.getText().toString().equals(""))
        {
            editTextName.setError("Enter Name");
            editTextName.requestFocus();
            return false;
        }

        else if(editTextAccountType.getText().toString().equals(""))
        {
            editTextAccountType.setError("Enter Account Type");
            editTextAccountType.requestFocus();
            return false;
        }

        else if(editTextBalance.getText().toString().equals(""))
        {
            editTextBalance.setError("Enter Current Balance");
            editTextBalance.requestFocus();
            return false;
        }



        return true;
    }

    private void editAccount() {

        final ProgressDialog pDialog = new ProgressDialog(AccountEditActivity.this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        System.out.println(new Gson().toJson(acccountPojo));


        // show it
        apiClientEditAccount = ApiClientBase.getApiClient().create(ApiClientEditAccount.class);
        Call<String> call= apiClientEditAccount.editAccount(new Gson().toJson(acccountPojo));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String i=response.body();
                //  Toast.makeText(getActivity(),i+"",Toast.LENGTH_SHORT).show();

                if(i!=null&&i.equals("1"))
                {

                    Toast.makeText(AccountEditActivity.this,"Account Updated",Toast.LENGTH_SHORT).show();
                    Intent in =new Intent(AccountEditActivity.this,Dashboard.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(in);

                }

                else {
                    Toast.makeText(AccountEditActivity.this,"Cannot update",Toast.LENGTH_SHORT).show();
                }
                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pDialog.hide();


                Toast.makeText(AccountEditActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });


    }

    private void setFields() {

        acccountPojo = new Gson().fromJson(intentData,AccountPojo.class);

        editTextName.setText(acccountPojo.getAccountName());
        editTextAccountType.setText(acccountPojo.getType());
        editTextBalance.setText(acccountPojo.getCurrentBalance());

    }
}
