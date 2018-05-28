package yp.com.akki.ypreportadmin.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.activity.AccountEditActivity;
import yp.com.akki.ypreportadmin.pojo.accounts.AccountPojo;

/**
 * Created by akshaybmsa96 on 04/03/18.
 */

public class CustomAdapterAccountEdit extends RecyclerView.Adapter<CustomAdapterAccountEdit.ViewHolder>
{

    private Context context;
    private Activity activity;
    private ArrayList<AccountPojo> accountPojo;


    public CustomAdapterAccountEdit(Context context, ArrayList<AccountPojo> accountPojo, Activity activity) {

        this.context = context;
        this.activity=activity;
        this.accountPojo=accountPojo;


    }


    @Override
    public CustomAdapterAccountEdit.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.account_list_layout, parent, false);
        CustomAdapterAccountEdit.ViewHolder holder = new CustomAdapterAccountEdit.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(CustomAdapterAccountEdit.ViewHolder holder, final int position)
    {

        holder.textViewName.setText(accountPojo.get(position).getAccountName());

        holder.textViewName.setTextSize(16);

        holder.textViewType.setVisibility(View.GONE);
        holder.textViewAmount.setVisibility(View.GONE);

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

          //   Toast.makeText(context,accountPojo.get(getPosition()).get_id(),Toast.LENGTH_SHORT).show();


            Intent i = new Intent(context, AccountEditActivity.class);
            i.putExtra("data",new Gson().toJson(accountPojo.get(getPosition())));
            context.startActivity(i);



        }
    }


    @Override
    public int getItemCount() {

        return accountPojo.size();
    }


}