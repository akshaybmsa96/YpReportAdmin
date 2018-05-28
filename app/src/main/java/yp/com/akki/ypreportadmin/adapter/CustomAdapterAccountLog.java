package yp.com.akki.ypreportadmin.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;

import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.pojo.accounts.AccountLogPojo;


/**
 * Created by akshaybmsa96 on 06/03/18.
 */

public class CustomAdapterAccountLog extends RecyclerView.Adapter<CustomAdapterAccountLog.ViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder>
{

    private Context context;
    private Activity activity;
    private ArrayList<AccountLogPojo> accountLogPojo;


    public CustomAdapterAccountLog(Context context, ArrayList<AccountLogPojo> accountLogPojo, Activity activity) {

        this.context = context;
        this.activity=activity;
        this.accountLogPojo=accountLogPojo;


    }


    @Override
    public CustomAdapterAccountLog.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.account_log_list_layout, parent, false);
        CustomAdapterAccountLog.ViewHolder holder = new CustomAdapterAccountLog.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(CustomAdapterAccountLog.ViewHolder holder, int position)
    {

        holder.textViewAmount.setText("₹ " + accountLogPojo.get(position).getAmount());
        holder.textViewFromAc.setText("From : "+accountLogPojo.get(position).getFromAc());
        holder.textViewToAc.setText("To : "+accountLogPojo.get(position).getToAc());


    }


    class ViewHolder extends RecyclerView.ViewHolder {


        TextView textViewAmount,textViewFromAc,textViewToAc;


        public ViewHolder(View view) {
            super(view);
            textViewAmount=(TextView)view.findViewById(R.id.textViewAmount);
            textViewFromAc=(TextView)view.findViewById(R.id.textViewFromAc);
            textViewToAc=(TextView)view.findViewById(R.id.textViewToAc);

        }

    }


    @Override
    public long getHeaderId(int position) {
        return getId(accountLogPojo.get(position).getDate());
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_header_date, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

        TextView textViewDate = (TextView) holder.itemView.findViewById(R.id.textViewDate);
        textViewDate.setText(accountLogPojo.get(position).getDate());



    }

    @Override
    public int getItemCount() {

        return accountLogPojo.size();
    }


    Long getId(String input)
    {
        Long id=0l;
        input=input.replace("-","");
        input=input.replace(":","");
        input=input.replace(" ","");
        //  System.out.println("Input is                                                     "+input);
        id=Long.parseLong(input);
        return id;
    }



}