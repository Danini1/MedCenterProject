package com.example.projetomobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class add_consultas_antigas extends AppCompatActivity {

    EditText nome;
    EditText dia;
    EditText descricao;
    EditText doutor;
    EditText horario;
    EditText local;
    Button add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_consultas_antigas);


        dia = (EditText)findViewById(R.id.addia);
        descricao = (EditText)findViewById(R.id.addescricao);
        doutor = (EditText)findViewById(R.id.addoutor);
        horario = (EditText)findViewById(R.id.addhorario) ;
        local = (EditText)findViewById(R.id.addlocal);
        add = (Button)findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processinsert();
            }
        });
    }

    private void processinsert() {

        Map<String,Object> map = new HashMap<>();

        map.put("Latitude", dia.getText().toString());
        map.put("Longitude", descricao.getText().toString());
        map.put("Hospital", horario.getText().toString());


        FirebaseDatabase.getInstance().getReference().child("Localização").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dia.setText("");
                        descricao.setText("");
                        horario.setText("");

                    }
                });
    }
}