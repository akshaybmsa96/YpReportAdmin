package yp.com.akki.ypreportadmin.fragment;


import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.global.PreferencedData;
import yp.com.akki.ypreportadmin.network.ApiClientAddItem;
import yp.com.akki.ypreportadmin.network.ApiClientAddVendor;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.allItems.Items;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemAddFragment extends Fragment {

    private EditText editTextItemName,editTextUnit,editTextPurchasePrice,editTextSellingPrice,editTextCostingPrice,editTextTax;
    private Button buttonAddItem;
    private Items itemsPojo;
    private ApiClientAddItem apiClientAddItem;


    public ItemAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_add, container, false);

        buttonAddItem=(Button)view.findViewById(R.id.buttonAddItem);
        editTextItemName=(EditText)view.findViewById(R.id.editTextItemName);
        editTextUnit=(EditText)view.findViewById(R.id.editTextUnit);
        editTextPurchasePrice=(EditText)view.findViewById(R.id.editTextPurchasePrice);
        editTextSellingPrice=(EditText)view.findViewById(R.id.editTextSellingPrice);
        editTextCostingPrice=(EditText)view.findViewById(R.id.editTextCostingPrice);
        editTextTax=(EditText)view.findViewById(R.id.editTextTax);

        itemsPojo=new Items();

        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate())
                {


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Add " + editTextItemName.getText().toString() + " ?");
                    builder.setMessage("Are You Sure?");
                    builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            if(NetworkCheck.isNetworkAvailable(getContext())) {

                                setPojo();
                                addItem();

                            }

                            else {

                                Toast.makeText(getActivity(), "Network Unavailable", Toast.LENGTH_SHORT).show();
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


        return view;
    }

    private void addItem() {


        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it

        System.out.println(new Gson().toJson(itemsPojo));


        apiClientAddItem = ApiClientBase.getApiClient().create(ApiClientAddItem.class);
        Call<String> call= apiClientAddItem.addItem(new Gson().toJson(itemsPojo));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String i=response.body();

                //   Toast.makeText(getActivity(),i+"",Toast.LENGTH_SHORT).show();

                if(i!=null&&i.equals("1"))
                {

                    Toast.makeText(getActivity(),"Item Added",Toast.LENGTH_SHORT).show();
                    clearFields();


                }

                else {
                    Toast.makeText(getActivity(),"Cannot Add",Toast.LENGTH_SHORT).show();
                }
                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pDialog.hide();


                Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });



    }

    private void clearFields() {

        editTextItemName.setText("");
        editTextUnit.setText("");
        editTextPurchasePrice.setText("");
        editTextSellingPrice.setText("");
        editTextCostingPrice.setText("");
        editTextTax.setText("");

    }

    private void setPojo() {

        itemsPojo.setUnit(editTextUnit.getText().toString());
        itemsPojo.setItemName(editTextItemName.getText().toString());
        itemsPojo.setPurchasePrice(editTextPurchasePrice.getText().toString());
        itemsPojo.setSellingPrice(editTextSellingPrice.getText().toString());
        itemsPojo.setCostingPrice(editTextCostingPrice.getText().toString());
        itemsPojo.setTax(editTextTax.getText().toString());
        itemsPojo.setCentre(PreferencedData.getPrefDeliveryCentre(getActivity()));
        itemsPojo.setCentreId(PreferencedData.getPrefDeliveryCentreId(getActivity()));
        itemsPojo.setCentreAdminId(PreferencedData.getPrefDeliveryCentreId(getActivity()));

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

        else if(editTextPurchasePrice.getText().toString().equals(""))
        {
            editTextPurchasePrice.setError("Enter Purchase Price");
            editTextPurchasePrice.requestFocus();
            return false;
        }

        else if(editTextSellingPrice.getText().toString().equals(""))
        {
            editTextSellingPrice.setError("Enter Selling Price");
            editTextSellingPrice.requestFocus();
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
