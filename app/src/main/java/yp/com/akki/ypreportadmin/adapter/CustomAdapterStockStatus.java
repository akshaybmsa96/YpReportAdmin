package yp.com.akki.ypreportadmin.adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.activity.ItemEditActivity;
import yp.com.akki.ypreportadmin.pojo.allItems.Items;
import yp.com.akki.ypreportadmin.pojo.stock.StockPojo;

/**
 * Created by akshaybmsa96 on 01/04/18.
 */

public class CustomAdapterStockStatus extends RecyclerView.Adapter<CustomAdapterStockStatus.ViewHolder>
{

    private Context context;
    private Activity activity;
    private ArrayList<StockPojo> stockPojo,filterList;
    private StockPojo listItem;


    public CustomAdapterStockStatus(Context context, ArrayList<StockPojo> stockPojo , Activity activity) {

        this.context = context;
        this.activity=activity;
        this.stockPojo=stockPojo;
        this.filterList=new ArrayList<>();
        this.filterList.addAll(this.stockPojo);


    }


    @Override
    public CustomAdapterStockStatus.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.stock_status_layout, parent, false);
        CustomAdapterStockStatus.ViewHolder holder = new CustomAdapterStockStatus.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(CustomAdapterStockStatus.ViewHolder holder, int position)
    {

        listItem = filterList.get(position);

        holder.textViewItemName.setText(listItem.getItemName());

        holder.textViewCurrentStatus.setText(String.format("%.1f",Double.parseDouble(listItem.getCurrentStatus()) )+ " "+listItem.getUnit()+" Available");

      //  String.format("%.2f", d);

        Double percentage = Double.parseDouble(listItem.getCurrentStatus())/Double.parseDouble(listItem.getUpperLimit())*100;
        holder.textViewPercentage.setText(Math.round(percentage)+" %");

      //  holder.progressBar.setProgress((percentage.intValue()));

        if(android.os.Build.VERSION.SDK_INT >= 11){
            // will update the "progress" propriety of seekbar until it reaches progress
            ObjectAnimator animation = ObjectAnimator.ofInt(holder.progressBar, "progress", 0 ,percentage.intValue());
            animation.setDuration(900); // 0.9 second
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();
        }

        else
        {
            holder.progressBar.setProgress((percentage.intValue()));
        }


        if(Double.parseDouble(listItem.getCurrentStatus()) < Double.parseDouble(listItem.getLowerLimit()))
        {
            holder.progressBar.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.Red)));
        }

        else{
            holder.progressBar.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green)));
        }



    }


    class ViewHolder extends RecyclerView.ViewHolder {


        TextView textViewItemName,textViewPercentage,textViewCurrentStatus;
        ProgressBar progressBar;


        public ViewHolder(View view) {
            super(view);

            textViewItemName=(TextView)view.findViewById(R.id.textViewItemName);
            textViewPercentage=(TextView)view.findViewById(R.id.textViewPercentage);
            textViewCurrentStatus=(TextView)view.findViewById(R.id.textViewCurrentStatus);

            progressBar=(ProgressBar)view.findViewById(R.id.progressBar);

        }

    }


    public void filter(String text) {
        filterList.clear();

        // If there is no search value, then add all original list items to filter list
        if (TextUtils.isEmpty(text)) {
            //       Toast.makeText(context,"No text",Toast.LENGTH_SHORT).show();

            filterList.addAll(stockPojo);


        }

        else {

            //   Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
            // Iterate in the original List and add it to filter list...
            for (StockPojo item : stockPojo) {
                if (item.getItemName().toLowerCase().contains(text.toLowerCase())    ) {

                    // Adding Matched items
                    filterList.add(item);
                }
            }
        }

        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {

        return filterList.size();
    }


}