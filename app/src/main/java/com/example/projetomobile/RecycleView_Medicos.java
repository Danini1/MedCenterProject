package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import adapters.AdapterMedicos;
import model.Medicos;
public class RecycleView_Medicos extends AppCompatActivity {

    private RecyclerView myMedicoList;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private String userNome = "";
    private String userEspecialidade  = "";
    private String userLocal = "";
    private String userImagem = "";
    private Button Ligacao;
    private String profileImage;
    private DatabaseReference MedicoRef;
    private DatabaseReference MedicoReference;
    private String calledBy = "";
    private String NomeUser = "";

    RecyclerView mycontactList;
    RecyclerView recview;
    DatabaseReference dr;
    ArrayList<Medicos> list;
    AdapterMedicos adapter;
    FloatingActionButton fb;
    String email;
    String UID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view__medicos);
        setTitle("Médicos");

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        MedicoReference = FirebaseDatabase.getInstance().getReference().child("Usuário");
        userRef = FirebaseDatabase.getInstance().getReference().child("Usuário");



        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        Intent uid = getIntent();
        UID = uid.getStringExtra("UID");

        recview = (RecyclerView) findViewById(R.id.reciview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<Medicos>();


        MedicoRef = FirebaseDatabase.getInstance().getReference().child("Usuário");


        dr = FirebaseDatabase.getInstance().getReference().child("Usuário");
        Query query = dr.orderByChild("tipo").equalTo("doctor");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshotl: snapshot.getChildren()) {

                    Medicos r = dataSnapshotl.getValue(Medicos.class);
                    list.add(r);
                }
                adapter = new AdapterMedicos(RecycleView_Medicos.this, list, email);
                recview.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RecycleView_Medicos.this, "Erro", Toast.LENGTH_SHORT).show();

            }

        });
        fb = (FloatingActionButton)findViewById(R.id.fadd);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(getApplicationContext(), MarcarConsulta.class);
                menu.putExtra("email", email);
                startActivity(menu);

            }
        });
        fb = (FloatingActionButton)findViewById(R.id.VOLTAR);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

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

    @Override
    protected void onStart() {
        super.onStart();

        RecebendoLigacao();
    }

    private void RecebendoLigacao()
    {

        userRef.child(mAuth.getCurrentUser().getUid())
                .child("Tocando")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild("tocando"))
                        {
                            calledBy = dataSnapshot.child("tocando").getValue().toString();

                            Intent callingIntent = new Intent(RecycleView_Medicos.this, CallingActivity.class);
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
    private  void LayoutAnimation(RecyclerView recyclerView)
    {
        Context context = recyclerView.getContext();
        LayoutAnimationController layoutAnimationController =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_slide_right);

        recyclerView.setLayoutAnimation(layoutAnimationController);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();

    }
}