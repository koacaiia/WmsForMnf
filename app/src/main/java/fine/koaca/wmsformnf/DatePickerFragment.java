package fine.koaca.wmsformnf;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
String a;

    public DatePickerFragment(String a) {
        this.a=a;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c= Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH);
        int day=c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),this,year,month,day);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
       if(a.equals("a")){
        MainActivity activity=(MainActivity)getActivity();
        activity.processDatePickerResult(year,month,dayOfMonth);}
       else{
           Incargo incargo=(Incargo)getActivity();
           incargo.processDatePickerResult(year,month,dayOfMonth);
       }


    }
}