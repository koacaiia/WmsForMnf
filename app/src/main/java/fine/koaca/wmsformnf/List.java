package fine.koaca.wmsformnf;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

public class List {
    String bl;
    String description;
    String location;
    String date;
    String count;
    String remark;

    public List(){


    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List(String bl, String description, String location, String date, String count, String remark) {
        this.bl = bl;
        this.description = description;
        this.location = location;
        this.date = date;
        this.count = count;
        this.remark = remark;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBl() {
        return bl;
    }

    public void setBl(String bl) {
        this.bl = bl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result=new HashMap<>();
        result.put("bl",bl);
        result.put("description",description);
        result.put("location",location);
        result.put("date",date);
        result.put("count",count);
        result.put("remark",remark);
        return result;
    }
}
