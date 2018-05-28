package yp.com.akki.ypreportadmin.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.activity.AttendanceDetailActivity;
import yp.com.akki.ypreportadmin.global.PreferencedData;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.ApiClientGetAttendanceDetail;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.attendanceRecord.AttendanceDetailPojo;
import yp.com.akki.ypreportadmin.pojo.attendanceRecord.AttendanceRecordPojo;


/**
 * Created by akshaybmsa96 on 28/02/18.
 */

public class CustomAdapterAttendanceReport extends RecyclerView.Adapter<CustomAdapterAttendanceReport.ViewHolder>
{

    private Context context;
    private Activity activity;
    private ArrayList<AttendanceRecordPojo> attendanceRecordPojo;
    private String fromdate,todate;
    private ApiClientGetAttendanceDetail apiClientGetAttendanceDetail;
    private ArrayList<AttendanceDetailPojo> attendanceDetailPojo;
    private Boolean sameMonth;


    public CustomAdapterAttendanceReport(Context context, ArrayList<AttendanceRecordPojo> attendanceRecordPojo, Activity activity, String fromdate,String todate , Boolean sameMonth) {

        this.context = context;
        this.activity=activity;
        this.attendanceRecordPojo=attendanceRecordPojo;
        this.fromdate=fromdate;
        this.todate=todate;
        this.sameMonth=sameMonth;


    }


    @Override
    public CustomAdapterAttendanceReport.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.employee_attendance_reoprt_layout, parent, false);
        CustomAdapterAttendanceReport.ViewHolder holder = new CustomAdapterAttendanceReport.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(CustomAdapterAttendanceReport.ViewHolder holder, final int position)
    {

        holder.textViewName.setText(attendanceRecordPojo.get(position).get_id().getName());
     //   holder.textViewDesignation.setText(attendanceRecordPojo.get(position));
     //   holder.textViewAddress.setText(attendanceRecordPojo.get(position).getAddress());

        holder.textViewDesignation.setVisibility(View.GONE);
        holder.textViewAddress.setVisibility(View.GONE);


        holder.textViewTotalPresents.setText("Presents : "+attendanceRecordPojo.get(position).getAttendanceTotal() + " Days");


    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView textViewName,textViewDesignation,textViewAddress,textViewTotalPresents;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            textViewName=(TextView)view.findViewById(R.id.textViewName);
            textViewDesignation=(TextView)view.findViewById(R.id.textViewDesignation);
            textViewAddress=(TextView)view.findViewById(R.id.textViewAddress);
            textViewTotalPresents=(TextView)view.findViewById(R.id.textViewTotalPresents);

        }


        @Override
        public void onClick(View view) {


            if(NetworkCheck.isNetworkAvailable(context))
                    getEmployeeAttendanceDetail(attendanceRecordPojo.get(getPosition()).get_id().getEmployeeId());

            else
                Toast.makeText(context,"Network Unavailable",Toast.LENGTH_SHORT).show();

        }
    }

    private void getEmployeeAttendanceDetail(String id) {


        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientGetAttendanceDetail = ApiClientBase.getApiClient().create(ApiClientGetAttendanceDetail.class);

        String url="attendancedetail/id="+id+"&centre="+ PreferencedData.getPrefDeliveryCentreId(context)+"&fromdate="+fromdate+"&todate="+todate;



        Call<ArrayList<AttendanceDetailPojo>> call= apiClientGetAttendanceDetail.getAttendanceDetail(url);
        call.enqueue(new Callback<ArrayList<AttendanceDetailPojo>>() {
            @Override
            public void onResponse(Call<ArrayList<AttendanceDetailPojo>> call, Response<ArrayList<AttendanceDetailPojo>> response) {


                attendanceDetailPojo=response.body();

                // Toast.makeText(getActivity(),employeePojo.toString()+"",Toast.LENGTH_SHORT).show();
                if(attendanceDetailPojo!=null) {


                 //  Toast.makeText(context,attendanceDetailPojo.toString()+"",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(activity, AttendanceDetailActivity.class);
                    intent.putExtra("sameMonth",sameMonth);
                    intent.putExtra("data",new Gson().toJson(attendanceDetailPojo));
                    intent.putExtra("fromdate",fromdate);
                    intent.putExtra("todate",todate);
                    context.startActivity(intent);


                }

                else
                    Toast.makeText(context,"No Record Found",Toast.LENGTH_SHORT).show();

                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ArrayList<AttendanceDetailPojo>> call, Throwable t) {
                pDialog.hide();


                Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });

    }


    @Override
    public int getItemCount() {

        return attendanceRecordPojo.size();
    }


}
