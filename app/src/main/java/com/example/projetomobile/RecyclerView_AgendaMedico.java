package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import adapters.AdapterAgendaMedicos;
import adapters.AdapterMedicos;
import model.AgendaMedicos;
import model.Medicos;
import model.Usuario;

public class RecyclerView_AgendaMedico extends AppCompatActivity {

    private RecyclerView recview;
    private ArrayList<AgendaMedicos> list;
    private AdapterAgendaMedicos adapter;
    private DatabaseReference dr;
    private DatabaseReference MedicoReference;
    private FirebaseAuth mAuth;
    private String uid;
    private String Nome;
    private String calledBy = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view__agenda_medico);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        recview = (RecyclerView) findViewById(R.id.reciview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<AgendaMedicos>();

        dr = FirebaseDatabase.getInstance().getReference().child("Usuário");
        Query queryUid = dr.orderByChild("uid").equalTo(uid);

        RecuperarNome(queryUid);
    }

    @Override
    protected void onStart() {
        super.onStart();

        RecebendoLigacao();
    }

    private void RecebendoLigacao()
    {
        dr.child(mAuth.getCurrentUser().getUid())
                .child("Tocando")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild("tocando"))
                        {
                            calledBy = dataSnapshot.child("tocando").getValue().toString();

                            Intent callingIntent = new Intent(RecyclerView_AgendaMedico.this, CallingActivity.class);
                            callingIntent.putExtra("UID_LIGACAO", calledBy);
                            startActivity(callingIntent);
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    public void RecuperarNome(Query query)
    {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    Usuario user = snapshot.getValue(Usuario.class);
                     Nome = user.getNome();

                    MedicoReference = FirebaseDatabase.getInstance().getReference().child("Consultas Marcadas");
                    Query queryNome = MedicoReference.orderByChild("Medico").equalTo(Nome);

                    RecuperarDados(queryNome);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RecyclerView_AgendaMedico.this, "Não foi possivel recuperar os dados", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void RecuperarDados(Query query) {

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshotl: snapshot.getChildren()) {

                    AgendaMedicos agenda = dataSnapshotl.getValue(AgendaMedicos.class);
                    list.add(agenda);
                }
                adapter = new AdapterAgendaMedicos(RecyclerView_AgendaMedico.this, list);
                recview.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RecyclerView_AgendaMedico.this, "Erro", Toast.LENGTH_SHORT).show();

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