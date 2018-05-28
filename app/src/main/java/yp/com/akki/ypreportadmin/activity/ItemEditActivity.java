package yp.com.akki.ypreportadmin.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import yp.com.akki.ypreportadmin.network.ApiClientUpdateItem;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.allItems.Items;

public class ItemEditActivity extends AppCompatActivity {

    private EditText editTextItemName,editTextUnit,editTextPurchasePrice,editTextSellingPrice,editTextCostingPrice,editTextTax;
    private Button buttonEditItem;
    private Items itemsPojo;
    private String intentData;
    private Toolbar tb;
    private ApiClientUpdateItem apiClientUpdateItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);

        buttonEditItem=(Button)findViewById(R.id.buttonEditItem);
        editTextItemName=(EditText)findViewById(R.id.editTextItemName);
        editTextUnit=(EditText)findViewById(R.id.editTextUnit);
        editTextPurchasePrice=(EditText)findViewById(R.id.editTextPurchasePrice);
        editTextSellingPrice=(EditText)findViewById(R.id.editTextSellingPrice);
        editTextCostingPrice=(EditText)findViewById(R.id.editTextCostingPrice);
        editTextTax=(EditText)findViewById(R.id.editTextTax);

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

        buttonEditItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate())
                {

                    if(NetworkCheck.isNetworkAvailable(ItemEditActivity.this)) {

                        setPojo();
                        editItem();

                    }

                    else {

                        Toast.makeText(ItemEditActivity.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });


    }

    private void setFields() {

        itemsPojo=new Gson().fromJson(intentData,Items.class);

        editTextItemName.setText(itemsPojo.getItemName());
        editTextUnit.setText(itemsPojo.getUnit());
        editTextCostingPrice.setText(itemsPojo.getCostingPrice());
        editTextTax.setText(itemsPojo.getTax());
        editTextPurchasePrice.setText(itemsPojo.getPurchasePrice());
        editTextSellingPrice.setText(itemsPojo.getSellingPrice());
    }

    private void editItem() {


        final ProgressDialog pDialog = new ProgressDialog(ItemEditActivity.this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        System.out.println(new Gson().toJson(itemsPojo));


        // show it
        apiClientUpdateItem = ApiClientBase.getApiClient().create(ApiClientUpdateItem.class);
        Call<String> call= apiClientUpdateItem.updateItem(new Gson().toJson(itemsPojo));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String i=response.body();
                //  Toast.makeText(getActivity(),i+"",Toast.LENGTH_SHORT).show();

                if(i!=null&&i.equals("1"))
                {

                    Toast.makeText(ItemEditActivity.this,"Item Updated",Toast.LENGTH_SHORT).show();
                    Intent in =new Intent(ItemEditActivity.this,Dashboard.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(in);

                }

                else {
                    Toast.makeText(ItemEditActivity.this,"Cannot update",Toast.LENGTH_SHORT).show();
                }
                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pDialog.hide();


                Toast.makeText(ItemEditActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });



    }

    private void setPojo() {

        itemsPojo.setUnit(editTextUnit.getText().toString());
        itemsPojo.setItemName(editTextItemName.getText().toString());
        itemsPojo.setCostingPrice(editTextCostingPrice.getText().toString());
        itemsPojo.setPurchasePrice(editTextPurchasePrice.getText().toString());
        itemsPojo.setSellingPrice(editTextSellingPrice.getText().toString());
        itemsPojo.setTax(editTextTax.getText().toString());
        itemsPojo.setCentre(PreferencedData.getPrefDeliveryCentre(ItemEditActivity.this));
        itemsPojo.setCentreId(PreferencedData.getPrefDeliveryCentreId(ItemEditActivity.this));
        itemsPojo.setCentreAdminId(PreferencedData.getPrefDeliveryCentreId(ItemEditActivity.this));

    }

    private boolean validate() {

        if(editTextItemName.getText().toString().equals(""))
        {
            editTextItemName.setError("Enter Name");
            editTextItemName.requestFocus();
            return false;
        }

        else if(editTextUnit.getText().toString().equals(""))
        {
            editTextUnit.setError("Enter Unit");
            editTextUnit.requestFocus();
            return false;
        }


        else if(editTextCostingPrice.getText().toString().equals(""))
        {
            editTextCostingPrice.setError("Enter Costing Price");
            editTextCostingPrice.requestFocus();
            return false;
        }

        else if(editTextTax.getText().toString().equals(""))
        {
            editTextTax.setError("Enter Tax");
            editTextTax.requestFocus();
            return false;
        }


        return true;
    }
}