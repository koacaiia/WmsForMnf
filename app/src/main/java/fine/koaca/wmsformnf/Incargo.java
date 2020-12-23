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
    IncargoListAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    String sort="date";
    String sort_dialog="dialogsort";
    String sortConsignee;

    ArrayList<String> arrConsignee = new ArrayList<>();
    String [] consignee_list;
    String [] consignee_list2;

    String dataMessage;
    Button incargo_location;
    String str_sort="long";
    String str_sort_date="today_init";

    TextView incargo_incargo;
    String container40;
    String container20;
    String lclcargo;
    String inCargo;

    TextView dia_date;
    TextView dia_consignee;

    String day_start;
    String day_end;
    String dia_dateInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incargo);

        dataMessage = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        incargo_incargo=findViewById(R.id.incargo_incargo);
        recyclerView=findViewById(R.id.incargo_recyclerViewList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        listItems=new ArrayList<>();

        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("Incargo");

        adapter=new IncargoListAdapter(listItems,this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClicklistener(new OnListItemClickListener() {
            @Override
            public void onItemClick(Fine2IncargoListAdapter.ListViewHolder holder, View view, int position) {

            }
        });



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

                sort_dialog="dialogsort";
                str_sort_date="today_init";
                str_sort="long";
                dia_dateInit=new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                getFirebaseIncargoDatabase();
                return true;
            }
        });
        dia_dateInit=new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        getFirebaseIncargoDatabase();
    }

    public void getFirebaseIncargoDatabase(){
        ValueEventListener incargoListener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listItems.clear();
                arrConsignee.clear();


                ArrayList<Integer> list_40 = new ArrayList<Integer>();
                ArrayList<Integer> list_20=new ArrayList<Integer>();
                ArrayList<Integer> list_lclcargo=new ArrayList<Integer>();
                ArrayList<Integer> list_incargo=new ArrayList<Integer>();

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Fine2IncargoList data=dataSnapshot.getValue(Fine2IncargoList.class);


                    if(str_sort.equals("long")){
                        listItems.add(data);}
                    else if(str_sort.equals("sort")){
                        if(dia_consignee.getText().toString().equals("All")){
                            listItems.add(data);
                        }
                        if(data.getConsignee().equals(dia_consignee.getText().toString())){
                            listItems.add(data);
                        }else{}
                        dia_dateInit=dia_date.getText().toString();
                    }
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
                    arrConsignee.add("All");
                    arrConsignee.add(str_consignee);

                }
                consignee_list=arrConsignee.toArray(new String[arrConsignee.size()]);
                arrConsignee.clear();
                for(String item : consignee_list){
                    if(!arrConsignee.contains(item))
                        arrConsignee.add(item);}

                consignee_list2=arrConsignee.toArray(new String[arrConsignee.size()]);
                adapter.notifyDataSetChanged();
                if(sort_dialog.equals("dialogsort")){
                dialogMessage(container40,container20,lclcargo,inCargo,consignee_list2);

                }else if(sort_dialog.equals("init")){
                    dialogMessage(consignee_list2);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Data Server connection Error", Toast.LENGTH_SHORT).show();
            }
        };
        Query sortbyDate = null;

      switch(str_sort_date){
          case "total":
          sortbyDate = databaseReference.orderByChild("date");
          break;
          case "fixed1":
              sortbyDate=databaseReference.orderByChild("date").equalTo(dia_date.getText().toString());
              break;
          case "fixed2":
              sortbyDate=databaseReference.orderByChild("date").startAt(day_start).endAt(day_end);
              break;
          case "today_init":
              sortbyDate=
                      databaseReference.orderByChild("date").equalTo(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
      }
        sortbyDate.addListenerForSingleValueEvent(incargoListener);
    }

    private void dialogMessage(String[] consignee_list2) {
        final String fixedDate = "날짜지정";


        ArrayList<String> dateSelected=new ArrayList<String>();
        dateSelected.add("내일 전체화물 입고 일정");
        dateSelected.add(fixedDate);
        dateSelected.add("이번 주");
        dateSelected.add("다음 주");
        dateSelected.add("이번 달");
        dateSelected.add("전체");
        ArrayList selectedItems=new ArrayList();
        int defaultItem=0;
        selectedItems.add(defaultItem);
        this.consignee_list2=consignee_list2;

        String[] dateList=dateSelected.toArray(new String[dateSelected.size()]);
        AlertDialog.Builder builder=new AlertDialog.Builder(Incargo.this);
        View view=getLayoutInflater().inflate(R.layout.spinnerlist,null);
        Spinner sp=view.findViewById(R.id.incargo_spinner_listconsignee1);
        dia_date=view.findViewById(R.id.dia_date);
        dia_date.setText(dia_dateInit);
        dia_consignee=view.findViewById(R.id.dia_consignee);
        ArrayAdapter<String> consigneelistAdapter=new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, Incargo.this.consignee_list2);
        consigneelistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(consigneelistAdapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortConsignee=consignee_list2[position];
                Log.i("koaca_consignee",sortConsignee);
                dia_consignee.setText(sortConsignee);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setView(view);
        builder.create();
        AlertDialog ad=builder.create();
        builder.setSingleChoiceItems(dateList,defaultItem,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CalendarPick calendarPick=new CalendarPick();
                calendarPick.CalendarCall();

                switch(which){
                    case 0:
                        String tomorrow=calendarPick.date_tomorrow;
                        Log.i("datetomorrow",tomorrow);
                         dia_dateInit=tomorrow;
                         dia_consignee.setText("All");
                         str_sort_date="fixed1";
                         str_sort="sort";
                         sort_dialog="dialogsort";
                        getFirebaseIncargoDatabase();



                        break;
                    case 1:
                        String a="b";
                        str_sort_date="fixed1";
                        str_sort="sort";
                        dia_dateInit=dia_date.getText().toString();

                        DatePickerFragment datePickerFragment=new DatePickerFragment(a);
                        datePickerFragment.show(getSupportFragmentManager(),"datePicker");
                    break;
                    case 2:

                        str_sort_date="fixed2";
                        str_sort="sort";
                       day_start=calendarPick.date_mon;
                       day_end=calendarPick.date_sat;
                       dia_dateInit=day_start+"~"+day_end;

                        break;
                    case 3:

                        str_sort_date="fixed2";
                        str_sort="sort";
                        day_start=calendarPick.date_Nmon;
                        day_end=calendarPick.date_Nsat;
                        dia_dateInit=day_start+"~"+day_end;

                                             break;
                    case 4:
                        str_sort="sort";
                        str_sort_date="fixed2";
                        day_start=calendarPick.year+"-"+calendarPick.month+"-"+"01";
                        day_end=calendarPick.year+"-"+calendarPick.month+"-"+calendarPick.date_lastMonth;
                        dia_dateInit=day_start+"~"+day_end;
                        break;
                    case 5:
                       day_start="전체";
                       day_end="조회";
                       str_sort="long";
                       str_sort_date="total";
                       dia_dateInit="전화물 ";
                        break;
                }
                dia_date.setText(dia_dateInit);
            }
        });

        builder.setPositiveButton("조회", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sort_dialog="dialogsort";
                Toast.makeText(Incargo.this, "화주명:"+dia_consignee.getText().toString()+"\n"+"검색기간"+dia_date.getText().toString()+
                                "\n"+
                                "화물을 " +
                                "조회 합니다.",
                        Toast.LENGTH_LONG).show();
                getFirebaseIncargoDatabase();


            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setTitle("조건별 화물 조회");
        builder.show();
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
        dia_date.setText(dataMessage);

    }


    public void dialogMessage(String container40, String container20, String lclcargo, String inCargo, String[] consignee_list2){
        this.consignee_list2=consignee_list2;

        AlertDialog.Builder dialog=new AlertDialog.Builder(Incargo.this);
        dialog.setTitle(dia_dateInit+"__"+"입고 화물 현황");
        dialog.setMessage(container40 +"\n"+container20 +"\n"+ lclcargo+"\n"+inCargo);
        dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.setNegativeButton("전체 화물조회", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sort_dialog="dialogsort";
                str_sort_date="total";
                str_sort="long";
                dia_dateInit="전체";
                getFirebaseIncargoDatabase();

            }
        });
        dialog.setNeutralButton("조건별 화물 조회", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               str_sort_date="total";
                sort_dialog="init";
                str_sort="long";
                getFirebaseIncargoDatabase();
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