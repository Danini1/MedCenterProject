package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import adapters.AdapterRemedios;
import model.Remedios;

import static com.example.projetomobile.R.layout.item_remedios;

public class RecycleView_Remedios extends AppCompatActivity {

    RecyclerView recview;
    DatabaseReference dr;
    ArrayList<Remedios> list;
    AdapterRemedios adapter;
    FloatingActionButton fb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view__remedios);
        setTitle("Remédios");



        recview = (RecyclerView) findViewById(R.id.reciview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<Remedios>();

        dr = FirebaseDatabase.getInstance().getReference().child("Remédios");
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshotl: dataSnapshot.getChildren()) {
                    Remedios r = dataSnapshotl.getValue(Remedios.class);
                    list.add(r);
                }
                adapter = new AdapterRemedios(RecycleView_Remedios.this, list);
                recview.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(RecycleView_Remedios.this, "Erro", Toast.LENGTH_SHORT).show();

            }
        });

        fb = (FloatingActionButton)findViewById(R.id.remedio);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });

    }
    public void voltarmenu (View view){

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private  void firebasesearch(String searchText)
    {
        String quary = searchText = searchText.toLowerCase();

        Query firebaseSearchQuery = dr.orderByChild("Remedios").startAt(quary).endAt(quary + "\uf8ff");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_remedio,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s.toString());
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }






}
