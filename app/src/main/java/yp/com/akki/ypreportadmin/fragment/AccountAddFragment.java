package yp.com.akki.ypreportadmin.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import yp.com.akki.ypreportadmin.network.ApiClientAddAccount;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.accounts.AccountPojo;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountAddFragment extends Fragment {

    private EditText editTextName,editTextBalance;
    private AutoCompleteTextView editTextAccountType;
    private Button buttonAddAcount;
    private ApiClientAddAccount apiClientAddAccount;
    private AccountPojo accountPojo;


    public AccountAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_add, container, false);

        editTextName=(EditText)view.findViewById(R.id.editTextName);
        editTextAccountType=(AutoCompleteTextView) view.findViewById(R.id.editTextAccountType);
        editTextBalance=(EditText)view.findViewById(R.id.editTextBalance);

        buttonAddAcount=(Button)view.findViewById(R.id.buttonAddAcount);



        buttonAddAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate())
                {
                    if(NetworkCheck.isNetworkAvailable(getContext()))
                    {
                        setPojo();
                        addAccount();
                    }

                    else
                    {
                        Toast.makeText(getContext(),"Network Unavailable",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });


        return view;
    }

    private void setPojo() {

        accountPojo = new AccountPojo();
        accountPojo.setAccountName(editTextName.getText().toString());
        accountPojo.setType(editTextAccountType.getText().toString());
        accountPojo.setCurrentBalance(editTextBalance.getText().toString());
        accountPojo.setCentre(PreferencedData.getPrefDeliveryCentre(getContext()));
        accountPojo.setCentreId(PreferencedData.getPrefDeliveryCentreId(getActivity()));

    }

    private void clearFields() {

        editTextName.setText("");
        editTextAccountType.setText("");
        editTextBalance.setText("");


    }

    private void addAccount() {


        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientAddAccount = ApiClientBase.getApiClient().create(ApiClientAddAccount.class);
        Call<String> call= apiClientAddAccount.addAccount(new Gson().toJson(accountPojo));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String i=response.body();
                //  Toast.makeText(getActivity(),i+"",Toast.LENGTH_SHORT).show();

                if(i!=null&&i.equals("1"))
                {

                    Toast.makeText(getActivity(),"Account Added",Toast.LENGTH_SHORT).show();
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

    private boolean validate() {


        if(editTextName.getText().toString().equals(""))
        {
            editTextName.setError("Enter a name");
            editTextName.requestFocus();

            return false;
        }

        else if(editTextAccountType.getText().toString().equals(""))
        {
            editTextAccountType.setError("Enter type");
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

}
