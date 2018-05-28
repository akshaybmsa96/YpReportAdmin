package yp.com.akki.ypreportadmin.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.pojo.accounts.AccountPojo;


/**
 * Created by akshaybmsa96 on 04/03/18.
 */

public class CustomAdapterAccountStatus extends RecyclerView.Adapter<CustomAdapterAccountStatus.ViewHolder>
{

    private Context context;
    private Activity activity;
    private ArrayList<AccountPojo> accountPojo;


    public CustomAdapterAccountStatus(Context context, ArrayList<AccountPojo> accountPojo, Activity activity) {

        this.context = context;
        this.activity=activity;
        this.accountPojo=accountPojo;


    }


    @Override
    public CustomAdapterAccountStatus.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.account_list_layout, parent, false);
        CustomAdapterAccountStatus.ViewHolder holder = new CustomAdapterAccountStatus.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(CustomAdapterAccountStatus.ViewHolder holder, final int position)
    {

        holder.textViewName.setText(accountPojo.get(position).getAccountName());
        holder.textViewType.setText(accountPojo.get(position).getType());
        holder.textViewAmount.setText("₹ "+accountPojo.get(position).getCurrentBalance());

    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView textViewName,textViewType,textViewAmount;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            textViewName=(TextView)view.findViewById(R.id.textViewName);
            textViewType=(TextView)view.findViewById(R.id.textViewType);
            textViewAmount=(TextView)view.findViewById(R.id.textViewAmount);


        }


        @Override
        public void onClick(View view) {

            // Toast.makeText(context,employeePojo.get(getPosition()).getId(),Toast.LENGTH_SHORT).show();

/*
            Intent i = new Intent(context, EmployeeEditActivity.class);
            i.putExtra("data",new Gson().toJson(employeePojo.get(getPosition())));
            context.startActivity(i);

            */

        }
    }


    @Override
    public int getItemCount() {

        return accountPojo.size();
    }


}