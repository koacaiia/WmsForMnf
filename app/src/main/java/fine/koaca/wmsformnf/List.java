package fine.koaca.wmsformnf;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

public class List {
    String bl;
    String description;
    String location;

    public List(){


    }

    public List(String bl, String description,String location) {
        this.bl = bl;
        this.description = description;
        this.location=location;
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


//    @Exclude
//    public Map<String,Object> toMap(){
//        HashMap<String,Object> result=new HashMap<>();
//        result.put("bl",bl);
//        result.put("description",description);
//        result.put("location",location);
//        return result;
//    }
}
