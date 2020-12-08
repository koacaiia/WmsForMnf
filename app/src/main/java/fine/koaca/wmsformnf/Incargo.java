package fine.koaca.wmsformnf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class Incargo extends AppCompatActivity {
    ArrayList<Fine2IncargoList> listItems;
    IncargoListAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    String sort="date";
    Spinner listConsignee;
    TextView text_listConsignee;

    ArrayList<String> arrConsignee = new ArrayList<>();
    String [] consignee_list;
    String [] consignee_list2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incargo);

        recyclerView=findViewById(R.id.incargo_recyclerViewList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        listItems=new ArrayList<>();

        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("Incargo");
//        getFirebaseIncargoDatabase();
        adapter=new IncargoListAdapter(listItems,this);
        recyclerView.setAdapter(adapter);


        listConsignee=findViewById(R.id.incargo_spinner_listconsignee);
        text_listConsignee=findViewById(R.id.incargo_listconsignee);




        ArrayAdapter<String> consigneeAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,
                consignee_list2);
        consigneeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listConsignee.setAdapter(consigneeAdapter);
        listConsignee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sort=consignee_list2[position];
                text_listConsignee.setText(sort);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ValueEventListener incargoListener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listItems.clear();

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Fine2IncargoList data=dataSnapshot.getValue(Fine2IncargoList.class);
                    listItems.add(data);
                }
                Collections.reverse(listItems);
                int listItems_count=listItems.size();
                for(int i=0;i<listItems_count;i++){
                    String str_consignee=listItems.get(i).getConsignee();
                    Log.i("koaca",str_consignee);
                    arrConsignee.add(str_consignee);
                }
                consignee_list=arrConsignee.toArray(new String[arrConsignee.size()]);
                arrConsignee.clear();
                for(String item : consignee_list){
                    if(!arrConsignee.contains(item))
                        arrConsignee.add(item);}
                consignee_list2=arrConsignee.toArray(new String[arrConsignee.size()]);


                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Data Server connection Error", Toast.LENGTH_SHORT).show();

            }

        };

        Query sortbyDate=databaseReference.orderByChild(sort);
        sortbyDate.addListenerForSingleValueEvent(incargoListener);

    }



    public void getFirebaseIncargoDatabase(){
        ValueEventListener incargoListener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listItems.clear();

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Fine2IncargoList data=dataSnapshot.getValue(Fine2IncargoList.class);
                    listItems.add(data);
                }
                Collections.reverse(listItems);
                int listItems_count=listItems.size();
                for(int i=0;i<listItems_count;i++){
                    String str_consignee=listItems.get(i).getConsignee();
                    Log.i("koaca",str_consignee);
                    arrConsignee.add(str_consignee);
                }
                consignee_list=arrConsignee.toArray(new String[arrConsignee.size()]);
                arrConsignee.clear();
                for(String item : consignee_list){
                    if(!arrConsignee.contains(item))
                        arrConsignee.add(item);}
                consignee_list2=arrConsignee.toArray(new String[arrConsignee.size()]);


                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Data Server connection Error", Toast.LENGTH_SHORT).show();

            }

        };

        Query sortbyDate=databaseReference.orderByChild(sort);
        sortbyDate.addListenerForSingleValueEvent(incargoListener);

    }


}