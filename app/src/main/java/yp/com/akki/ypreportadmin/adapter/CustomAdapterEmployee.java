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

import com.google.gson.Gson;

import java.util.ArrayList;

import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.activity.EmployeeEditActivity;
import yp.com.akki.ypreportadmin.pojo.employee.EmployeePojo;

/**
 * Created by akshaybmsa96 on 18/02/18.
 */

public class CustomAdapterEmployee extends RecyclerView.Adapter<CustomAdapterEmployee.ViewHolder>
{

    private Context context;
    private Activity activity;
    private ArrayList<EmployeePojo> employeePojo,filterList;
    private EmployeePojo listItem;


    public CustomAdapterEmployee(Context context, ArrayList<EmployeePojo> employeePojo, Activity activity) {

        this.context = context;
        this.activity=activity;
        this.employeePojo=employeePojo;
        this.filterList=new ArrayList<>();
        this.filterList.addAll(this.employeePojo);


    }


    @Override
    public CustomAdapterEmployee.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.employee_list_layout, parent, false);
        CustomAdapterEmployee.ViewHolder holder = new CustomAdapterEmployee.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(CustomAdapterEmployee.ViewHolder holder, int position)
    {
        listItem = filterList.get(position);

        holder.textViewName.setText(listItem.getEmployeeName());
        holder.textViewDesignation.setText(listItem.getDesignation());
        holder.textViewAddress.setText(listItem.getAddress());



    }

    public void filter(String text) {

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
                        item.getDesignation().toLowerCase().contains(text.toLowerCase()) ||
                        item.getAddress().toLowerCase().contains(text.toLowerCase())) {

                    // Adding Matched items
                    filterList.add(item);
                }
            }
            notifyDataSetChanged();
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView textViewName,textViewDesignation,textViewAddress;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            textViewName=(TextView)view.findViewById(R.id.textViewName);
            textViewDesignation=(TextView)view.findViewById(R.id.textViewDesignation);
            textViewAddress=(TextView)view.findViewById(R.id.textViewAddress);

        }


        @Override
        public void onClick(View view) {

            // Toast.makeText(context,employeePojo.get(getPosition()).getId(),Toast.LENGTH_SHORT).show();


            Intent i = new Intent(context, EmployeeEditActivity.class);
            i.putExtra("data",new Gson().toJson(filterList.get(getPosition())));
            context.startActivity(i);

        }
    }


    @Override
    public int getItemCount() {

        return filterList.size();
    }


}
