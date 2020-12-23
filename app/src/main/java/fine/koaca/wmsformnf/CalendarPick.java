package fine.koaca.wmsformnf;

import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarPick {

    Calendar cal= Calendar.getInstance();
    SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
    int dayOfWeek=cal.get(Calendar.DATE);
    Date nowDate=new Date();
    String date_mon;
    String date_sat;
    String date_Nmon;
    String date_Nsat;
    String date_startMonth;
    String date_lastMonth;
    String date_tomorrow;

    String year;
    String month;
    String day;


    public void CalendarCall(){
      String nowDated = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
      Incargo incargo=new Incargo();

            SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");

            Log.i("Date_now",nowDated);
            Calendar c=Calendar.getInstance();

            c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
            date_mon=formatter.format(c.getTime());
            incargo.day_start=formatter.format(c.getTime());

            c.add(c.DATE,2);
            date_tomorrow=formatter.format(c.getTime());

            c.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
//            c.add(c.DATE,7);
            date_sat=formatter.format(c.getTime());

            c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
            c.add(c.DATE,7);
            date_Nmon=formatter.format(c.getTime());

            c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            c.add(c.DATE,7);
            date_Nsat=formatter.format(c.getTime());
         c.getMinimum(Calendar.DAY_OF_MONTH+1);
         date_startMonth=formatter.format(c.getTime());
         date_lastMonth=Integer.toString(c.getActualMaximum(Calendar.DAY_OF_MONTH));



        year=String.valueOf(cal.get(Calendar.YEAR));
        month=String.valueOf(cal.get(Calendar.MONTH)+1);
        day=String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        Log.i("koaca+_date_All",year+month+day);


    }


}
