package yp.com.akki.ypreportadmin.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import yp.com.akki.ypreportadmin.network.ApiClientUpdateVendor;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.vendor.VendorPojo;

public class VendorEditActivity extends AppCompatActivity {

    private EditText editTextVendorName,editTextAddress,editTextGSTNumber,editTextPhoneNumber,editTextLimit,editTextBalance;
    private Button buttonUpdateVendor;
    private VendorPojo vendorPojo;
    private ApiClientUpdateVendor apiClientUpdateVendor;
    private String intentData;
    private Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_edit);

        buttonUpdateVendor=(Button)findViewById(R.id.buttonUpdateVendor);
        editTextVendorName=(EditText)findViewById(R.id.editTextName);
        editTextAddress=(EditText)findViewById(R.id.editTextAddress);
        editTextGSTNumber=(EditText)findViewById(R.id.editTextGSTNumber);
        editTextLimit=(EditText)findViewById(R.id.editTextLimit);
        editTextPhoneNumber=(EditText)findViewById(R.id.editTextPhoneNumber);
        editTextBalance=(EditText)findViewById(R.id.editTextBalance);

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

        buttonUpdateVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(NetworkCheck.isNetworkAvailable(VendorEditActivity.this))
                {
                    if(validate())
                    {
                        setPojo();
                        update();
                    }
                }

                else {
                    Toast.makeText(VendorEditActivity.this,"Network Unavailable",Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    private void setPojo() {

        vendorPojo.setVendorName(editTextVendorName.getText().toString());
        vendorPojo.setAddress(editTextAddress.getText().toString());
        vendorPojo.setGstNumber(editTextGSTNumber.getText().toString());
        vendorPojo.setCreditLimit(editTextLimit.getText().toString());
        vendorPojo.setPhoneNumber(editTextPhoneNumber.getText().toString());
        vendorPojo.setCentre(PreferencedData.getPrefDeliveryCentre(this));

        if(!editTextBalance.getText().toString().equals(""))
            vendorPojo.setCurrentBalance(editTextBalance.getText().toString());

        else
            vendorPojo.setCurrentBalance("0");

    }

    private void update() {

        final ProgressDialog pDialog = new ProgressDialog(VendorEditActivity.this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        System.out.println(new Gson().toJson(vendorPojo));


        // show it
        apiClientUpdateVendor = ApiClientBase.getApiClient().create(ApiClientUpdateVendor.class);
        Call<String> call= apiClientUpdateVendor.updateVendor(new Gson().toJson(vendorPojo));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String i=response.body();
                //  Toast.makeText(getActivity(),i+"",Toast.LENGTH_SHORT).show();

                if(i!=null&&i.equals("1"))
                {

                    Toast.makeText(VendorEditActivity.this,"Vendor Updated",Toast.LENGTH_SHORT).show();
                    Intent in =new Intent(VendorEditActivity.this,Dashboard.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(in);

                }

                else {
                    Toast.makeText(VendorEditActivity.this,"Cannot update",Toast.LENGTH_SHORT).show();
                }
                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pDialog.hide();


                Toast.makeText(VendorEditActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });

    }

    private boolean validate() {

        if(editTextVendorName.getText().toString().equals(""))
        {
            editTextVendorName.setError("Enter Name");
            editTextVendorName.requestFocus();
            return false;
        }

        else if(editTextAddress.getText().toString().equals(""))
        {
            editTextAddress.setError("Enter Address");
            editTextAddress.requestFocus();
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

        else {

            return true;
        }
    }

    private void setFields() {

        vendorPojo=new Gson().fromJson(intentData,VendorPojo.class);

        editTextVendorName.setText(vendorPojo.getVendorName());
        editTextAddress.setText(vendorPojo.getAddress());
        editTextGSTNumber.setText(vendorPojo.getGstNumber());
        editTextLimit.setText(vendorPojo.getCreditLimit());
        editTextPhoneNumber.setText(vendorPojo.getPhoneNumber());
        editTextBalance.setText(vendorPojo.getCurrentBalance());


    }
}
