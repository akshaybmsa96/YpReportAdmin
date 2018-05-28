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
import yp.com.akki.ypreportadmin.pojo.attendanceRecord.AttendanceDetailPojo;


/**
 * Created by akshaybmsa96 on 28/02/18.
 */

public class CustomAdapterAttendanceDetail extends RecyclerView.Adapter<CustomAdapterAttendanceDetail.ViewHolder>
{

    private Context context;
    private Activity activity;
    private ArrayList<AttendanceDetailPojo> attendanceDetailPojo;



    public CustomAdapterAttendanceDetail(Context context, ArrayList<AttendanceDetailPojo> attendanceDetailPojo ,Activity activity) {

        this.context = context;
        this.activity=activity;
        this.attendanceDetailPojo=attendanceDetailPojo;

    }


    @Override
    public CustomAdapterAttendanceDetail.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.attendance_detail_layout, parent, false);
        CustomAdapterAttendanceDetail.ViewHolder holder = new CustomAdapterAttendanceDetail.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(final CustomAdapterAttendanceDetail.ViewHolder holder, final int position)
    {

        holder.textViewDate.setText(attendanceDetailPojo.get(position).getDate());

        if(attendanceDetailPojo.get(position).getAttendance().equals("0")) {

            holder.textViewStatus.setTextColor(context.getResources().getColor(R.color.darkred));
            holder.textViewStatus.setText("ABSENT");
        }

        else if(attendanceDetailPojo.get(position).getAttendance().equals("1")) {

            holder.textViewStatus.setTextColor(context.getResources().getColor(R.color.green));
            holder.textViewStatus.setText("PRESENT");
        }

        else if(attendanceDetailPojo.get(position).getAttendance().equals("0.5")) {

            holder.textViewStatus.setTextColor(context.getResources().getColor(R.color.orange));
            holder.textViewStatus.setText("HALF DAY");
        }


    }


    class ViewHolder extends RecyclerView.ViewHolder {


        TextView textViewDate,textViewStatus;


        public ViewHolder(View view) {
            super(view);
            textViewDate=(TextView)view.findViewById(R.id.textViewDate);
            textViewStatus=(TextView)view.findViewById(R.id.textViewStatus);

        }

    }

    @Override
    public int getItemCount() {

        return attendanceDetailPojo.size();
    }


}