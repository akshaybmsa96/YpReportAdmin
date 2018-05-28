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
import yp.com.akki.ypreportadmin.activity.VendorEditActivity;
import yp.com.akki.ypreportadmin.pojo.vendor.VendorPojo;

/**
 * Created by akshaybmsa96 on 20/02/18.
 */

public class CustomAdapterVendors extends RecyclerView.Adapter<CustomAdapterVendors.ViewHolder>
{

    private Context context;
    private Activity activity;
    private ArrayList<VendorPojo> vendorPojo;


    public CustomAdapterVendors(Context context, ArrayList<VendorPojo> vendorPojo, Activity activity) {

        this.context = context;
        this.activity=activity;
        this.vendorPojo=vendorPojo;


    }


    @Override
    public CustomAdapterVendors.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.vendor_list_layout, parent, false);
        CustomAdapterVendors.ViewHolder holder = new CustomAdapterVendors.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(CustomAdapterVendors.ViewHolder holder, int position)
    {

        holder.textViewName.setText(vendorPojo.get(position).getVendorName());
        holder.textViewAddress.setText(vendorPojo.get(position).getAddress());





    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView textViewName,textViewAddress;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            textViewName=(TextView)view.findViewById(R.id.textViewName);
            textViewAddress=(TextView)view.findViewById(R.id.textViewAddress);

        }


        @Override
        public void onClick(View view) {

          //  Toast.makeText(context,vendorPojo.get(getPosition()).get_id(),Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(context, VendorEditActivity.class);
            intent.putExtra("data",new Gson().toJson(vendorPojo.get(getPosition())));
            context.startActivity(intent);
        }
    }


    @Override
    public int getItemCount() {

        return vendorPojo.size();
    }


}
