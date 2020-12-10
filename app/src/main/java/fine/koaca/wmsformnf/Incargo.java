package fine.koaca.wmsformnf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

public class Incargo extends AppCompatActivity {
    ArrayList<Fine2IncargoList> listItems;
    ArrayList<Fine2IncargoList> sortArr;
    IncargoListAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    String sort="date";
    String sortConsignee;
    Spinner listConsignee;
    TextView text_listConsignee;

    ArrayList<String> arrConsignee = new ArrayList<>();
    String [] consignee_list;
    String [] consignee_list2;

    TextView date_Start;
    TextView date_End;

    String dataMessage;
    String b;

    RadioGroup dateSelect;

    String nowDated;



    Button btn_koaca;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incargo);

        dataMessage = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

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

        date_End=findViewById(R.id.textView_dateEnd);
        date_End.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b="c";
                DialogFragment newFragment=new DatePickerFragment(b);
                newFragment.show(getSupportFragmentManager(),"datePicker");
            }
        });
        date_Start=findViewById(R.id.textView_dateStart);
        date_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b="b";
                DialogFragment newFragment=new DatePickerFragment(b);
                newFragment.show(getSupportFragmentManager(),"datePicker");
            }
        });

        getFirebaseIncargoDatabase();

        Button btn_sort=findViewById(R.id.btn_sort);
        btn_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSortFirebasedata();
            }
        });
        btn_sort.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                getFirebaseIncargoDatabase();
                return true;
            }
        });

        dateSelect=findViewById(R.id.radion_dateSelect);
        dateSelect.setOnCheckedChangeListener(radioListener);

        btn_koaca=findViewById(R.id.btn_koaca);
        btn_koaca.setOnClickListener(btnlistener);


        nowDated = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

       date_Start.setText(nowDated);
        Log.i("nowDated",nowDated);

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

                ArrayAdapter<String> consigneeAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,
                        consignee_list2);
                consigneeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                listConsignee.setAdapter(consigneeAdapter);
                listConsignee.setSelected(false);
                listConsignee.setSelection(0,true);
                listConsignee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        sortConsignee= consignee_list2[position];
                        text_listConsignee.setText(sortConsignee);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        listConsignee.setTag("");
                        text_listConsignee.setText("");

                    }
                });

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Data Server connection Error", Toast.LENGTH_SHORT).show();

            }

        };

//    nowDated=date_Start.getText().toString();
        String nowDated1 = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
//        String nowDated1=date_Start.getText().toString();
        Query sortbyDate=databaseReference.orderByChild(sort).equalTo(nowDated1);
        sortbyDate.addListenerForSingleValueEvent(incargoListener);

    }
    public void getSortFirebasedata(){


        ValueEventListener postListener=new ValueEventListener( ){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listItems.clear();
                String date_list=date_Start.getText().toString();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Fine2IncargoList data=dataSnapshot.getValue(Fine2IncargoList.class);
                    if(data.getDate().equals(date_list)){
                    listItems.add(data);}
                }
//                listItems.sort()


//                listItems.sort(new IncargoListComparator(sort).reversed());
////consignee_list2=arrConsignee.toArray(new String[arrConsignee.size()]);
//                int listItems_size=listItems.size();
////                String sort_date=date_Start.getText().toString();
//                String sort_date="2020-12-08";
//                Log.i("koaca_datesize",Integer.toString(listItems_size));
//                Log.i("koaca_date",sort_date);
//////
//                for(int i=0;i<listItems_size;i++){
//                    if( ! sort_date.equals(listItems.get(i).getDate())){
//                        Log.i("koaca_delete"+i,listItems.get(i).getDate());
//                        listItems.remove(i);
//                    }
//                    listItems_size--;
//                    i--;
//                }
////
////                Iterator iter = listItems.iterator();
////                while(iter.hasNext()) {
////                    if( ! sort_date.equals(iter.next())) { iter.remove(); } }


                   adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        String consignee_list=text_listConsignee.getText().toString();


if(text_listConsignee.getText().toString().equals("")){
    Query sortbySort=databaseReference.orderByChild("date").equalTo(date_Start.getText().toString());
    sortbySort.addListenerForSingleValueEvent(postListener);
    Log.i("ifCheck","true");


}else{
    Query sortbySort=databaseReference.orderByChild("consignee").equalTo(consignee_list);
    sortbySort.addListenerForSingleValueEvent(postListener);
    Log.i("ifChck","false");
    Log.i("ifCheck",text_listConsignee.getText().toString());
}




        Log.i("koacaDate",dataMessage);


        Toast.makeText(this, dataMessage, Toast.LENGTH_SHORT).show();

    }
    public void processDatePickerResult(int year, int month, int dayOfMonth) {
        String month_string=Integer.toString(month+1);
        String day_string;
        if(dayOfMonth<10){
             day_string="0"+Integer.toString(dayOfMonth);
        }else{
           day_string=Integer.toString(dayOfMonth);
        }
        String year_string=Integer.toString(year);
        dataMessage=(year_string+"-"+month_string+"-"+day_string);
        SimpleDateFormat transFormat=new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date to=transFormat.parse(dataMessage);
            Log.i("kocaDate",dataMessage);
            Log.i("koacaDatetrance",transFormat.format(to));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        switch(b){
           case "b":
           date_Start.setText(dataMessage);
           break;
           case "c":
               date_End.setText(dataMessage);
               break;

       }


    }
    RadioGroup.OnCheckedChangeListener radioListener= new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

        }
    };

    Button.OnClickListener btnlistener=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

           CalendarPick calendarPick=new CalendarPick();
           calendarPick.CalendarCall();


        }
    };




}