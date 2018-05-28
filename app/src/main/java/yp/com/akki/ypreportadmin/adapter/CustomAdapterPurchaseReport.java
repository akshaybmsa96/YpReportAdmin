package yp.com.akki.ypreportadmin.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.global.PreferencedData;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.ApiClientMaterialDistributionDelete;
import yp.com.akki.ypreportadmin.network.ApiClientPurchaseDelete;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.purchase.PurchaseReportPojo;

/**
 * Created by akshaybmsa96 on 18/02/18.
 */

public class CustomAdapterPurchaseReport extends RecyclerView.Adapter<CustomAdapterPurchaseReport.ViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder>
{

    private Context context;
    private Activity activity;
    private ArrayList<PurchaseReportPojo> purchaseReportPojo,filterList;
    private PurchaseReportPojo toDeletePojo;
    private TextView textViewAmount,textViewUsage;
    private int flag = 0;
    private ApiClientPurchaseDelete apiClientPurchaseDelete;
    private EditText editTextPassword;


    public CustomAdapterPurchaseReport(Context context, ArrayList<PurchaseReportPojo> purchaseReportPojo, Activity activity , TextView textViewAmount, TextView textViewUsage) {

        this.context = context;
        this.activity=activity;
        this.purchaseReportPojo=purchaseReportPojo;
        this.filterList=new ArrayList<>();
        this.filterList.addAll(this.purchaseReportPojo);
        this.textViewAmount=textViewAmount;
        this.textViewUsage=textViewUsage;


    }


