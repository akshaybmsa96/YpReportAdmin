package yp.com.akki.ypreportadmin.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import java.util.Calendar;
import java.util.Date;

import yp.com.akki.ypreportadmin.activity.ChooseDateActivity;


public class DateDualFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
// Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dp=new DatePickerDialog(getActivity(),(ChooseDateActivity)getActivity(), year, month, day);
        dp.getDatePicker().setMaxDate(new Date().getTime());


// Create a new instance of DatePickerDialog and return it
        return dp;
    }
}

