package yp.com.akki.ypreportadmin.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.fragment.StockAddAssociatedItemFragment;
import yp.com.akki.ypreportadmin.global.PreferencedData;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.ApiClientGetItems;
import yp.com.akki.ypreportadmin.network.ApiClientUpdateStock;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.allItems.ItemsPojo;
import yp.com.akki.ypreportadmin.pojo.stock.StockAssociatedItemPojo;
import yp.com.akki.ypreportadmin.pojo.stock.StockPojo;

public class StockEditActivity extends AppCompatActivity implements StockAddAssociatedItemFragment.OnCompleteListener {

    private AutoCompleteTextView autoCompleteTextViewItemName;
    private EditText editTextUnit,editTextLowerLimit,editTextCurrentStatus,editTextUpperLimit,editTextAssociatedItem;
    private String intentData;
    private Button buttonEditItem;
    private StockPojo stockPojo;
    private ApiClientUpdateStock apiClientUpdateStock;
    private ArrayList<StockAssociatedItemPojo> stockAssociatedItemPojo = new ArrayList<>();
    private ItemsPojo itempojo;
    private ApiClientGetItems apiClientGetItems;
    private Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_edit);

        autoCompleteTextViewItemName=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextViewItemName);
        editTextUnit=(EditText)findViewById(R.id.editTextUnit);
        editTextLowerLimit=(EditText)findViewById(R.id.editTextLowerLimit);
        editTextUpperLimit=(EditText)findViewById(R.id.editTextUpperLimit);
        editTextCurrentStatus=(EditText)findViewById(R.id.editTextCurrentStatus);
        editTextAssociatedItem = (EditText) findViewById(R.id.editTextAssociatedItem);
        buttonEditItem=(Button)findViewById(R.id.buttonEditItem);

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


        if(NetworkCheck.isNetworkAvailable(this))
        {
            getItems();
        }
        else {
            Toast.makeText(this,"Network Unavailable",Toast.LENGTH_SHORT).show();
        }


        intentData=getIntent().getStringExtra("data");
        setFields();


        buttonEditItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate())
                {
                    setpojo();


                    AlertDialog.Builder builder = new AlertDialog.Builder(StockEditActivity.this);
                    builder.setTitle("EDIT " + autoCompleteTextViewItemName.getText().toString() + " ?");
                    builder.setMessage("Are You Sure?");
                    builder.setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            if(NetworkCheck.isNetworkAvailable(StockEditActivity.this)) {

                                editStock();

                            }

                            else {

                                Toast.makeText(StockEditActivity.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }

            }
        });


        editTextAssociatedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(itempojo!=null&&itempojo.getItems().size()>0) {

                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    android.support.v4.app.Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                    if (prev != null) {
                        fragmentTransaction.remove(prev);
                    }
                    fragmentTransaction.addToBackStack(null);

                    // Create and show the dialog.
                    Gson gson = new Gson();
                    android.support.v4.app.DialogFragment newFragment = StockAddAssociatedItemFragment.newInstance(gson.toJson(itempojo), stockAssociatedItemPojo);
                    newFragment.show(fragmentTransaction, "addAssocaiatedItemForEdit");

                }

                else {

                    Toast.makeText(StockEditActivity.this,"Items not Found....Relaod Tab",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void editStock() {

        final ProgressDialog pDialog = new ProgressDialog(StockEditActivity.this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        System.out.println(new Gson().toJson(stockPojo));


        // show it
        apiClientUpdateStock = ApiClientBase.getApiClient().create(ApiClientUpdateStock.class);
        Call<String> call= apiClientUpdateStock.updateStock(new Gson().toJson(stockPojo));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String i=response.body();
                //  Toast.makeText(getActivity(),i+"",Toast.LENGTH_SHORT).show();

                if(i!=null&&i.equals("1"))
                {

                    Toast.makeText(StockEditActivity.this,"Item Updated",Toast.LENGTH_SHORT).show();
                    Intent in =new Intent(StockEditActivity.this,Dashboard.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(in);

                }

                else {
                    Toast.makeText(StockEditActivity.this,"Cannot update",Toast.LENGTH_SHORT).show();
                }
                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pDialog.hide();


                Toast.makeText(StockEditActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });


    }

    private void setpojo() {

        stockPojo.setItemName(autoCompleteTextViewItemName.getText().toString());
        stockPojo.setAssociatedItem(stockAssociatedItemPojo);
        stockPojo.setLowerLimit(editTextLowerLimit.getText().toString());
        stockPojo.setUpperLimit(editTextUpperLimit.getText().toString());
        stockPojo.setUnit(editTextUnit.getText().toString());
        stockPojo.setCurrentStatus(editTextCurrentStatus.getText().toString());
        stockPojo.setCentreId(PreferencedData.getPrefDeliveryCentreId(this));
    }

    private boolean validate() {

        if(autoCompleteTextViewItemName.getText().toString().equals("")){

            autoCompleteTextViewItemName.setError("Enter Item");
            autoCompleteTextViewItemName.requestFocus();
            return false;
        }

        else if(editTextAssociatedItem.getText().toString().equals(""))
        {
            editTextAssociatedItem.setError("Select Item");
            editTextAssociatedItem.requestFocus();
            return false;

        }



        else if(editTextLowerLimit.getText().toString().equals(""))
        {
            editTextLowerLimit.setError("Enter Limit");
            editTextLowerLimit.requestFocus();
            return false;
        }

        else if(editTextUpperLimit.getText().toString().equals(""))
        {
            editTextUpperLimit.setError("Enter Limit");
            editTextUpperLimit.requestFocus();
            return false;
        }

        else if(editTextCurrentStatus.getText().toString().equals(""))
        {
            editTextCurrentStatus.setError("Enter Current Status");
            editTextCurrentStatus.requestFocus();
            return false;
        }


        return true;

    }


    private void setFields() {

        stockPojo=new Gson().fromJson(intentData,StockPojo.class);

        autoCompleteTextViewItemName.setText(stockPojo.getItemName());
        editTextUnit.setText(stockPojo.getUnit());
        editTextLowerLimit.setText(stockPojo.getLowerLimit());
        editTextUpperLimit.setText(stockPojo.getUpperLimit());
        editTextCurrentStatus.setText(stockPojo.getCurrentStatus());

        stockAssociatedItemPojo.addAll(stockPojo.getAssociatedItem());

        setAssociatedItems();

    }


    private void setAssociatedItems() {

            editTextAssociatedItem.setText("");

            for(int i =0 ;i<stockAssociatedItemPojo.size();i++) {

                editTextAssociatedItem.append(stockAssociatedItemPojo.get(i).getItemName() + " ");

            }

            if(stockAssociatedItemPojo.size()>0)
                editTextUnit.setText(stockAssociatedItemPojo.get(0).getItemUnit());

            else {
                editTextUnit.setText("");
            }

    }



    private void getItems() {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Retrieving Items...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientGetItems = ApiClientBase.getApiClient().create(ApiClientGetItems.class);
        String url="items/centreId="+ PreferencedData.getPrefDeliveryCentreId(this)+"&centreAdminId="+PreferencedData.getPrefDeliveryCentreId(this);
        Call<ItemsPojo> call= apiClientGetItems.getInfo(url);
        call.enqueue(new Callback<ItemsPojo>() {
            @Override
            public void onResponse(Call<ItemsPojo> call, Response<ItemsPojo> response) {


                itempojo=response.body();
                if(itempojo!=null&&itempojo.getItems().size()>0) {


                    /*

                    for (int i = 0; i < itempojo.getItems().size(); i++) {
                        item.add(itempojo.getItems().get(i).getItemName());

                    }


                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, item);

                    autoCompleteTextViewItemName.setAdapter(adapter);
                    autoCompleteTextViewItemName.setThreshold(1);

                    */

                    //    Toast.makeText(getContext(),new Gson().toJson(item),Toast.LENGTH_SHORT).show();


                }

                else {
                    Toast.makeText(StockEditActivity.this,"No Items Available",Toast.LENGTH_SHORT).show();
                }

                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ItemsPojo> call, Throwable t) {
                pDialog.dismiss();


                Toast.makeText(StockEditActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });

    }

    @Override
    public void onComplete() {

        setAssociatedItems();

    }
}
