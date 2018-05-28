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
import yp.com.akki.ypreportadmin.activity.CentreEditActivity;
import yp.com.akki.ypreportadmin.activity.ItemEditActivity;
import yp.com.akki.ypreportadmin.pojo.allItems.Items;
import yp.com.akki.ypreportadmin.pojo.store.StorePojo;

/**
 * Created by akshaybmsa96 on 16/03/18.
 */

public class CustomCentreAdapter extends RecyclerView.Adapter<CustomCentreAdapter.ViewHolder>
{

    private Context context;
    private Activity activity;
    private ArrayList<StorePojo> storePojo;


    public CustomCentreAdapter(Context context, ArrayList<StorePojo> storePojo , Activity activity) {

        this.context = context;
        this.activity=activity;
        this.storePojo=storePojo;


    }


    @Override
    public CustomCentreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.centre_list_view, parent, false);
        CustomCentreAdapter.ViewHolder holder = new CustomCentreAdapter.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(CustomCentreAdapter.ViewHolder holder, int position)
    {

        holder.textViewName.setText(storePojo.get(position).getCentre());



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

            Intent intent = new Intent(context, CentreEditActivity.class);
            intent.putExtra("data",new Gson().toJson(storePojo.get(getPosition())));
            context.startActivity(intent);
        }
    }


    @Override
    public int getItemCount() {

        return storePojo.size();
    }


}