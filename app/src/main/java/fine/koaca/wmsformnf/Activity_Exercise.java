package fine.koaca.wmsformnf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class Activity_Exercise extends AppCompatActivity {
    RecyclerView exr_recyclerView;
    IncargoListAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    ArrayList<Fine2IncargoList> listItems=new ArrayList<Fine2IncargoList>();

    ArrayList<String> list_consignee=null;

    static private String SHARE_NAME="SHARE_DEPOT";
    static SharedPreferences sharedPref=null;
    static SharedPreferences.Editor editor=null;
    TextView exr_text;
    Incargo inCargo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__exercise);

        exr_text=findViewById(R.id.exr_text);
        inCargo=new Incargo(listItems);
        listItems=inCargo.listItems;


        exr_recyclerView=findViewById(R.id.exr_recyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        exr_recyclerView.setLayoutManager(layoutManager);
        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("Incargo");
        getFirebaseDatabase();

        adapter=new IncargoListAdapter(listItems,this);
        exr_recyclerView.setAdapter(adapter);
//        Toast.makeText(this, String.valueOf(listItems1.size()), Toast.LENGTH_SHORT).show();
//        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
//        dialog.setTitle("koaca");
//        dialog.show();

        sharedPref=getSharedPreferences(SHARE_NAME,MODE_PRIVATE);
        editor= sharedPref.edit();
    }

    public void btnClick(View view){
        int btnName=view.getId();
        switch(btnName){
            case R.id.exr_save:

                saveData();
                break;
            case R.id.exr_update:
                updateData();
                break;
            case R.id.exr_delete:
                deleteData();
                break;
            case R.id.exr_clear:
                editor.clear();
                editor.commit();
                break;
            default:
        }
        listData();
    }

    private void listData() {
        String dataList="";
        Map<String,?> totalvalue=sharedPref.getAll();
        for(Map.Entry<String,?>entry: totalvalue.entrySet()){
            dataList +=entry.getKey().toString()+":"+entry.getValue().toString()+"\r\n";
            Log.d("share:",entry.getKey()+":"+entry.getValue());
        }
//        exr_text.setText(dataList);
    }

    private void deleteData() {
        editor.remove("nValue");
        editor.commit();
    }

    public void updateData() {

        editor.putBoolean("isShare",false);
        editor.putFloat("fRate",3.33f);
        editor.putInt("nValue",5000);
        editor.putString("name","copycoding.tistory");
        editor.apply();
    }

    public void saveData() {
        editor.putBoolean("isShare",true);
        editor.putFloat("fRate",1.33f);
        editor.putInt("nValue",100);
        editor.putString("name","copycoding");

        editor.apply();

    }

//    public void saveDepotData(String depotName){
//        editor.putString("depotSortname",depotName);
//        editor.apply();
//    }

    public void getFirebaseDatabase(){

        list_consignee=new ArrayList<String>();
        ArrayList<String> list_container=new ArrayList<String>();

        ValueEventListener sortListener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listItems.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Fine2IncargoList data=snapshot1.getValue(Fine2IncargoList.class);
                    String consigneeName=data.getConsignee();
                    String containerName=data.getContainer();
                    int containerNameLength=containerName.length();
                    Log.i("containerLength",consigneeName+containerNameLength);
                    if(containerNameLength==11){
                        String search_containerName=containerName.substring(containerName.length()-4,containerName.length());
                        if(search_containerName.equals("0589")){
                            listItems.add(data);
                        }else{}
                    }else{}


                    if(!list_consignee.contains(consigneeName)){
                    list_consignee.add(consigneeName);}
//                    listItems.add(data);
                }
                for(int i=0;i<list_consignee.size();i++ ){
                    String consigneeName2=list_consignee.get(i);
                    exr_text.append(consigneeName2+"\n");
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        databaseReference.addListenerForSingleValueEvent(sortListener);
        String[] arr_consignee=list_consignee.toArray(new String[list_consignee.size()]);
//        exr_text.setText(arr_consignee);
        int list_consignee_init=0;
//       for(String str:list_consignee){
//           exr_text.append(str+",");
//
//        }



//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                listItems.clear();
//                for(DataSnapshot snapshot1:snapshot.getChildren()){
//                    Fine2IncargoList data=snapshot1.getValue(Fine2IncargoList.class);
//                    listItems.add(data);
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }
}