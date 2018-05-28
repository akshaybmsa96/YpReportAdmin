package yp.com.akki.ypreportadmin.fragment;


import android.app.ProgressDialog;
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
import yp.com.akki.ypreportadmin.network.ApiClientAddVendor;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.vendor.VendorPojo;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendorAddFragment extends Fragment {

    private EditText editTextVendorName,editTextAddress,editTextGSTNumber,editTextPhoneNumber,editTextLimit,editTextBalance;
    private Button buttonAddVendor;
    private VendorPojo vendorPojo;
    private ApiClientAddVendor apiClientAddVendor;


    public VendorAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_vendor_add, container, false);

        buttonAddVendor=(Button)view.findViewById(R.id.buttonAddVendor);
        editTextVendorName=(EditText)view.findViewById(R.id.editTextName);
        editTextAddress=(EditText)view.findViewById(R.id.editTextAddress);
        editTextGSTNumber=(EditText)view.findViewById(R.id.editTextGSTNumber);
        editTextLimit=(EditText)view.findViewById(R.id.editTextLimit);
        editTextPhoneNumber=(EditText)view.findViewById(R.id.editTextPhoneNumber);
        editTextBalance=(EditText)view.findViewById(R.id.editTextBalance);

        vendorPojo=new VendorPojo();



        buttonAddVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(editTextVendorName.getText().toString().equals(""))
                {
                    editTextVendorName.setError("Enter Name");
                    editTextVendorName.requestFocus();
                }

                else if(editTextAddress.getText().toString().equals(""))
                {
                    editTextAddress.setError("Enter Address");
                    editTextAddress.requestFocus();
                }

                else if(editTextPhoneNumber.getText().toString().length()<10)
                {
                    editTextPhoneNumber.setError("Enter Valid Number");
                    editTextPhoneNumber.requestFocus();
                }

                else if(editTextGSTNumber.getText().toString().equals(""))
                {
                    editTextGSTNumber.setError("Enter GST Number");
                    editTextGSTNumber.requestFocus();
                }

                else if(editTextLimit.getText().toString().equals(""))
                {
                    editTextLimit.setError("Enter Limit");
                    editTextLimit.requestFocus();
                }

                else {

                    if(NetworkCheck.isNetworkAvailable(getActivity()))
                    {
                        vendorPojo.setVendorName(editTextVendorName.getText().toString());
                        vendorPojo.setAddress(editTextAddress.getText().toString());
                        vendorPojo.setGstNumber(editTextGSTNumber.getText().toString());
                        vendorPojo.setCreditLimit(editTextLimit.getText().toString());
                        vendorPojo.setPhoneNumber(editTextPhoneNumber.getText().toString());
                        vendorPojo.setCentre(PreferencedData.getPrefDeliveryCentre(getActivity()));
                        vendorPojo.setCentreId(PreferencedData.getPrefDeliveryCentreId(getActivity()));

                        if(!editTextBalance.getText().toString().equals(""))
                            vendorPojo.setCurrentBalance(editTextBalance.getText().toString());

                        else
                            vendorPojo.setCurrentBalance("0");

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Add " + editTextVendorName.getText().toString() + " ?");
                        builder.setMessage("Are You Sure?");
                        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                if(NetworkCheck.isNetworkAvailable(getContext())) {

                                    addVendor();

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

            }
        });



        return view;
    }

    private void addVendor() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientAddVendor = ApiClientBase.getApiClient().create(ApiClientAddVendor.class);
        Call<String> call= apiClientAddVendor.addVendor(new Gson().toJson(vendorPojo));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String i=response.body();

                //   Toast.makeText(getActivity(),i+"",Toast.LENGTH_SHORT).show();

                if(i!=null&&i.equals("1"))
                {

                    Toast.makeText(getActivity(),"Vendor Added",Toast.LENGTH_SHORT).show();
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

        editTextVendorName.setText("");
        editTextAddress.setText("");
        editTextGSTNumber.setText("");
        editTextLimit.setText("");
        editTextPhoneNumber.setText("");
        editTextBalance.setText("");

    }

}
