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
import yp.com.akki.ypreportadmin.pojo.history.ItemUsagePojo;
import yp.com.akki.ypreportadmin.pojo.reportDetailPojo.ReportDetailItems;

/**
 * Created by akshaybmsa96 on 21/01/18.
 */

public class CustomDetailAdapter extends RecyclerView.Adapter<CustomDetailAdapter.ViewHolder>
{

    private Context context;
    private Activity activity;
    private ArrayList<ItemUsagePojo> itemUsagePojo;


    public CustomDetailAdapter(Context context, ArrayList<ItemUsagePojo> itemUsagePojo, Activity activity) {

        this.context = context;
        this.activity=activity;
        this.itemUsagePojo=itemUsagePojo;


    }


    @Override
    public CustomDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_detail_list_layout, parent, false);
        CustomDetailAdapter.ViewHolder holder = new CustomDetailAdapter.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(CustomDetailAdapter.ViewHolder holder, int position)
    {

        holder.textViewItemName.setText(itemUsagePojo.get(position).get_id().getItemName());
        holder.textViewQty.setText(itemUsagePojo.get(position).getQty()+" "+ itemUsagePojo.get(position).get_id().getUnit());
        holder.textViewTotalCost.setText("₹ "+itemUsagePojo.get(position).getTotalItemCost());


    }


    class ViewHolder extends RecyclerView.ViewHolder {


        TextView textViewItemName,textViewQty,textViewTotalCost;


        public ViewHolder(View view) {
            super(view);
            textViewItemName=(TextView)view.findViewById(R.id.textViewItemName);
            textViewQty=(TextView)view.findViewById(R.id.textViewQty);
            textViewTotalCost=(TextView)view.findViewById(R.id.textViewTotalCost);

        }

    }


    @Override
    public int getItemCount() {

        return itemUsagePojo.size();
    }


}
