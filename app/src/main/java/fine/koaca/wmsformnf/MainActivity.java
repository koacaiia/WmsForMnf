package fine.koaca.wmsformnf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<List> listItems;
    ListAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    EditText textView_bl;
    EditText textView_des;
    EditText textView_loc;
    TextView textView_date;

    String ID;
    String bl;
    String description;
    String location;
    String date;
    EditText etc;
    String dataMessage;

    Button btn_databaseReg;
    Button btn_dataInsert;
    Button btn_dataDelete;
    Button btn_webList;
    String sort="date";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView=findViewById(R.id.recyclerView_list);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        listItems=new ArrayList<>();
        textView_bl=findViewById(R.id.textView3);
        textView_bl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, "B/L 별 정렬진행", Toast.LENGTH_SHORT).show();
                sort="bl";
                getFirebaseDatabase();
                return true;
            }
        });
        textView_des=findViewById(R.id.textView4);
        textView_des.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, "품목별 정렬 진행", Toast.LENGTH_SHORT).show();
                sort="description";
                getFirebaseDatabase();
                return true;
            }
        });
        textView_loc=findViewById(R.id.textView5);
        textView_loc.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, "location 별 정렬 진행", Toast.LENGTH_SHORT).show();
                sort="loc";
                getFirebaseDatabase();
                return true;
            }
        });
        textView_date=findViewById(R.id.textView_date);
        textView_date.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, "반입일 별 정렬 진행", Toast.LENGTH_SHORT).show();
                sort="date";
                getFirebaseDatabase();
                return true;
            }
        });



        btn_databaseReg=findViewById(R.id.btn_databaseReg);
        btn_databaseReg.setOnClickListener(this);
        btn_dataInsert=findViewById(R.id.btn_date);
        btn_dataInsert.setOnClickListener(this);
        btn_dataDelete=findViewById(R.id.btn_dataDelete);
        btn_dataDelete.setOnClickListener(this);
        btn_webList=findViewById(R.id.btn_web);
        btn_webList.setOnClickListener(this);


        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("MnF");

//        database=FirebaseDatabase.getInstance();
//        databaseReference=database.getReference("MnF");
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                listItems.clear();
//                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
//                    List data=dataSnapshot.getValue(List.class);
//                    listItems.add(data);
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(MainActivity.this, "Data Server connection Error", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//        adapter=new ListAdapter(listItems);
//        List items=new List("q123","qwesadf");
//        listItems.add(items);
        adapter=new ListAdapter(listItems,this);
        recyclerView.setAdapter(adapter);



        adapter.setOnItemClicklistener(new OnListItemClickListener() {
            @Override
            public void onItemClick(ListAdapter.ListViewHolder holder, View view, int position) {
                String bl=listItems.get(position).getBl();
                String des=listItems.get(position).getDescription();
                String loc=listItems.get(position).getLocation();
                String date=listItems.get(position).getDate();
//
                textView_bl.setText(bl);
                textView_des.setText(des);
                textView_date.setText(date);
                textView_loc.setText(loc);

            }
        });
        adapter.setLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onLongItemClick(ListAdapter.ListViewHolder listViewHolder, View v, int pos) {
                bl=textView_bl.getText().toString();
                description=textView_des.getText().toString();
                location=textView_loc.getText().toString();

                AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("데이터 삭제")
                        .setMessage("해당 데이터를 삭제 하시겠습니까?"+"\n"+bl+"\n" +
                                description+"\n"+location)
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                postFirebaseDatabase(false);
                                getFirebaseDatabase();
                                Toast.makeText(MainActivity.this, "Removed Data", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "Cancel Removed Data", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create()
                        .show();

            }
        });

        Intent intent=getIntent();
        String str_location=intent.getStringExtra("location");
        String str_bl=intent.getStringExtra("bl");
        String str_des=intent.getStringExtra("des");
//
//        TextView textViewEtc=findViewById(R.id.textView_etc);
//        textViewEtc.setText(str_location);
        if(str_location !=null){
            textView_bl.setText(str_bl);
            textView_des.setText(str_des);
            textView_loc.setText(str_location);
        }
//        if(savedInstanceState !=null){
//            String data=savedInstanceState.getString("data");
//            textView.setText(data);
//        }else{
//            Toast.makeText(this, "18", Toast.LENGTH_SHORT).show();
        getFirebaseDatabase();
        }

//

        public void getFirebaseDatabase(){

//            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {



            ValueEventListener postListener=new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listItems.clear();
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        List data=dataSnapshot.getValue(List.class);
                        listItems.add(data);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Data Server connection Error", Toast.LENGTH_SHORT).show();

                }
            };

            Query sortbyAge=databaseReference.orderByChild(sort);
            sortbyAge.addListenerForSingleValueEvent(postListener);

        }


    public void locationSelect(String location){
//        Toast.makeText(this, location, Toast.LENGTH_SHORT).show();
//        Intent intent=new Intent(MainActivity.this,LocationA.class);
        Intent intent=new Intent(MainActivity.this,Location.class);
        String data_bl=textView_bl.getText().toString();
        String data_description=textView_des.getText().toString();
        intent.putExtra("bl",data_bl);
        intent.putExtra("des",data_description);
        startActivity(intent);
    }
    public void postFirebaseDatabase(boolean add){

        Map<String,Object> childUpdates=new HashMap<>();
        Map<String,Object> postValues=null;
        if(add){
            List list=new List(bl,description,location,date);
            postValues=list.toMap();
             }
        childUpdates.put(bl+"_"+description+"/",postValues);
        databaseReference.updateChildren(childUpdates);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
            super.onSaveInstanceState(outState);
            String data=textView_bl
                    .getText().toString();
            outState.putString("data",data);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_databaseReg:

                bl=textView_bl.getText().toString();
                description=textView_des.getText().toString();
                location=textView_loc.getText().toString();
                date=textView_date.getText().toString();
//                Toast.makeText(this, location, Toast.LENGTH_SHORT).show();
                postFirebaseDatabase(true);
                getFirebaseDatabase();
                break;

            case R.id.btn_date:
               DialogFragment newFragment=new DatePickerFragment();
               newFragment.show(getSupportFragmentManager(),"datePicker");
                break;

            case R.id.btn_dataDelete:
                String location="a";
                locationSelect(location);

                break;

            case R.id.btn_web:
                Intent intent=new Intent(MainActivity.this,WebList.class);
                String data_bl=textView_bl.getText().toString();
                intent.putExtra("bl",data_bl);
                startActivity(intent);
        }

    }


    public void processDatePickerResult(int year, int month, int dayOfMonth) {
        String month_string=Integer.toString(month+1);
        String day_string=Integer.toString(dayOfMonth);
        String year_string=Integer.toString(year);

        dataMessage=(year_string+"/"+month_string+"/"+day_string);
        textView_date.setText(dataMessage);


    }
}