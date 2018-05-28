package yp.com.akki.ypreportadmin.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.pojo.employee.EmployeePojo;
import yp.com.akki.ypreportadmin.pojo.vendor.VendorPojo;

/**
 * Created by akshaybmsa96 on 26/02/18.
 */

public class CustomAdapterVendorStatus extends RecyclerView.Adapter<CustomAdapterVendorStatus.ViewHolder>
{

    private Context context;
    private Activity activity;
    private ArrayList<VendorPojo> vendorPojo,filterList;
    private VendorPojo listItem;
    private TextView textViewAmount;


    public CustomAdapterVendorStatus(Context context, ArrayList<VendorPojo> vendorPojo, Activity activity,TextView textViewAmount) {

        this.context = context;
        this.activity=activity;
        this.vendorPojo=vendorPojo;
        this.filterList=new ArrayList<>();
        this.filterList.addAll(this.vendorPojo);
        this.textViewAmount=textViewAmount;


    }


    @Override
    public CustomAdapterVendorStatus.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.vendor_list_layout, parent, false);
        CustomAdapterVendorStatus.ViewHolder holder = new CustomAdapterVendorStatus.ViewHolder(view);



        return holder;
    }

    @Override
    public void onBindViewHolder(CustomAdapterVendorStatus.ViewHolder holder, final int position)
    {

        listItem = filterList.get(position);

        holder.textViewName.setText(listItem.getVendorName());
        holder.textViewAddress.setText(listItem.getAddress());

        holder.textViewGSTNumber.setVisibility(View.GONE);
        holder.textViewAmount.setVisibility(View.VISIBLE);

        holder.textViewGSTNumber.setText("GST No : "+listItem.getGstNumber());
        holder.imageViewCall.setVisibility(View.VISIBLE);


        Double limit = Double.parseDouble(listItem.getCreditLimit());
        Double amount = Double.parseDouble(listItem.getCurrentBalance());

       // new DecimalFormat("##,##,##,##0").format(amount);


        if(amount>limit)
        {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.recbound_red));
            holder.textViewAmount.setText("Balance : ₹ "+Math.ceil(Double.parseDouble(listItem.getCurrentBalance()))+" / " + String.valueOf(Math.ceil(amount-limit)));
        }

        else
        {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.recbound_green));
            holder.textViewAmount.setText("Balance : ₹ "+Math.ceil(Double.parseDouble(listItem.getCurrentBalance())));
        }

     //   Toast.makeText(context,""+vendorPojo.get(position).getCreditLimit().compareTo(vendorPojo.get(position).getCurrentBalance()),Toast.LENGTH_SHORT).show();


        holder.imageViewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "0"+String.valueOf(listItem.getPhoneNumber())));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Toast.makeText(context, "permission denied, check Setting", Toast.LENGTH_LONG).show();

                } else {
                    context.startActivity(intent);
                }
            }
        });






    }

    public void filter(String text) {

        // Searching could be complex..so we will dispatch it to a different thread...

        // Clear the filter list
        filterList.clear();

        // If there is no search value, then add all original list items to filter list
        if (TextUtils.isEmpty(text)) {
//                    Toast.makeText(context,"No text",Toast.LENGTH_SHORT).show();

            filterList.addAll(vendorPojo);


        }

        else {

            //   Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
            // Iterate in the original List and add it to filter list...
            for (VendorPojo item : vendorPojo) {
                if (item.getVendorName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getCurrentBalance().toLowerCase().contains(text.toLowerCase()) ||
                        item.getGstNumber().toLowerCase().contains(text.toLowerCase()) ||
                        item.getPhoneNumber().toLowerCase().contains(text.toLowerCase()) ||
                        item.getAddress().toLowerCase().contains(text.toLowerCase())   ) {

                    // Adding Matched items
                    filterList.add(item);
                }
            }
        }

        notifyDataSetChanged();
        setTotal();


    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView textViewName,textViewAddress,textViewGSTNumber,textViewAmount;
        ImageView imageViewCall;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            textViewName=(TextView)view.findViewById(R.id.textViewName);
            textViewAddress=(TextView)view.findViewById(R.id.textViewAddress);
            textViewGSTNumber=(TextView)view.findViewById(R.id.textViewGSTNumber);
            textViewAmount=(TextView)view.findViewById(R.id.textViewAmount);
            imageViewCall=(ImageView)view.findViewById(R.id.imageViewCall);

        }


        @Override
        public void onClick(View view) {

            //  Toast.makeText(context,vendorPojo.get(getPosition()).get_id(),Toast.LENGTH_SHORT).show();

            /*
            Intent intent = new Intent(context, VendorEditActivity.class);
            intent.putExtra("data",new Gson().toJson(vendorPojo.get(getPosition())));
            context.startActivity(intent);
            */
        }
    }

    private void setTotal() {

        Double total = 0.0;

        for (int i =0; i<filterList.size();i++)
        {
            total=total+Double.parseDouble(filterList.get(i).getCurrentBalance());
        }

        textViewAmount.setText("₹" + Math.ceil(total));
    }


    @Override
    public int getItemCount() {

        return filterList.size();
    }


}
