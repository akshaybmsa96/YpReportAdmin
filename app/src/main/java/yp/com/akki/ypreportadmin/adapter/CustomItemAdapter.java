package yp.com.akki.ypreportadmin.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.pojo.MaterialDistributionPojo.MaterialDistributionPojo;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Akki on 03-06-2017.
 */


public class CustomItemAdapter extends ArrayAdapter<MaterialDistributionPojo> {

    private Context context;
    private Activity activity;
    private ArrayList<MaterialDistributionPojo> items;
    private TextView textViewTotalAmount;

    public CustomItemAdapter(Context context, Activity activity, ArrayList<MaterialDistributionPojo> items,TextView textViewTotalAmount ) {
        super(context, R.layout.item_list_view_layout,items);
        this.context = context;
        this.activity=activity;
        this.items=items;
        this.textViewTotalAmount=textViewTotalAmount;
    }


    public View getView(final int position, View view, ViewGroup parent)

    {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.item_list_view_layout, null, true);
        final TextView textViewItemName=(TextView)rowView.findViewById(R.id.textViewItemName);
        TextView textViewQty=(TextView) rowView.findViewById(R.id.textViewQty);
        TextView textViewTotal=(TextView) rowView.findViewById(R.id.textViewTotal);
        Button buttonDeleteItem=(Button)rowView.findViewById(R.id.buttonDeleteItem);


        textViewItemName.setText(items.get(position).getItemName());
        textViewQty.setText(items.get(position).getQty()+" "+items.get(position).getUnit());
        textViewTotal.setText("₹ "+items.get(position).getTotalItemCost());

        buttonDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete " + items.get(position).getItemName()+" ?");
                builder.setMessage("Are You Sure?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        items.remove(position);
                        notifyDataSetChanged();
                        textViewTotalAmount.setText("Amount : ₹ " + totalAmount());

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
        });

        return rowView;
    }

    private String totalAmount()
    {
        Double total=0.0;

        for(int i=0;i<items.size();i++)
        {
            total=total+Double.parseDouble(items.get(i).getTotalItemCost());
        }

        return String.valueOf(total);
    }

}

