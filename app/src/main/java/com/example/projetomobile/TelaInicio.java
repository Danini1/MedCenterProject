package com.example.projetomobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TelaInicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicio);
    }

    public void ircadastro (View view){

        Intent intent = new Intent(this, Cadastro.class);
        startActivity(intent);
    }
    public void irlogin (View view){

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}