package yp.com.akki.ypreportadmin.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.global.PreferencedData;
import yp.com.akki.ypreportadmin.pojo.employee.EmployeeAttendancePojo;
import yp.com.akki.ypreportadmin.pojo.employee.EmployeePojo;

/**
 * Created by akshaybmsa96 on 12/02/18.
 */

public class CustomAdaptderAttendance extends RecyclerView.Adapter<CustomAdaptderAttendance.ViewHolder>
{

    private Context context;
    private Activity activity;
    private ArrayList<EmployeePojo> employeePojo;
    private ArrayList<EmployeeAttendancePojo> employeeAttendancePojo;
    private ArrayList<EmployeeAttendancePojo> employeeAttendancePojoToday;
    private EditText editTextDate;
    private EmployeeAttendancePojo temp;
    private boolean match = false;


    public CustomAdaptderAttendance(Context context, ArrayList<EmployeePojo> employeePojo, Activity activity, ArrayList<EmployeeAttendancePojo> employeeAttendancePojo, EditText editTextDate , ArrayList<EmployeeAttendancePojo> employeeAttendancePojoToday) {

        this.context = context;
        this.activity=activity;
        this.employeePojo=employeePojo;
        this.employeeAttendancePojo=employeeAttendancePojo;
        this.editTextDate=editTextDate;
        this.employeeAttendancePojoToday=employeeAttendancePojoToday;


    }


