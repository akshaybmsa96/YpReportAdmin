package yp.com.akki.ypreportadmin.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.activity.ItemEditActivity;
import yp.com.akki.ypreportadmin.pojo.allItems.Items;


/**
 * Created by akshaybmsa96 on 16/03/18.
 */

public class CustomAdapterItem extends RecyclerView.Adapter<CustomAdapterItem.ViewHolder>
{

    private Context context;
    private Activity activity;
    private ArrayList<Items> itemsPojo,filterList;
    private Items listItem;


    public CustomAdapterItem(Context context, ArrayList<Items> itemsPojo , Activity activity) {

        this.context = context;
        this.activity=activity;
        this.itemsPojo=itemsPojo;
        this.filterList=new ArrayList<>();
        this.filterList.addAll(this.itemsPojo);


    }


    @Override
    public CustomAdapterItem.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_list_view, parent, false);
        CustomAdapterItem.ViewHolder holder = new CustomAdapterItem.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(CustomAdapterItem.ViewHolder holder, int position)
    {

        listItem = filterList.get(position);
        holder.textViewName.setText(listItem.getItemName());



    }

    public void filter(String text) {
        filterList.clear();

        // If there is no search value, then add all original list items to filter list
        if (TextUtils.isEmpty(text)) {
            //       Toast.makeText(context,"No text",Toast.LENGTH_SHORT).show();

            filterList.addAll(itemsPojo);


        }

        else {

            //   Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
            // Iterate in the original List and add it to filter list...
            for (Items item : itemsPojo) {
                if (item.getItemName().toLowerCase().contains(text.toLowerCase())    ) {

                    // Adding Matched items
                    filterList.add(item);
                }
            }
        }

        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView textViewName;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            textViewName=(TextView)view.findViewById(R.id.textViewName);

        }


        @Override
        public void onClick(View view) {

            //  Toast.makeText(context,vendorPojo.get(getPosition()).get_id(),Toast.LENGTH_SHORT).show();

           Intent intent = new Intent(context, ItemEditActivity.class);
            intent.putExtra("data",new Gson().toJson(filterList.get(getPosition())));
            context.startActivity(intent);
        }
    }


    @Override
    public int getItemCount() {

        return filterList.size();
    }


}
