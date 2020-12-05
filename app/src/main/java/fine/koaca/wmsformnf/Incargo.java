package fine.koaca.wmsformnf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
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

public class Incargo extends AppCompatActivity {
    ArrayList<ListIncargo> listItems;
    IncargoListAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    String sort="date";

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
        getFirebaseIncargoDatabase();
        adapter=new IncargoListAdapter(listItems,this);
        recyclerView.setAdapter(adapter);


    }
    public void getFirebaseIncargoDatabase(){
        ValueEventListener incargoListener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listItems.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ListIncargo data=dataSnapshot.getValue(ListIncargo.class);
                    listItems.add(data);
                }
//                Collections.reverse(listItems);
                listItems.sort(new IncargoListComparator().reversed());
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Data Server connection Error", Toast.LENGTH_SHORT).show();

            }

        };
        Query sortbyDate=databaseReference.orderByChild("consignee").equalTo("코만");

//     sortbyDate.orderByChild("consignee").equalTo("코만");
        sortbyDate.addListenerForSingleValueEvent(incargoListener);

    }
}