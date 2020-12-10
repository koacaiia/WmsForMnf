package fine.koaca.wmsformnf;

import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarPick {
    String monday;
    String sunday;
    Calendar cal= Calendar.getInstance();
    SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
    int dayOfWeek=cal.get(Calendar.DATE);
    Date nowDate=new Date();

    public void CalendarCall(){
      String nowDated = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

            SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");

            Log.i("Date_now",nowDated);
            Calendar c=Calendar.getInstance();

            c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
            String date_mon=formatter.format(c.getTime());
            Log.i("Date_mon",date_mon);

            c.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
//            c.add(c.DATE,7);
            String date_sat=formatter.format(c.getTime());
            Log.i("Date_sat",date_sat);

         String date_startMonth=Integer.toString(c.getMinimum(Calendar.DAY_OF_MONTH));
         Log.i("Date_1",date_startMonth);

         String date_lastMonth=Integer.toString(c.getActualMaximum(Calendar.DAY_OF_MONTH));
         Log.i("Date_30",date_lastMonth);


        final Calendar c1= Calendar.getInstance();
        int year=c1.get(Calendar.YEAR);
        int month=c1.get(Calendar.MONTH);
        int day=c1.get(Calendar.DAY_OF_MONTH);
    }


}
