package fine.koaca.wmsformnf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//    ArrayList<List> listItems;
    ArrayList<Fine2IncargoList> listItems;
//    ListAdapter adapter;
    Fine2IncargoListAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    EditText textView_bl;
    EditText textView_des;
    EditText textView_loc;
    EditText editText_remark;
    TextView textView_date;
    EditText textView_count;
    TextView textView_container;
    EditText editText_incargo;

    EditText editText_delete;


    String ID;
    String bl;
    String description;
    String location;
    String date;
    String count;
    String container;
    String dataMessage;
    String selectedItemsText;
    String remark;
    String incargo;

    Button btn_databaseReg;
    Button btn_datalocation;
    Button btn_camera;
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
                sort="location";
                getFirebaseDatabase();
                return true;
            }
        });
        textView_date=findViewById(R.id.textView_date);
        textView_date.setOnClickListener(this);
        textView_date.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, "반입일 별 정렬 진행", Toast.LENGTH_SHORT).show();
                sort="date";
                getFirebaseDatabase();
                return true;
            }
        });
        textView_count=findViewById(R.id.textView_count);
        editText_remark=findViewById(R.id.edit_remark);
        editText_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               remark= editText_remark.getText().toString();
                editText_remark.setText(remark);
            }
        });
        textView_container=findViewById(R.id.textView_container_activity);
        editText_incargo=findViewById(R.id.editText_incargo);



        btn_databaseReg=findViewById(R.id.btn_databaseReg);
        btn_databaseReg.setOnClickListener(this);

        btn_datalocation=findViewById(R.id.btn_location);

        btn_datalocation.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String intentActivityName="Incargo";
                intentSelect(intentActivityName);

