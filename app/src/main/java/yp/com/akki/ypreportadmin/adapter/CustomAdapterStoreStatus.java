package yp.com.akki.ypreportadmin.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.activity.CentreEditActivity;
import yp.com.akki.ypreportadmin.pojo.store.StorePojo;

/**
 * Created by akshaybmsa96 on 16/03/18.
 */

public class CustomAdapterStoreStatus extends RecyclerView.Adapter<CustomAdapterStoreStatus.ViewHolder>
{

    private Context context;
    private Activity activity;
    private ArrayList<StorePojo> storePojo;


    public CustomAdapterStoreStatus(Context context, ArrayList<StorePojo> storePojo , Activity activity) {

        this.context = context;
        this.activity=activity;
        this.storePojo=storePojo;


    }


    @Override
    public CustomAdapterStoreStatus.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.centre_list_view, parent, false);
        CustomAdapterStoreStatus.ViewHolder holder = new CustomAdapterStoreStatus.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(CustomAdapterStoreStatus.ViewHolder holder, final int position)
    {

        holder.textViewName.setText(storePojo.get(position).getCentre());

        holder.textViewAmount.setVisibility(View.VISIBLE);
        holder.imageViewCall.setVisibility(View.VISIBLE);

        holder.imageViewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "0"+String.valueOf(storePojo.get(position).getPhoneNumber())));
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


        Double limit = Double.parseDouble(storePojo.get(position).getCreditLimit());
        Double amount = Double.parseDouble(storePojo.get(position).getCurrentBalance());

        // new DecimalFormat("##,##,##,##0").format(amount);


        if(amount>limit)
        {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.recbound_red));
            holder.textViewAmount.setText("Balance : ₹ "+String.valueOf(Math.ceil(Double.parseDouble(storePojo.get(position).getCurrentBalance())))+" / " + String.valueOf(Math.ceil(amount-limit)));
        }

        else
        {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.recbound_green));
            holder.textViewAmount.setText("Balance : ₹ "+Math.ceil(Double.parseDouble(storePojo.get(position).getCurrentBalance())));
        }

        //   Toast.makeText(context,""+vendorPojo.get(position).getCreditLimit().compareTo(vendorPojo.get(position).getCurrentBalance()),Toast.LENGTH_SHORT).show();



    }


    class ViewHolder extends RecyclerView.ViewHolder {


        TextView textViewName,textViewAmount;
        ImageView imageViewCall;


        public ViewHolder(View view) {
            super(view);

            textViewName=(TextView)view.findViewById(R.id.textViewName);
            textViewAmount=(TextView)view.findViewById(R.id.textViewAmount);

            imageViewCall=(ImageView) view.findViewById(R.id.imageViewCall);

        }

    }


    @Override
    public int getItemCount() {

        return storePojo.size();
    }


}