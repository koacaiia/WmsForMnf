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

    static private String SHARE_NAME="SHARE_PREF";
    static SharedPreferences sharedPref=null;
    static SharedPreferences.Editor editor=null;
    TextView exr_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__exercise);

        exr_text=findViewById(R.id.exr_text);

        exr_recyclerView=findViewById(R.id.exr_recyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        exr_recyclerView.setLayoutManager(layoutManager);
        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("Incargo");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listItems.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Fine2IncargoList data=snapshot1.getValue(Fine2IncargoList.class);
                    listItems.add(data);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter=new IncargoListAdapter(listItems,this);
        exr_recyclerView.setAdapter(adapter);
        Toast.makeText(this, String.valueOf(listItems.size()), Toast.LENGTH_SHORT).show();
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("koaca");
        dialog.show();

        sharedPref=getSharedPreferences(SHARE_NAME,MODE_PRIVATE);
        editor= sharedPref.edit();
    }

    public void btnClick(View view){
        int btnName=view.getId();
        switch(btnName){
            case R.id.exr_save:
                String depotName="";
                saveData(depotName);
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
        exr_text.setText(dataList);
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

    public void saveData(String depotName) {
        editor.putBoolean("isShare",true);
        editor.putFloat("fRate",1.33f);
        editor.putInt("nValue",100);
        editor.putString("name","copycoding");
        editor.putString("depotSortName",depotName);
        editor.apply();

    }
}