package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import adapters.AdapterVacinas;
import model.Vacinas;

public class addVacinaUBS extends AppCompatActivity {

    EditText nome;
    String NOME;
    EditText quantidade;
    Button add;
    DatabaseReference dr;
    FirebaseAuth mAuth;
    String UID;
    FloatingActionButton fb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vacina_ubs);

        dr = FirebaseDatabase.getInstance().getReference().child("Vacinas");


        nome = (EditText)findViewById(R.id.addnome);
        quantidade = (EditText)findViewById(R.id.quantidade);
        add = (Button)findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NOME = nome.getText().toString().trim();
                RecuperarLista(NOME);


            }
        });
    }

    private void processinsert(String UID) {
        Map<String,Object> map = new HashMap<>();
        map.put("Nome", nome.getText().toString());
        map.put("Quantidade", quantidade.getText().toString());
        map.put("Uid", UID);
        FirebaseDatabase.getInstance().getReference().child("Ubs").child("-MKVV7XPqwiVT8AkuXyq").child("Vacinas")
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        nome.setText("");
                        quantidade.setText("");
                        Toast.makeText(getApplicationContext() ,"Inserido com Sucesso", Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getApplicationContext() ,"Os dados não foram salvos", Toast.LENGTH_LONG).show();

                    }
                });

    }
    private void RecuperarLista(String Nome) {

        Query query = dr.orderByChild("Nome").equalTo(Nome);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshotl: dataSnapshot.getChildren()) {
                    Vacinas r = dataSnapshotl.getValue(Vacinas.class);
                    String UID = r.getUid();

                    processinsert(UID);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(addVacinaUBS.this, "Erro", Toast.LENGTH_SHORT).show();

            }
        });
    }

}