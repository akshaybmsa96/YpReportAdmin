package yp.com.akki.ypreportadmin.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.activity.ItemEditActivity;
import yp.com.akki.ypreportadmin.activity.StockEditActivity;
import yp.com.akki.ypreportadmin.pojo.allItems.Items;
import yp.com.akki.ypreportadmin.pojo.stock.StockPojo;

/**
 * Created by akshaybmsa96 on 02/04/18.
 */

public class CustomAdapterStock extends RecyclerView.Adapter<CustomAdapterStock.ViewHolder>
{

    private Context context;
    private Activity activity;
    private ArrayList<StockPojo> stockPojo,filterList;
    private StockPojo listItem;;


    public CustomAdapterStock(Context context, ArrayList<StockPojo> stockPojo , Activity activity) {

        this.context = context;
        this.activity=activity;
        this.stockPojo=stockPojo;
        this.filterList=new ArrayList<>();
        this.filterList.addAll(this.stockPojo);


    }


    @Override
    public CustomAdapterStock.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.stock_status_layout, parent, false);
        CustomAdapterStock.ViewHolder holder = new CustomAdapterStock.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(CustomAdapterStock.ViewHolder holder, int position)
    {

        listItem = filterList.get(position);

        holder.itemView.setElevation(1);
        holder.itemView.setPadding(10,10,10,10);

        holder.textViewItemName.setPadding(5,5,5,5);
        holder.textViewItemName.setTextSize(16);
        holder.textViewItemName.setText(listItem.getItemName());

        holder.textViewCurrentStatus.setVisibility(View.GONE);
        holder.textViewPercentage.setVisibility(View.GONE);

        holder.progressBar.setVisibility(View.GONE);

        holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));



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


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView textViewItemName,textViewPercentage,textViewCurrentStatus;
        ProgressBar progressBar;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            textViewItemName=(TextView)view.findViewById(R.id.textViewItemName);
            textViewPercentage=(TextView)view.findViewById(R.id.textViewPercentage);
            textViewCurrentStatus=(TextView)view.findViewById(R.id.textViewCurrentStatus);

            progressBar=(ProgressBar)view.findViewById(R.id.progressBar);

        }

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(context, StockEditActivity.class);
            intent.putExtra("data",new Gson().toJson(filterList.get(getPosition())));
            context.startActivity(intent);

        }
    }


    @Override
    public int getItemCount() {

        return filterList.size();
    }


}