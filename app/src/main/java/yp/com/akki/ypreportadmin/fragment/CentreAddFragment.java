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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.global.PreferencedData;
import yp.com.akki.ypreportadmin.network.ApiClientAddStore;
import yp.com.akki.ypreportadmin.network.ApiClientAddVendor;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.store.StorePojo;

/**
 * A simple {@link Fragment} subclass.
 */
public class CentreAddFragment extends Fragment {

    private EditText editTextStoreName,editTextStoreCode,editTextGSTNumber,editTextPhoneNumber,editTextLimit,editTextBalance;
    private Button buttonAddStore;
    private StorePojo storePojo;
    private Spinner spinnerStoreType;
    private ApiClientAddStore apiClientAddStore;


    public CentreAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_centre_add, container, false);


        buttonAddStore=(Button)view.findViewById(R.id.buttonAddStore);
        editTextStoreName=(EditText)view.findViewById(R.id.editTextStoreName);
        editTextStoreCode=(EditText)view.findViewById(R.id.editTextStoreCode);
        editTextGSTNumber=(EditText)view.findViewById(R.id.editTextGSTNumber);
        editTextLimit=(EditText)view.findViewById(R.id.editTextLimit);
        editTextPhoneNumber=(EditText)view.findViewById(R.id.editTextPhoneNumber);
        editTextBalance=(EditText)view.findViewById(R.id.editTextBalance);

        spinnerStoreType=(Spinner) view.findViewById(R.id.spinnerStoreType);

        storePojo=new StorePojo();



        buttonAddStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(editTextStoreName.getText().toString().equals(""))
                {
                    editTextStoreName.setError("Enter Store Name");
                    editTextStoreName.requestFocus();
                }

                else if(editTextStoreCode.getText().toString().equals(""))
                {
                    editTextStoreCode.setError("Enter Store Code");
                    editTextStoreCode.requestFocus();
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
                        storePojo.setCentre(editTextStoreName.getText().toString());
                        storePojo.setCentreCode(editTextStoreCode.getText().toString());
                        storePojo.setGstNumber(editTextGSTNumber.getText().toString());
                        storePojo.setCreditLimit(editTextLimit.getText().toString());
                        storePojo.setPhoneNumber(editTextPhoneNumber.getText().toString());
                        storePojo.setAdminId(PreferencedData.getPrefDeliveryCentreId(getActivity()));
                        storePojo.setType(spinnerStoreType.getSelectedItem().toString());

                        if(!editTextBalance.getText().toString().equals(""))
                            storePojo.setCurrentBalance(editTextBalance.getText().toString());

                        else
                            storePojo.setCurrentBalance("0");



                        //

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Add " + editTextStoreName.getText().toString() + " ?");
                        builder.setMessage("Are You Sure?");
                        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                if(NetworkCheck.isNetworkAvailable(getContext())) {

                                    addCentre();

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

                        //



                    }
                }

            }
        });

        return view;

    }

    private void addCentre() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it

        System.out.println(new Gson().toJson(storePojo));

        apiClientAddStore = ApiClientBase.getApiClient().create(ApiClientAddStore.class);
        Call<String> call= apiClientAddStore.addStore(new Gson().toJson(storePojo));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String i=response.body();

                //   Toast.makeText(getActivity(),i+"",Toast.LENGTH_SHORT).show();

                if(i!=null&&i.equals("1"))
                {

                    Toast.makeText(getActivity(),"Store Added",Toast.LENGTH_SHORT).show();
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

        editTextStoreName.setText("");
        editTextStoreCode.setText("");
        editTextGSTNumber.setText("");
        editTextLimit.setText("");
        editTextPhoneNumber.setText("");
        editTextBalance.setText("");

    }

}
