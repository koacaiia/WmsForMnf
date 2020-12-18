package fine.koaca.wmsformnf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
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

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class Incargo extends AppCompatActivity implements Serializable {
    ArrayList<Fine2IncargoList> listItems;
    static ArrayList<Fine2IncargoList> arr_listItems=new ArrayList<>();
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

    String dataMessage;
    String b;

    RadioGroup dateSelect;
    RadioButton rBtn_fixDate;
    RadioButton rBtn_total;

    Button btn_sort;
    Button incargo_location;
    String str_sort="sort";
    String str_sort_date="total";

    TextView incargo_40ft;
    TextView incargo_20ft;
    TextView incargo_lclcargo;
    TextView incargo_incargo;

    String container40;
    String container20;
    String lclcargo;
    String inCargo;

    String nowDated = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incargo);

        dataMessage = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

//        incargo_40ft=findViewById(R.id.incargo_40ft);
//        incargo_20ft=findViewById(R.id.incargo_20ft);
//        incargo_lclcargo=findViewById(R.id.incargo_lcl);
        incargo_incargo=findViewById(R.id.incargo_incargo);

        rBtn_fixDate=findViewById(R.id.rBtn_FixDate);
        rBtn_total=findViewById(R.id.rBtn_Total);

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

        getFirebaseIncargoDatabase();

