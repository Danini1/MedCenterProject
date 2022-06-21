package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import adapters.AdapterVacinas;
import model.Vacinas;

public class RecycleViewVacina extends AppCompatActivity {

    RecyclerView recview;
    DatabaseReference dr;
    DatabaseReference tomadas;
    ArrayList<Vacinas> list;
    AdapterVacinas adapter;
    FloatingActionButton fb;
   Button Vacina;
    Button V;
    Button NaoTomadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view_vacina);

        recview = (RecyclerView) findViewById(R.id.reciview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<Vacinas>();
        Vacina = findViewById(R.id.vacinas);
        V = findViewById(R.id.Vacinatomada);
        NaoTomadas = findViewById(R.id.VacinaNtomada);

        dr = FirebaseDatabase.getInstance().getReference().child("Vacinas");
        RecuperarLista();

        tomadas = FirebaseDatabase.getInstance().getReference().child("Vacinas Tomadas");

        Vacina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecuperarLista();
            }
        });


        V.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                dr.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot dataSnapshotl: dataSnapshot.getChildren()) {
                            Vacinas r = dataSnapshotl.getValue(Vacinas.class);
                            String UidVacina = r.getUid();

                                Query query = tomadas.orderByChild("Uid").equalTo(UidVacina);

                                query.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot ds: snapshot.getChildren()) {
                                            Vacinas tomadas = ds.getValue(Vacinas.class);
                                            list.add(tomadas);

                                        }
                                        adapter = new AdapterVacinas(RecycleViewVacina.this, list);
                                        recview.setAdapter(adapter);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                        }

                        //   LinearLayoutManager layoutManager = new LinearLayoutManager(RecycleViewVacina.this, LinearLayoutManager.HORIZONTAL, false);
                        //  recview.setLayoutManager(layoutManager);]

                        GridLayoutManager gridLayoutManager = new GridLayoutManager(RecycleViewVacina.this, 2);
                        recview.setLayoutManager(gridLayoutManager);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(RecycleViewVacina.this, "Erro", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        NaoTomadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                dr.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot dataSnapshotl: dataSnapshot.getChildren()) {
                            Vacinas r = dataSnapshotl.getValue(Vacinas.class);
                            final String UidVacina = r.getUid();

                            tomadas.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot ds: snapshot.getChildren()) {
                                        Vacinas tomadas = ds.getValue(Vacinas.class);
                                        String Uid_Vacina_Tomada = tomadas.getUid();
                                        if(Uid_Vacina_Tomada.equals(UidVacina))
                                        {

                                        }
                                        else
                                        {
                                            list.clear();
                                            String NaoTomada = tomadas.getUid();// se for igual de alguma UBS lista  Ubs
                                            list.add(tomadas);
                                            adapter = new AdapterVacinas(RecycleViewVacina.this, list);
                                            recview.setAdapter(adapter);
                                        }
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(RecycleViewVacina.this, 2);
                        recview.setLayoutManager(gridLayoutManager);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(RecycleViewVacina.this, "Erro", Toast.LENGTH_SHORT).show();

                    }
                });

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

    private void RecuperarLista() {
        list.clear();
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshotl: dataSnapshot.getChildren()) {
                    Vacinas r = dataSnapshotl.getValue(Vacinas.class);
                    list.add(r);
                }
                adapter = new AdapterVacinas(RecycleViewVacina.this, list);
                recview.setAdapter(adapter);

             //   LinearLayoutManager layoutManager = new LinearLayoutManager(RecycleViewVacina.this, LinearLayoutManager.HORIZONTAL, false);
              //  recview.setLayoutManager(layoutManager);]

                GridLayoutManager gridLayoutManager = new GridLayoutManager(RecycleViewVacina.this, 2);
                recview.setLayoutManager(gridLayoutManager);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(RecycleViewVacina.this, "Erro", Toast.LENGTH_SHORT).show();

            }
        });
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