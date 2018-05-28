package yp.com.akki.ypreportadmin.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.activity.ItemEditActivity;
import yp.com.akki.ypreportadmin.pojo.MaterialDistributionPojo.MaterialDistributionPojo;
import yp.com.akki.ypreportadmin.pojo.allItems.Items;
import yp.com.akki.ypreportadmin.pojo.stock.StockAssociatedItemPojo;

/**
 * Created by akshaybmsa96 on 03/04/18.
 */

public class CustomAdapterAssociatedItemAdd extends ArrayAdapter<Items>
{

    private Context context;
    private Activity activity;
    private ArrayList<Items> itemsPojo,filterList;
    private Items listItem;
    private ArrayList<StockAssociatedItemPojo> associatedItem;
    private CheckBox checkBox;
    private StockAssociatedItemPojo temp;



    public CustomAdapterAssociatedItemAdd(Context context, ArrayList<Items> itemsPojo , Activity activity ,ArrayList<StockAssociatedItemPojo> associatedItem) {
        super(context, R.layout.stock_associated_item_list_layout,itemsPojo);
        this.context = context;
        this.activity=activity;
        this.itemsPojo=itemsPojo;
        this.associatedItem=associatedItem;
        this.filterList=new ArrayList<>();
        this.filterList.addAll(this.itemsPojo);
    }




    public View getView(final int position, View view, ViewGroup parent)

    {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.stock_associated_item_list_layout, null, true);
        final TextView textViewItemName=(TextView)rowView.findViewById(R.id.textViewItemName);
        checkBox=(CheckBox) rowView.findViewById(R.id.checkbox);

        listItem = filterList.get(position);


        textViewItemName.setText(listItem.getItemName());

        for (int j =0 ; j<associatedItem.size();j++)
        {
            if(listItem.get_id().equals(associatedItem.get(j).getItemId()))
            {
                checkBox.setChecked(true);
            }
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {


                if(isChecked)
                {

                    temp = new StockAssociatedItemPojo();
                    temp.setItemName(filterList.get(position).getItemName().toString());
                    temp.setItemId(filterList.get(position).get_id());
                    temp.setItemUnit(filterList.get(position).getUnit());
                        associatedItem.add(temp);


                //    Toast.makeText(context,new Gson().toJson(associatedItem),Toast.LENGTH_SHORT).show();
                }

                else if(!isChecked) {
                    for(int i =0;i<associatedItem.size();i++) {
                        if(filterList.get(position).get_id().equals(associatedItem.get(i).getItemId())) {
                            associatedItem.remove(i);
                        }
                    }

                //    Toast.makeText(context,new Gson().toJson(associatedItem),Toast.LENGTH_SHORT).show();

                }

            }
        });



        return rowView;
    }

    @Override
    public int getCount() {
        return filterList.size();
    }


    @Nullable
    @Override
    public Items getItem(int position) {
        return filterList.get(position);
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
            for (int i =0 ; i< itemsPojo.size() ; i++) {
                if (itemsPojo.get(i).getItemName().toLowerCase().contains(text.toLowerCase())) {

                    // Adding Matched items
                    filterList.add(itemsPojo.get(i));
                }

                for (int j =0 ; j<associatedItem.size();j++)
                {
                    if(itemsPojo.get(i).get_id().equals(associatedItem.get(j).getItemId()))
                    {
                        if(!filterList.contains(itemsPojo.get(i))) {
                            filterList.add(itemsPojo.get(i));
                        }
                    }
                }
            }


        }

        notifyDataSetChanged();
      //    Toast.makeText(context,new Gson().toJson(associatedItem),Toast.LENGTH_SHORT).show();
    }
}