//        btn_sort=findViewById(R.id.btn_sort);
//        btn_sort.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               str_sort="consignee";
//               getFirebaseIncargoDatabase();
//            }
//        });
//        btn_sort.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                getFirebaseIncargoDatabase();
//
//
//                rBtn_total.setChecked(false);
//
//                str_sort="long";
//                return true;
//            }
//        });

        dateSelect=findViewById(R.id.radion_dateSelect);
        dateSelect.setOnCheckedChangeListener(radioListener);
        rBtn_fixDate.setText(nowDated);

        incargo_location=findViewById(R.id.incargo_location);
        incargo_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Incargo.this,MainActivity.class);
                startActivity(intent);
                Toast.makeText(Incargo.this, "엠엔에프 로케이션 Activity 실행", Toast.LENGTH_SHORT).show();
            }
        });
        incargo_location.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent=new Intent(Incargo.this,Activity_Exercise.class);
                startActivity(intent);
                dialogMessage(container40,container20,lclcargo,inCargo, consignee_list2);
                return true;
            }
        });


    }

    public void getFirebaseIncargoDatabase(){
        ValueEventListener incargoListener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listItems.clear();

                String date_list=rBtn_fixDate.getText().toString();
                ArrayList<Integer> list_40 = new ArrayList<Integer>();
                ArrayList<Integer> list_20=new ArrayList<Integer>();
                ArrayList<Integer> list_lclcargo=new ArrayList<Integer>();
                ArrayList<Integer> list_incargo=new ArrayList<Integer>();

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Fine2IncargoList data=dataSnapshot.getValue(Fine2IncargoList.class);
                    if(str_sort.equals("consignee")&& !str_sort_date.equals("total")){
                      if(str_sort_date.equals("fixed")){
                        if(data.getDate().equals(date_list)){
                            listItems.add(data);
                            arr_listItems.add(data);
                          }}
                    }else{
                        arr_listItems.add(data);
                    listItems.add(data);}
                }

                Collections.reverse(listItems);
                int listItems_count=listItems.size();
                int sum40=0;
                int sum20=0;
                int lcl=0;
                int sumIncargo=0;
                for(int i=0;i<listItems_count;i++){
                    String str_consignee=listItems.get(i).getConsignee();
                    int int_40=Integer.parseInt(listItems.get(i).getContainer40());
                    int int_20=Integer.parseInt(listItems.get(i).getContainer20());
                    int int_lclcargo=Integer.parseInt(listItems.get(i).getLclcargo());
                    int int_incargo=Integer.parseInt(listItems.get(i).getIncargo());
                    list_40.add(int_40);
                    list_20.add(int_20);
                    list_lclcargo.add(int_lclcargo);
                    list_incargo.add(int_incargo);
                    sum40=sum40+list_40.get(i);
                    sum20=sum20+list_20.get(i);
                    lcl=lcl+list_lclcargo.get(i);
                    sumIncargo=sumIncargo+list_incargo.get(i);

                    container40="40FT*"+sum40;
                    container20="20FT*"+sum20;
                    lclcargo="LcL화물:"+lcl+"PLT";
                    inCargo="총 입고 팔렛트 수량:"+sumIncargo+"(PLT)";

//                    incargo_40ft.setText(container40);
//                    incargo_20ft.setText(container20);
//                    incargo_lclcargo.setText(lclcargo);

                    Log.i("koaca_incargo",inCargo);

                    arrConsignee.add("All");
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
                        text_listConsignee.setText(""); }
                });


                adapter.notifyDataSetChanged();
                dialogMessage(container40,container20,lclcargo,inCargo,consignee_list2);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Data Server connection Error", Toast.LENGTH_SHORT).show();
            }
        };
        String consignee_list=text_listConsignee.getText().toString();

        Query sortbyDate;
        if(str_sort.equals("long")) {
            sortbyDate = databaseReference.orderByChild(sort);

        }else if(str_sort.equals("consignee")){
            if(text_listConsignee.getText().toString().equals("All")||text_listConsignee.getText().toString().equals("")){
                sortbyDate=databaseReference.orderByChild("date");
            }else if(str_sort_date.equals("total")){
                sortbyDate=databaseReference.orderByChild("consignee").equalTo(consignee_list);
            }else{
                sortbyDate=databaseReference.orderByChild("consignee").equalTo(consignee_list);
            }
        }
        else{
            sortbyDate = databaseReference.orderByChild(sort).equalTo(nowDated);
        }
        sortbyDate.addListenerForSingleValueEvent(incargoListener);




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
           rBtn_fixDate.setText(dataMessage);
           break;

       }
    }
    RadioGroup.OnCheckedChangeListener radioListener= new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if(checkedId==R.id.rBtn_Total){
                str_sort_date="total";
            }else if(checkedId==R.id.rBtn_FixDate){
                str_sort_date="fixed";
                b="b";
                DialogFragment newFragment=new DatePickerFragment(b);
                newFragment.show(getSupportFragmentManager(),"datePicker");
                rBtn_fixDate.setChecked(false);
            }else {
                Toast.makeText(Incargo.this, "미구현 Event 입니다.", Toast.LENGTH_SHORT).show();

            }

        }
    };

    public void dialogMessage(String container40, String container20, String lclcargo, String inCargo, String[] listItems){
        String datedFixed=rBtn_fixDate.getText().toString();
        ArrayList<String> list_container=new ArrayList<>();
        list_container.add(container40);
        list_container.add(container20);
        list_container.add(lclcargo);

        String[] items_container=list_container.toArray(new String[list_container.size()]);

        AlertDialog.Builder dialog=new AlertDialog.Builder(Incargo.this);
        dialog.setTitle(datedFixed+"__"+"입고 화물 현황");
        dialog.setMessage(container40 +"\n"+container20 +"\n"+ lclcargo+"\n"+inCargo);
//        dialog.setItems(items_container, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
        dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Incargo.this, "입고확인", Toast.LENGTH_SHORT).show();


            }
        });
        dialog.setNegativeButton("전화물조회", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                str_sort="consignee";
                getFirebaseIncargoDatabase();


            }
        });
        dialog.setNeutralButton("조건별 화물 조회", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Incargo.this);
                View view=getLayoutInflater().inflate(R.layout.spinnerlist,null);
                Spinner sp=(Spinner)view.findViewById(R.id.incargo_spinner_listconsignee1);
                ArrayAdapter<String> consigneelistAdapter=new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item,consignee_list2);
                consigneelistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp.setAdapter(consigneelistAdapter);

                builder.setView(view);
                builder.create();
                builder.show();




            }
        });

        dialog.show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }



}