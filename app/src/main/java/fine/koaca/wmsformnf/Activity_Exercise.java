package fine.koaca.wmsformnf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_Exercise extends AppCompatActivity {
    RecyclerView exr_recyclerView;
    IncargoListAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    ArrayList<Fine2IncargoList> listItems=new ArrayList<Fine2IncargoList>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__exercise);

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

    }
}