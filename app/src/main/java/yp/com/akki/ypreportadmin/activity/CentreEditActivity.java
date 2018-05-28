package yp.com.akki.ypreportadmin.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.global.PreferencedData;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.ApiClientUpdateStore;
import yp.com.akki.ypreportadmin.network.ApiClientUpdateVendor;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.allItems.Items;
import yp.com.akki.ypreportadmin.pojo.store.StorePojo;

public class CentreEditActivity extends AppCompatActivity {

    private EditText editTextStoreName,editTextStoreCode,editTextGSTNumber,editTextPhoneNumber,editTextLimit,editTextBalance;
    private Button buttonEditStore;
    private StorePojo storePojo;
    private Spinner spinnerStoreType;
    private String intentData;
    private Toolbar tb;
    private ApiClientUpdateStore apiClientUpdateStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centre_edit);

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

        buttonEditStore=(Button)findViewById(R.id.buttonEditStore);
        editTextStoreName=(EditText)findViewById(R.id.editTextStoreName);
        editTextStoreCode=(EditText)findViewById(R.id.editTextStoreCode);
        editTextGSTNumber=(EditText)findViewById(R.id.editTextGSTNumber);
        editTextLimit=(EditText)findViewById(R.id.editTextLimit);
        editTextPhoneNumber=(EditText)findViewById(R.id.editTextPhoneNumber);
        editTextBalance=(EditText)findViewById(R.id.editTextBalance);

        spinnerStoreType=(Spinner)findViewById(R.id.spinnerStoreType);

        storePojo=new StorePojo();

        intentData=getIntent().getStringExtra("data");
        setFields();


        buttonEditStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate())
                {
                    if(NetworkCheck.isNetworkAvailable(CentreEditActivity.this))
                    {
                        setPojo();
                        updateCentre();
                    }

                    else
                    {
                        Toast.makeText(CentreEditActivity.this,"Network Unavailable",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private void setPojo() {

        storePojo.setCentre(editTextStoreName.getText().toString());
        storePojo.setCentreCode(editTextStoreCode.getText().toString());
        storePojo.setGstNumber(editTextGSTNumber.getText().toString());
        storePojo.setCreditLimit(editTextLimit.getText().toString());
        storePojo.setPhoneNumber(editTextPhoneNumber.getText().toString());
        storePojo.setAdminId(PreferencedData.getPrefDeliveryCentreId(CentreEditActivity.this));
        storePojo.setType(spinnerStoreType.getSelectedItem().toString());

        if(!editTextBalance.getText().toString().equals(""))
            storePojo.setCurrentBalance(editTextBalance.getText().toString());

        else
            storePojo.setCurrentBalance("0");

    }

    private void updateCentre() {

        final ProgressDialog pDialog = new ProgressDialog(CentreEditActivity.this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        System.out.println(new Gson().toJson(storePojo));


        // show it
        apiClientUpdateStore = ApiClientBase.getApiClient().create(ApiClientUpdateStore.class);
        Call<String> call= apiClientUpdateStore.updateVendor(new Gson().toJson(storePojo));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String i=response.body();
                //  Toast.makeText(getActivity(),i+"",Toast.LENGTH_SHORT).show();

                if(i!=null&&i.equals("1"))
                {

                    Toast.makeText(CentreEditActivity.this,"Centre Updated",Toast.LENGTH_SHORT).show();
                    Intent in =new Intent(CentreEditActivity.this,Dashboard.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(in);

                }

                else {
                    Toast.makeText(CentreEditActivity.this,"Cannot update",Toast.LENGTH_SHORT).show();
                }
                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pDialog.hide();


                Toast.makeText(CentreEditActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });



    }

    private boolean validate() {

        if(editTextStoreName.getText().toString().equals(""))
        {
            editTextStoreName.setError("Enter Store Name");
            editTextStoreName.requestFocus();
            return false;
        }

        else if(editTextStoreCode.getText().toString().equals(""))
        {
            editTextStoreCode.setError("Enter Store Code");
            editTextStoreCode.requestFocus();
            return false;
        }

        else if(editTextPhoneNumber.getText().toString().length()<10)
        {
            editTextPhoneNumber.setError("Enter Valid Number");
            editTextPhoneNumber.requestFocus();
            return false;
        }

        else if(editTextGSTNumber.getText().toString().equals(""))
        {
            editTextGSTNumber.setError("Enter GST Number");
            editTextGSTNumber.requestFocus();
            return false;
        }

        else if(editTextLimit.getText().toString().equals(""))
        {
            editTextLimit.setError("Enter Limit");
            editTextLimit.requestFocus();
            return false;
        }


        return true;
    }

    private void setFields() {

        storePojo=new Gson().fromJson(intentData,StorePojo.class);

        editTextStoreName.setText(storePojo.getCentre());
        editTextStoreCode.setText(storePojo.getCentreCode());
        editTextGSTNumber.setText(storePojo.getGstNumber());
        editTextLimit.setText(storePojo.getCreditLimit());
        editTextPhoneNumber.setText(storePojo.getPhoneNumber());
        editTextBalance.setText(storePojo.getCurrentBalance());

        for(int i= 0; i < spinnerStoreType.getAdapter().getCount(); i++)
        {
            if(spinnerStoreType.getAdapter().getItem(i).toString().contains(storePojo.getType()))
            {
                spinnerStoreType.setSelection(i);
            }
        }

    }
}
