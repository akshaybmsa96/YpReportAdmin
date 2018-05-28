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

/**
 * Created by akshaybmsa96 on 26/02/18.
 */

public class CustomAdapterEmployeeStatus extends RecyclerView.Adapter<CustomAdapterEmployeeStatus.ViewHolder>
{

    private Context context;
    private Activity activity;
    private ArrayList<EmployeePojo> employeePojo,filterList;
    private EmployeePojo listItem;


    public CustomAdapterEmployeeStatus(Context context, ArrayList<EmployeePojo> employeePojo, Activity activity) {

        this.context = context;
        this.activity=activity;
        this.employeePojo=employeePojo;
        this.filterList=new ArrayList<>();
        this.filterList.addAll(this.employeePojo);


    }


    @Override
    public CustomAdapterEmployeeStatus.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.employee_list_layout, parent, false);
        CustomAdapterEmployeeStatus.ViewHolder holder = new CustomAdapterEmployeeStatus.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(CustomAdapterEmployeeStatus.ViewHolder holder, final int position)
    {
        listItem = filterList.get(position);

        holder.textViewName.setText(listItem.getEmployeeName());
        holder.textViewDesignation.setText(listItem.getDesignation());
        holder.textViewAddress.setText(listItem.getAddress());

        holder.textViewAmount.setVisibility(View.VISIBLE);
        holder.textViewAmount.setText("₹ "+listItem.getCurrentBalance());

        holder.imageViewCall.setVisibility(View.VISIBLE);


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

    public void filter(final String text) {

        // Searching could be complex..so we will dispatch it to a different thread...

        // Clear the filter list
        filterList.clear();

        // If there is no search value, then add all original list items to filter list
        if (TextUtils.isEmpty(text)) {
//                    Toast.makeText(context,"No text",Toast.LENGTH_SHORT).show();

            filterList.addAll(employeePojo);


        }

        else {

            //   Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
            // Iterate in the original List and add it to filter list...
            for (EmployeePojo item : employeePojo) {
                if (item.getEmployeeName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getCurrentBalance().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDesignation().toLowerCase().contains(text.toLowerCase()) ||
                        item.getPhoneNumber().toLowerCase().contains(text.toLowerCase()) ||
                        item.getAddress().toLowerCase().contains(text.toLowerCase()) ||
                        item.getSalary().toLowerCase().contains(text.toLowerCase()) ||
                        item.getLeavesPerMonth().toLowerCase().contains(text.toLowerCase())) {

                    // Adding Matched items
                    filterList.add(item);
                }
            }
            notifyDataSetChanged();
        }



    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView textViewName,textViewDesignation,textViewAddress,textViewAmount;
        ImageView imageViewCall;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            textViewName=(TextView)view.findViewById(R.id.textViewName);
            textViewDesignation=(TextView)view.findViewById(R.id.textViewDesignation);
            textViewAddress=(TextView)view.findViewById(R.id.textViewAddress);
            textViewAmount=(TextView)view.findViewById(R.id.textViewAmount);
            imageViewCall=(ImageView)view.findViewById((R.id.imageViewCall));

        }


        @Override
        public void onClick(View view) {

            // Toast.makeText(context,employeePojo.get(getPosition()).getId(),Toast.LENGTH_SHORT).show();

/*
            Intent i = new Intent(context, EmployeeEditActivity.class);
            i.putExtra("data",new Gson().toJson(employeePojo.get(getPosition())));
            context.startActivity(i);

            */

        }
    }


    @Override
    public int getItemCount() {

        return filterList.size();
    }


}