//                longClickItem();
                return true;
            }
        });
        btn_datalocation.setOnClickListener(this);
        btn_camera=findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(this);



        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("Incargo");

        adapter=new Fine2IncargoListAdapter(listItems,this);

        recyclerView.setAdapter(adapter);
        editText_delete=new EditText(this);



        adapter.setOnItemClicklistener(new OnListItemClickListener() {
            @Override
            public void onItemClick(Fine2IncargoListAdapter.ListViewHolder holder, View view, int position) {
                String bl=listItems.get(position).getBl();
                String des=listItems.get(position).getDescription();
                String loc=listItems.get(position).getLocation();
                String date=listItems.get(position).getDate();
                String count=listItems.get(position).getCount();
                String remark=listItems.get(position).getRemark();
                String container=listItems.get(position).getContainer();
                String incargo=listItems.get(position).getIncargo();

                textView_bl.setText(bl);
                textView_des.setText(des);
                textView_date.setText(date);
                textView_loc.setText(loc);
                textView_count.setText(count);
                editText_remark.setText(remark);
                textView_container.setText(container);
                editText_incargo.setText(incargo);
                           }
        });
        adapter.setLongClickListener(new OnItemLongClickListener() {

            @Override
            public void onLongItemClick(Fine2IncargoListAdapter.ListViewHolder listViewHolder, View v, int pos) {
                bl=textView_bl.getText().toString();
                description=textView_des.getText().toString();
                location=textView_loc.getText().toString();




                AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("데이터 삭제,화물조회 ")

                        .setMessage("해당 BL 화물에 대한 자료 삭제,조회"+"\n"+bl+"\n" +
                                description+"\n"+location)

                        .setPositiveButton("자료삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                AlertDialog.Builder dialog_Delete=new AlertDialog.Builder(MainActivity.this);
                                dialog_Delete.setTitle("자료삭제 선택창")
                                        .setMessage("하기 해당 화물에 대한 자료변경을 진행 합니다.화물정보 다신 한번 확인후 진행 바랍니다."+"\n"+bl+"\n" + description+
                                                "\n"+location)
                                        .setView(editText_delete)
                                        .setPositiveButton("자료삭제", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String str_delete=editText_delete.getText().toString();
                                                if(str_delete.equals("02010027")){
                                                postFirebaseDatabase(false);
                                                getFirebaseDatabase();
                                                Toast.makeText(MainActivity.this, "Removed Data", Toast.LENGTH_SHORT).show();}
                                                else{
                                                    Toast.makeText(MainActivity.this, "삭제 권한이 없습니다.", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }

                                            }
                                        })
                                        .setNegativeButton("삭제취소", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(MainActivity.this, "Cancel Removed Data", Toast.LENGTH_SHORT).show();
                                            }
                                        })

                                        .create()
                                .show();


                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "취소 하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNeutralButton("화물조회", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(MainActivity.this,WebList.class);
                                String data_bl=textView_bl.getText().toString();
                                intent.putExtra("bl",data_bl);
                                startActivity(intent);


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
        String str_date=intent.getStringExtra("date");
        String str_count=intent.getStringExtra("count");
        String str_remark=intent.getStringExtra("remark");
        String str_container=intent.getStringExtra("container");
        String str_incargo=intent.getStringExtra("incargo");
        if(str_location !=null){
            textView_bl.setText(str_bl);
            textView_des.setText(str_des);
            textView_loc.setText(str_location);
            textView_date.setText(str_date);
            textView_count.setText(str_count);
            editText_remark.setText(str_remark);
            textView_container.setText(str_container);
            editText_incargo.setText(str_incargo);
        }
        getFirebaseDatabase();

        }


        public void getFirebaseDatabase(){

            ValueEventListener postListener=new ValueEventListener(){

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listItems.clear();

                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Fine2IncargoList data=dataSnapshot.getValue(Fine2IncargoList.class);

                        listItems.add(data);

                    }
//                    Collections.reverse(listItems);

                    listItems.sort(new IncargoListComparator().reversed());
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Data Server connection Error", Toast.LENGTH_SHORT).show();

                }
            };

            Query sortbyAge=databaseReference.orderByChild("consignee").equalTo("코만");

            sortbyAge.addListenerForSingleValueEvent(postListener);

        }


    public void intentSelect(String className){
        Intent intent=new Intent();
        switch(className){
            case "Location":

        intent=new Intent(MainActivity.this,Location.class);
                break;
            case "Incargo":
                intent=new Intent(MainActivity.this,Incargo.class);
                break;
            case "CameraCapture":
        intent=new Intent(MainActivity.this,CameraCapture.class);
        break;}
        String data_bl=textView_bl.getText().toString();
        String data_description=textView_des.getText().toString();
        String data_date=textView_date.getText().toString();
        String data_count=textView_count.getText().toString();
        String data_remark=editText_remark.getText().toString();
        String data_container=textView_container.getText().toString();
        String data_incargo=editText_incargo.getText().toString();

        intent.putExtra("bl",data_bl);
        intent.putExtra("des",data_description);
        intent.putExtra("date",data_date);
        intent.putExtra("count",data_count);
        intent.putExtra("remark",data_remark);
        intent.putExtra("container",data_container);
        intent.putExtra("incargo",data_incargo);
        startActivity(intent);

    }
    public void postFirebaseDatabase(boolean add){

        Map<String,Object> childUpdates=new HashMap<>();
        Map<String,Object> postValues=null;
        if(add){
            Fine2IncargoList list=new Fine2IncargoList(bl,description,location,date,count,remark,container,incargo);
            postValues=list.toMap();
             }
        childUpdates.put(bl+"_"+description+"_"+count+"/",postValues);
        databaseReference.updateChildren(childUpdates);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        String intentActivityName;
        switch(v.getId()){
            case R.id.btn_databaseReg:

                bl=textView_bl.getText().toString();
                description=textView_des.getText().toString();
                location=textView_loc.getText().toString();
                date=textView_date.getText().toString();
                count=textView_count.getText().toString();
                remark=editText_remark.getText().toString();
                container=textView_container.getText().toString();
                incargo=editText_incargo.getText().toString();
                if(bl.equals("") || description.equals("") || location .equals("")|| date.equals("") ||count.equals("")){
                    Toast.makeText(this, "등록 항목누락! 목록 다시한번 확인 바랍니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                postFirebaseDatabase(true);
                getFirebaseDatabase();
                break;

            case R.id.textView_date:
               DialogFragment newFragment=new DatePickerFragment();
               newFragment.show(getSupportFragmentManager(),"datePicker");
                break;

            case R.id.btn_location:
                intentActivityName="Location";
                intentSelect(intentActivityName);
                break;

            case R.id.btn_camera:

                intentActivityName="CameraCapture";
                intentSelect(intentActivityName);
                break;

        }

    }


    public void processDatePickerResult(int year, int month, int dayOfMonth) {
        String month_string=Integer.toString(month+1);
        String day_string=Integer.toString(dayOfMonth);
        String year_string=Integer.toString(year);

        dataMessage=(year_string+"/"+month_string+"/"+day_string);
        textView_date.setText(dataMessage);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void longClickItem(){
        bl=textView_bl.getText().toString();
        description=textView_des.getText().toString();
        location=textView_loc.getText().toString();

        if(bl.equals("")){
            Toast.makeText(this, "항목선택 바랍니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        final java.util.List<String> listItems = new ArrayList<>();

        listItems.add("수입화물 진행정보조회");
        listItems.add("해당항목 자료삭제");
        CharSequence[] items_add=listItems.toArray(new String[listItems.size()]);
        final EditText editText_remark=new EditText(this);
        final java.util.List SelectedItems=new ArrayList();
        int defaultItem=0;
        SelectedItems.add(defaultItem);

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("하단 비엘 항목 조작 선텍");
                builder.setView(editText_remark);
                builder.setSingleChoiceItems(items_add,defaultItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SelectedItems.clear();
                        SelectedItems.add(which);
                        selectedItemsText=items_add[which].toString();
                        Toast.makeText(MainActivity.this, selectedItemsText, Toast.LENGTH_SHORT).show();

                    }
                });

                builder.setPositiveButton("비고란 등록", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        remark=editText_remark.getText().toString();
                        Toast.makeText(MainActivity.this, remark, Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "진행이 취소 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNeutralButton("항목선택", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(selectedItemsText){
                            case "비고란 자료추가":
                                Toast.makeText(MainActivity.this, "take Picture", Toast.LENGTH_SHORT).show();
                                break;
                            case "해당항목 자료삭제":
                                AlertDialog.Builder dialog_Delete=new AlertDialog.Builder(MainActivity.this);
                                dialog_Delete.setTitle("자료삭제선택창")
                                        .setMessage("하기 해당 화물에 대한 자료변경을 진행 합니다.화물정보 다신 한번 확인후 진행 바랍니다."+"\n"+bl+"\n" + description+
                                                "\n"+location)
                                        .setPositiveButton("자료삭제", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                postFirebaseDatabase(false);
                                                getFirebaseDatabase();
                                                Toast.makeText(MainActivity.this, "Removed Data", Toast.LENGTH_SHORT).show();

                                            }
                                        })
                                .setNegativeButton("삭제취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(MainActivity.this, "Cancel Removed Data", Toast.LENGTH_SHORT).show();
                                }
                            });
                                break;
                            case "수입화물 진행정보조회":
                                Intent intent=new Intent(MainActivity.this,WebList.class);
                                String data_bl=textView_bl.getText().toString();
                                intent.putExtra("bl",data_bl);
                                startActivity(intent);
                                break;
                            case "기초자료 변경":

                                DialogFragment newFragment=new DatePickerFragment();
                                newFragment.show(getSupportFragmentManager(),"datePicker");
                                break;
                        }
                        }


                });
                builder.show();

    }


}