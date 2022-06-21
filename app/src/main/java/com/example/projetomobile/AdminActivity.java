package com.example.projetomobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {

    private Button Medicos;
    private Button UBS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Medicos = findViewById(R.id.med);
        UBS = findViewById(R.id.Ubs);


        Medicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cadastro = new Intent(getApplicationContext(), Cadastro_Medicos.class);
                startActivity(cadastro);
                finish();
            }
        });

        UBS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cadastro = new Intent(getApplicationContext(), addVacinaUBS.class);
                startActivity(cadastro);
                finish();
            }
        });
    }

    public void Vacina(View view){

        Intent vacina = new Intent(this, add_remedio.class);
        startActivity(vacina);
    }
}