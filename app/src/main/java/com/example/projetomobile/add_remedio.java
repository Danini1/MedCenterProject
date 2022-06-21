package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import static com.example.projetomobile.R.*;
import com.example.projetomobile.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class add_remedio extends AppCompatActivity {

    EditText nome;
    EditText descricao;
    EditText indicacao;
    EditText foto;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(layout.activity_add_remedio);

        nome = (EditText)findViewById(id.addnome);
        descricao = (EditText)findViewById(id.addescricao);
        indicacao = (EditText)findViewById(id.addqtd);
        foto = (EditText)findViewById(id.addimage);
        add = (Button)findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                processinsert();

            }
        });
        FloatingActionButton fb = (FloatingActionButton) findViewById(R.id.faddaddremedio);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminActivity.class));

            }
        });
    }

    private void processinsert() {
        Map<String,Object> map = new HashMap<>();
        map.put("Nome", nome.getText().toString());
        map.put("Descricao", descricao.getText().toString());
        map.put("Indicacao", indicacao.getText().toString());
        map.put("Imagem", foto.getText().toString());
        FirebaseDatabase.getInstance().getReference().child("Vacinas").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        nome.setText("");
                        descricao.setText("");
                        indicacao.setText("");
                        foto.setText("");
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
}