    @Override
    public CustomAdaptderAttendance.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.attendace_list_layout, parent, false);
        CustomAdaptderAttendance.ViewHolder holder = new CustomAdaptderAttendance.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(final CustomAdaptderAttendance.ViewHolder holder, final int position)
    {

        holder.textViewName.setText(employeePojo.get(position).getEmployeeName());


        holder.buttonPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reset(holder);

                holder.buttonPresent.setTextColor(context.getResources().getColor(R.color.white));
                holder.buttonPresent.setBackgroundColor(context.getResources().getColor(R.color.green));

//                if(employeeAttendancePojo.size()>position)
//                employeeAttendancePojo.remove(position);


                if(employeeAttendancePojo.size()==0)
                {

                    temp= new EmployeeAttendancePojo();
                    temp.setName(employeePojo.get(position).getEmployeeName());
                    temp.setDate(editTextDate.getText().toString());
                    temp.setEmployeeId(employeePojo.get(position).getId());
                    temp.setCentre(PreferencedData.getPrefDeliveryCentre(context));
                    temp.setAttendance("1");
                    temp.setCentreId(PreferencedData.getPrefDeliveryCentreId(context));
                    temp.setEmployeeId(employeePojo.get(position).get_id());


                    employeeAttendancePojo.add(temp);

                }

                else {

                    for (int i = 0; i < employeeAttendancePojo.size(); i++) {
                        if (employeeAttendancePojo.get(i).getEmployeeId().equals(employeePojo.get(position).getId())) {
                            employeeAttendancePojo.get(i).setAttendance("1");
                            match = true;
                            break;
                        } else {

                            match = false;
                        }

                    }


                    if (!match) {
                        temp = new EmployeeAttendancePojo();
                        temp.setName(employeePojo.get(position).getEmployeeName());
                        temp.setDate(editTextDate.getText().toString());
                        temp.setEmployeeId(employeePojo.get(position).getId());
                        temp.setCentre(PreferencedData.getPrefDeliveryCentre(context));
                        temp.setAttendance("1");
                        temp.setCentreId(PreferencedData.getPrefDeliveryCentreId(context));
                        temp.setEmployeeId(employeePojo.get(position).get_id());


                        employeeAttendancePojo.add(temp);
                    }

                }

                //employeeAttendancePojo.get(position).setAttendance("1");

            }
        });

        holder.buttonHalfDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reset(holder);

                holder.buttonHalfDay.setTextColor(context.getResources().getColor(R.color.white));
                holder.buttonHalfDay.setBackgroundColor(context.getResources().getColor(R.color.orange));


                if(employeeAttendancePojo.size()==0)
                {

                    temp= new EmployeeAttendancePojo();
                    temp.setName(employeePojo.get(position).getEmployeeName());
                    temp.setDate(editTextDate.getText().toString());
                    temp.setEmployeeId(employeePojo.get(position).getId());
                    temp.setCentre(PreferencedData.getPrefDeliveryCentre(context));
                    temp.setAttendance("0.5");
                    temp.setCentreId(PreferencedData.getPrefDeliveryCentreId(context));
                    temp.setEmployeeId(employeePojo.get(position).get_id());


                    employeeAttendancePojo.add(temp);

                }

                else {

                    for (int i = 0; i < employeeAttendancePojo.size(); i++) {
                        if (employeeAttendancePojo.get(i).getEmployeeId().equals(employeePojo.get(position).getId())) {
                            employeeAttendancePojo.get(i).setAttendance("0.5");
                            match = true;
                            break;
                        } else {

                            match = false;
                        }

                    }


                    if (!match) {
                        temp = new EmployeeAttendancePojo();
                        temp.setName(employeePojo.get(position).getEmployeeName());
                        temp.setDate(editTextDate.getText().toString());
                        temp.setEmployeeId(employeePojo.get(position).getId());
                        temp.setCentre(PreferencedData.getPrefDeliveryCentre(context));
                        temp.setAttendance("0.5");
                        temp.setCentreId(PreferencedData.getPrefDeliveryCentreId(context));
                        temp.setEmployeeId(employeePojo.get(position).get_id());


                        employeeAttendancePojo.add(temp);
                    }

                }



            }
        });

        holder.buttonAbsent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reset(holder);

                holder.buttonAbsent.setTextColor(context.getResources().getColor(R.color.white));
                holder.buttonAbsent.setBackgroundColor(context.getResources().getColor(R.color.darkred));

                if(employeeAttendancePojo.size()==0)
                {

                    temp= new EmployeeAttendancePojo();
                    temp.setName(employeePojo.get(position).getEmployeeName());
                    temp.setDate(editTextDate.getText().toString());
                    temp.setEmployeeId(employeePojo.get(position).getId());
                    temp.setCentre(PreferencedData.getPrefDeliveryCentre(context));
                    temp.setAttendance("0");
                    temp.setCentreId(PreferencedData.getPrefDeliveryCentreId(context));
                    temp.setEmployeeId(employeePojo.get(position).get_id());


                    employeeAttendancePojo.add(temp);

                }

                else {

                    for (int i = 0; i < employeeAttendancePojo.size(); i++) {
                        if (employeeAttendancePojo.get(i).getEmployeeId().equals(employeePojo.get(position).getId())) {
                            employeeAttendancePojo.get(i).setAttendance("0");
                            match = true;
                            break;
                        } else {

                            match = false;
                        }

                    }


                    if (!match) {
                        temp = new EmployeeAttendancePojo();
                        temp.setName(employeePojo.get(position).getEmployeeName());
                        temp.setDate(editTextDate.getText().toString());
                        temp.setEmployeeId(employeePojo.get(position).getId());
                        temp.setCentre(PreferencedData.getPrefDeliveryCentre(context));
                        temp.setAttendance("0");
                        temp.setCentreId(PreferencedData.getPrefDeliveryCentreId(context));
                        temp.setEmployeeId(employeePojo.get(position).get_id());


                        employeeAttendancePojo.add(temp);
                    }

                }



            }
        });


        if(employeeAttendancePojoToday!=null&&employeeAttendancePojoToday.size()>0)
        {
            for(int i =0 ; i< employeeAttendancePojoToday.size();i++)
            {
                if(employeePojo.get(position).get_id().equals(employeeAttendancePojoToday.get(i).getEmployeeId()))
                {
                    if(employeeAttendancePojoToday.get(i).getAttendance().equals("1")){
                        holder.buttonPresent.performClick();
                        employeeAttendancePojoToday.remove(i);
                        break;

                    }

                    else if(employeeAttendancePojoToday.get(i).getAttendance().equals("0.5")){
                        holder.buttonHalfDay.performClick();
                        employeeAttendancePojoToday.remove(i);
                        break;
                    }

                    else {
                        holder.buttonAbsent.performClick();
                        employeeAttendancePojoToday.remove(i);
                        break;
                    }
                }
            }
        }



    }

    private void reset(ViewHolder holder) {

        holder.buttonAbsent.setTextColor(context.getResources().getColor(R.color.darkred));
        holder.buttonAbsent.setBackgroundColor(context.getResources().getColor(R.color.white));

        holder.buttonHalfDay.setTextColor(context.getResources().getColor(R.color.orange));
        holder.buttonHalfDay.setBackgroundColor(context.getResources().getColor(R.color.white));

        holder.buttonPresent.setTextColor(context.getResources().getColor(R.color.green));
        holder.buttonPresent.setBackgroundColor(context.getResources().getColor(R.color.white));


    }


    class ViewHolder extends RecyclerView.ViewHolder {


        TextView textViewName,textViewQty,textViewTotalCost;
        Button buttonPresent,buttonHalfDay,buttonAbsent;


        public ViewHolder(View view) {
            super(view);
            textViewName=(TextView)view.findViewById(R.id.textViewName);
            textViewQty=(TextView)view.findViewById(R.id.textViewQty);
            textViewTotalCost=(TextView)view.findViewById(R.id.textViewTotalCost);

            buttonPresent=(Button)view.findViewById(R.id.buttonPresent);
            buttonHalfDay=(Button)view.findViewById(R.id.buttonHalfDay);
            buttonAbsent=(Button)view.findViewById(R.id.buttonAbsent);

        }

    }


    @Override
    public int getItemCount() {

        return employeePojo.size();
    }


}