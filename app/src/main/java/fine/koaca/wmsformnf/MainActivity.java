package fine.koaca.wmsformnf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ArrayList<List> listItems;
    ListAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView=findViewById(R.id.recyclerView_list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        listItems=new ArrayList<>();
        textView=findViewById(R.id.textView3);

        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("MnF");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
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
        });
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
//                Toast.makeText(getApplicationContext(), bl, Toast.LENGTH_SHORT).show();
                textView.setText(bl+"\n" +des);

            }
        });


        Button btn_etc=findViewById(R.id.btn_etc);
        btn_etc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location="a";
                locationSelect(location);
            }
        });
        Intent intent=getIntent();
        String str_location=intent.getStringExtra("location");
//
//        TextView textViewEtc=findViewById(R.id.textView_etc);
//        textViewEtc.setText(str_location);
        if(str_location !=null){

        }






    }

    public void locationSelect(String location){
//        Toast.makeText(this, location, Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(MainActivity.this,LocationA.class);
        startActivity(intent);
    }
    public void writeLoaction(String bl,String description,String location){

    }
}