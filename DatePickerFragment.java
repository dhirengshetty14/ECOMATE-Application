package com.ecomate;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
DatePickerDialog d=new DatePickerDialog(getActivity(), this, year, month, day);
        // Create a new instance of DatePickerDialog and return it
        d.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return d;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

       SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = sdf.format(c.getTime());
/*StringBuffer date=new StringBuffer();
        date.append(year).append("-").append(month).append("-").append(day);
//String date=String.valueOf(year) +"-" +String.valueOf(month+1)+ "-" + String.valueOf(year)*/
        EditText t1= (EditText) getActivity().findViewById(R.id.date);
        t1.setText(formattedDate);

    }
}
