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
import yp.com.akki.ypreportadmin.activity.ReportFullActivity;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.ApiClientCentreReportItemUsage;
import yp.com.akki.ypreportadmin.network.ApiClientHistory;
import yp.com.akki.ypreportadmin.pojo.history.CentreReportpojo;
import yp.com.akki.ypreportadmin.pojo.history.ItemUsagePojo;
import yp.com.akki.ypreportadmin.pojo.reportDetailPojo.ReportDetailPojo;
import yp.com.akki.ypreportadmin.pojo.reportPojo.Items;

/**
 * Created by akshaybmsa96 on 22/01/18.
 */

public class CustomSaleAdapter extends RecyclerView.Adapter<CustomSaleAdapter.ViewHolder>
{

    private Context context;
    private Activity activity;
    private ArrayList<CentreReportpojo> centreReportpojo;
    private String fromDate,toDate;
    private ApiClientHistory apiClientHistory;
    private ReportDetailPojo reportDetailPojo;
    private ApiClientCentreReportItemUsage apiClientCentreReportItemUsage;
    private ArrayList<ItemUsagePojo> itemUsagePojo;


    public CustomSaleAdapter(Context context, ArrayList<CentreReportpojo> centreReportpojo, Activity activity,String fromDate,String toDate) {

        this.context = context;
        this.activity=activity;
        this.centreReportpojo=centreReportpojo;
        this.fromDate=fromDate;
        this.toDate=toDate;


    }


    @Override
    public CustomSaleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.report_list_layout, parent, false);
        CustomSaleAdapter.ViewHolder holder = new CustomSaleAdapter.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(CustomSaleAdapter.ViewHolder holder, int position)
    {

        holder.textViewItemName.setText(centreReportpojo.get(position).get_id().getCentre());
        holder.textViewSale.setText("Sale : ₹ "+centreReportpojo.get(position).getSale());
        holder.textViewOrders.setText("Orders : "+centreReportpojo.get(position).getOrders());
        holder.textViewMaterialCost.setText("Material Cost : ₹ "+centreReportpojo.get(position).getMaterialCost());

    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView textViewItemName,textViewSale,textViewOrders,textViewMaterialCost;


        public ViewHolder(View view) {
            super(view);
            textViewItemName=(TextView)view.findViewById(R.id.textViewItemName);
            textViewSale=(TextView)view.findViewById(R.id.textViewSale);
            textViewOrders=(TextView)view.findViewById(R.id.textViewOrders);
            textViewMaterialCost=(TextView)view.findViewById(R.id.textViewMaterialCost);

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            getDetails(getPosition());
            //   Toast.makeText(context,"Touch",Toast.LENGTH_SHORT).show();
        }
    }

    private void getDetails(final int pos) {



        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientCentreReportItemUsage = ApiClientBase.getApiClient().create(ApiClientCentreReportItemUsage.class);
        String url="itemUsagereport/centre="+centreReportpojo.get(pos).get_id().getCentreId()+"&fromdate="+fromDate.replaceAll("^\"|\"$", "")+"&todate="+toDate.replaceAll("^\"|\"$", "");

        System.out.println(url);
        Call<ArrayList<ItemUsagePojo>> call= apiClientCentreReportItemUsage.getHistoryItemUsage(url);
        call.enqueue(new Callback<ArrayList<ItemUsagePojo>>() {
            @Override
            public void onResponse(Call<ArrayList<ItemUsagePojo>> call, Response<ArrayList<ItemUsagePojo>> response) {

                itemUsagePojo=response.body();
              //   Toast.makeText(context,""+response.body(),Toast.LENGTH_SHORT).show();

                if(itemUsagePojo!=null&&itemUsagePojo.size()>0)
                {

                  //  Toast.makeText(context,""+itemUsagePojo.toString(),Toast.LENGTH_SHORT).show();

                    //delete history Activity
                       Intent i=new Intent(activity,ReportFullActivity.class);
                       i.putExtra("data",new Gson().toJson(itemUsagePojo));

                       context.startActivity(i);
                }

                else
                {
                    Toast.makeText(activity,"No Data Found",Toast.LENGTH_SHORT).show();
                }

                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ArrayList<ItemUsagePojo>> call, Throwable t) {
                pDialog.dismiss();


                Toast.makeText(activity,"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });




        /*

       // System.out.println(fromDate+" " + toDate +" " + itemInfoPojo.get(pos).getCentre());
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientHistory = ApiClientBase.getApiClient().create(ApiClientHistory.class);
        Call<ReportDetailPojo> call= apiClientHistory.getHistory(fromDate,toDate,centreReportpojo.get(pos).get_id().getCentreId());
        call.enqueue(new Callback<ReportDetailPojo>() {
            @Override
            public void onResponse(Call<ReportDetailPojo> call, Response<ReportDetailPojo> response) {

                reportDetailPojo=response.body();

               // Toast.makeText(context,""+reportDetailPojo.toString(),Toast.LENGTH_SHORT).show();

                if(reportDetailPojo.getItems().size()>0)
                {

                    //    Toast.makeText(context,""+reportDetailPojo.toString(),Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(context,ReportFullActivity.class);
                    i.putExtra("data",new Gson().toJson(reportDetailPojo));
                    context.startActivity(i);
                }

                else
                {
                    Toast.makeText(context,"No Data Found",Toast.LENGTH_SHORT).show();
                }

                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ReportDetailPojo> call, Throwable t) {
                pDialog.dismiss();


                Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });

        */
    }


    @Override
    public int getItemCount() {

        return centreReportpojo.size();
    }


}

