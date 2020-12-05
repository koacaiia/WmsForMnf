package fine.koaca.wmsformnf;

import android.widget.Toast;

import java.util.Comparator;

public class IncargoListComparator implements Comparator<Fine2IncargoList> {

    @Override
    public int compare(Fine2IncargoList a, Fine2IncargoList b) {
//        if(Integer.parseInt(a.date)>Integer.parseInt(b.date)) return 1;
//        if(Integer.parseInt(a.date)<Integer.parseInt(b.date)) return -1;

//        return 0;
        return a.date.compareTo(b.date);
    }
}