    @Override
    public CustomAdapterPurchaseReport.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.purchase_report_list_layout, parent, false);
        CustomAdapterPurchaseReport.ViewHolder holder = new CustomAdapterPurchaseReport.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(CustomAdapterPurchaseReport.ViewHolder holder, int position)
    {

        PurchaseReportPojo listItem = filterList.get(position);


        holder.textViewItemName.setText(listItem.getVendorName());
        holder.textViewVendorName.setText(listItem.getItem());
        holder.textViewQty.setText(listItem.getQty() + " " + listItem.getUnit());
        holder.textViewAmount.setText("₹ " + Math.ceil(Double.parseDouble(listItem.getAmount())));

        if(listItem.getDiscount()!=null&&!listItem.getDiscount().equals("0"))
        holder.textViewDiscount.setText("Discount : " + listItem.getDiscount() + " %");

        else
            holder.textViewDiscount.setText("");





    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {


        TextView textViewVendorName, textViewItemName,textViewQty,textViewAmount,textViewDiscount;


        public ViewHolder(View view) {
            super(view);
            view.setOnLongClickListener(this);
            textViewItemName=(TextView)view.findViewById(R.id.textViewItemName);
            textViewVendorName=(TextView)view.findViewById(R.id.textViewVendorName);
            textViewDiscount=(TextView)view.findViewById(R.id.textViewDiscount);
            textViewQty=(TextView)view.findViewById(R.id.textViewQty);
            textViewAmount=(TextView)view.findViewById(R.id.textViewAmount);

        }


        @Override
        public boolean onLongClick(View v) {

            PopupMenu popupMenu;

            popupMenu = new PopupMenu(context, v);
            popupMenu.inflate(R.menu.list_menu);
            popupMenu.show();

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    int mid=item.getItemId();

                    if(mid==R.id.delete_entry)
                    {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        LinearLayout layout = new LinearLayout(context);
                        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layout.setOrientation(LinearLayout.VERTICAL);
                        layout.setLayoutParams(parms);

                        layout.setGravity(Gravity.CLIP_VERTICAL);
                        layout.setPadding(40,0,40,0);

                        editTextPassword = new EditText(context);
                        editTextPassword.setHint("Enter Password");

                        layout.addView(editTextPassword, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                        builder.setView(layout);

                        builder.setTitle(filterList.get(getPosition()).getItem() + " of ₹" + filterList.get(getPosition()).getAmount() + " From " + filterList.get(getPosition()).getVendorName() +" ?");
                        builder.setMessage("Are You Sure?");


                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                if(NetworkCheck.isNetworkAvailable(context)) {


                                    //    Toast.makeText(context,"will be deleted",Toast.LENGTH_SHORT).show();

                                    if(editTextPassword.getText().toString().equals(""))
                                    {
                                        Toast.makeText(context, "Wrong Password", Toast.LENGTH_SHORT).show();
                                    }

                                    else if(!editTextPassword.getText().toString().equals(PreferencedData.getPrefDeliveryCentreCode(context)))
                                    {
                                        Toast.makeText(context, "Wrong Password", Toast.LENGTH_SHORT).show();
                                    }

                                    else if(editTextPassword.getText().toString().equals(PreferencedData.getPrefDeliveryCentreCode(context)))
                                    deleteEntry(getPosition());


                                }

                                else {

                                    Toast.makeText(context, "Network Unavailable", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();


                    }

                    return false;
                }
            });



            return false;
        }

    }


    @Override
    public long getHeaderId(int position) {
        return getId(filterList.get(position).getDate());
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_header_date, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

        TextView textViewDateTotal,textViewDate;
        textViewDate = (TextView) holder.itemView.findViewById(R.id.textViewDate);
        textViewDateTotal = (TextView) holder.itemView.findViewById(R.id.textViewDateTotal);


        textViewDate.setText(filterList.get(position).getDate());


        textViewDateTotal.setText("₹ "+getDateTotal(filterList.get(position).getDate()));



    }

    private String getDateTotal(String date) {

        Double total = 0.0;
        for(int i =0;i<filterList.size();i++)
        {
            if(filterList.get(i).getDate().equals(date))
            {
                total=total+Double.parseDouble(filterList.get(i).getAmount());
            }
        }

        return String.valueOf(Math.ceil(total));
    }

    @Override
    public int getItemCount() {

        return (null != filterList ? filterList.size() : 0);
    }


    Long getId(String input)
    {
        Long id=0l;
        input=input.replace("-","");
        input=input.replace(":","");
        input=input.replace(" ","");
        //  System.out.println("Input is                                                     "+input);
        id=Long.parseLong(input);
        return id;
    }

    public void filter(final String text) {

        // Searching could be complex..so we will dispatch it to a different thread...
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Clear the filter list
                filterList.clear();

                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)) {
//                    Toast.makeText(context,"No text",Toast.LENGTH_SHORT).show();

                    filterList.addAll(purchaseReportPojo);
                    flag=0;


                } else {

                 //   Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
                    // Iterate in the original List and add it to filter list...
                    for (PurchaseReportPojo item : purchaseReportPojo) {
                        if (item.getVendorName().toLowerCase().contains(text.toLowerCase()) ||
                                item.getItem().toLowerCase().contains(text.toLowerCase())) {

                            if(item.getItem().toLowerCase().contains(text.toLowerCase()) && !(item.getVendorName().toLowerCase().contains(text.toLowerCase())))
                            {
                                flag=1;
                            }

                            else
                            {
                                flag=0;
                            }
                            // Adding Matched items
                            filterList.add(item);
                        }
                    }
                }

                // Set on UI Thread
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Notify the List that the DataSet has changed...
                        notifyDataSetChanged();
                        if(flag==1)
                        {
                            setUsage();
                        }

                        else
                        {
                            textViewUsage.setText("");
                        }

                        setAmount();
                    }
                });

            }
        }).start();

    }

    private void setUsage() {

        Double qty=0.0d;

        for(int i=0;i<filterList.size();i++)
        {
            qty=qty+Double.valueOf(filterList.get(i).getQty()).doubleValue();
         //   Toast.makeText(context,"qty "+ filterList.get(i).getQty() + " "+BigDecimal.valueOf(qty),Toast.LENGTH_SHORT).show();
        }

        if(filterList.size()>0)
        textViewUsage.setText(BigDecimal.valueOf(qty)+""+filterList.get(0).getUnit());

        else
            textViewUsage.setText("");

    }


    private void setAmount() {
        Double total=0.0d;

        for(int i=0;i<filterList.size();i++)
        {
            total=total+Double.parseDouble(filterList.get(i).getAmount());
        }

        textViewAmount.setText("₹ "+Math.ceil(total));
    }


    private void deleteEntry(int position) {

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        // show it
        apiClientPurchaseDelete = ApiClientBase.getApiClient().create(ApiClientPurchaseDelete.class);

        //  String amount = String.valueOf(Double.parseDouble(editTextAmount.getText().toString())*-1.0);

        //  System.out.println(new Gson().toJson(receivedPaymentPojo));

        toDeletePojo=filterList.get(position);



        String url = "purchase/id="+filterList.get(position).getVendorId()+"&amount="+filterList.get(position).getAmount()+"&itemId="+filterList.get(position).getItemId();
        Call<String> call= apiClientPurchaseDelete.purchaseDelete(url,new Gson().toJson(filterList.get(position)));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String i=response.body();

                //   Toast.makeText(getActivity(),i+"",Toast.LENGTH_SHORT).show();

                if(i!=null&&i.equals("1"))
                {

                    Toast.makeText(context,"Entry Deleted",Toast.LENGTH_SHORT).show();
                    deleteEntryAtPostion();

                }

                else {
                    Toast.makeText(context,"Cannot Delete",Toast.LENGTH_SHORT).show();
                }
                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pDialog.hide();


                Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });


    }

    private void deleteEntryAtPostion() {

        filterList.remove(toDeletePojo);
        purchaseReportPojo.remove(toDeletePojo);
        notifyDataSetChanged();
        setAmount();
    }


}
