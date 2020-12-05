package fine.koaca.wmsformnf;

import android.widget.Toast;

import java.util.Comparator;

public class IncargoListComparator implements Comparator<ListIncargo> {

    @Override
    public int compare(ListIncargo a, ListIncargo b) {
//        if(Integer.parseInt(a.date)>Integer.parseInt(b.date)) return 1;
//        if(Integer.parseInt(a.date)<Integer.parseInt(b.date)) return -1;

//        return 0;
        return a.date.compareTo(b.date);
    }
}
