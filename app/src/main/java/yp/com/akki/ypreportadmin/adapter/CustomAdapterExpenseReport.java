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

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.global.PreferencedData;
import yp.com.akki.ypreportadmin.network.ApiClientBase;
import yp.com.akki.ypreportadmin.network.ApiClientExpenseDelete;
import yp.com.akki.ypreportadmin.network.ApiClientPurchaseDelete;
import yp.com.akki.ypreportadmin.network.NetworkCheck;
import yp.com.akki.ypreportadmin.pojo.expense.ExpenseReportPojo;


/**
 * Created by akshaybmsa96 on 25/02/18.
 */

public class CustomAdapterExpenseReport extends RecyclerView.Adapter<CustomAdapterExpenseReport.ViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder>
{

    private Context context;
    private Activity activity;
    private ExpenseReportPojo toDeletePojo;
    private ArrayList<ExpenseReportPojo> expenseReportPojo,filterList;
    private TextView textViewAmount;
    private int flag = 0;
    private ApiClientExpenseDelete apiClientExpenseDelete;
    private EditText editTextPassword;


    public CustomAdapterExpenseReport(Context context, ArrayList<ExpenseReportPojo> expenseReportPojo, Activity activity , TextView textViewAmount) {

        this.context = context;
        this.activity=activity;
        this.expenseReportPojo=expenseReportPojo;
        this.filterList=new ArrayList<>();
        this.filterList.addAll(this.expenseReportPojo);
        this.textViewAmount=textViewAmount;


    }


    @Override
    public CustomAdapterExpenseReport.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.expense_report_list_layout, parent, false);
        CustomAdapterExpenseReport.ViewHolder holder = new CustomAdapterExpenseReport.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(CustomAdapterExpenseReport.ViewHolder holder, int position)
    {

        ExpenseReportPojo listItem = filterList.get(position);


        holder.textViewCategory.setText(listItem.getCategory());
        holder.textViewName.setText(listItem.getName());
        holder.textViewType.setText(listItem.getType());
        holder.textViewAmount.setText("₹ " + Math.ceil(Double.parseDouble(listItem.getAmount())));
        holder.textViewPaymentMode.setText(listItem.getModeOfPayment());
        holder.textViewFromAc.setText("From : "+listItem.getFrom());
        holder.textViewToAc.setText("To : "+listItem.getTo());






    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {


        TextView textViewName,textViewCategory,textViewType,textViewAmount,textViewPaymentMode,textViewFromAc,textViewToAc;


        public ViewHolder(View view) {
            super(view);
            view.setOnLongClickListener(this);
            textViewCategory=(TextView)view.findViewById(R.id.textViewCategory);
            textViewName=(TextView)view.findViewById(R.id.textViewName);
            textViewPaymentMode=(TextView)view.findViewById(R.id.textViewPaymentMode);
            textViewType=(TextView)view.findViewById(R.id.textViewType);
            textViewAmount=(TextView)view.findViewById(R.id.textViewAmount);
            textViewFromAc=(TextView)view.findViewById(R.id.textViewFromAc);
            textViewToAc=(TextView)view.findViewById(R.id.textViewToAc);

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

                        builder.setTitle("Delete "+filterList.get(getPosition()).getName() + " of ₹" + filterList.get(getPosition()).getAmount() +" ?");
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

                    filterList.addAll(expenseReportPojo);


                } else {

                    //   Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
                    // Iterate in the original List and add it to filter list...
                    for (ExpenseReportPojo item : expenseReportPojo) {
                        if (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                                item.getType().toLowerCase().contains(text.toLowerCase()) ||
                                     item.getCategory().toLowerCase().contains(text.toLowerCase()) ||
                                          item.getModeOfPayment().toLowerCase().contains(text.toLowerCase()) ||
                                                 item.getFrom().toLowerCase().contains(text.toLowerCase()) ) {

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


                        setAmount();
                    }
                });

            }
        }).start();

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
        apiClientExpenseDelete = ApiClientBase.getApiClient().create(ApiClientExpenseDelete.class);

        //  String amount = String.valueOf(Double.parseDouble(editTextAmount.getText().toString())*-1.0);

        //  System.out.println(new Gson().toJson(receivedPaymentPojo));

        toDeletePojo=filterList.get(position);



        String url = "expense/type="+filterList.get(position).getType()+"&id="+filterList.get(position).getNameId()+"&amount="+filterList.get(position).getAmount();
        Call<String> call= apiClientExpenseDelete.expenseDelete(url,new Gson().toJson(filterList.get(position)));
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
        expenseReportPojo.remove(toDeletePojo);
        notifyDataSetChanged();
        setAmount();
    }


}
