package com.example.projetomobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class recycler_view_Ubs extends AppCompatActivity {
    TextView markerText;
    String nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view__ubs);

        Intent intent = new Intent();
        nome = getIntent().getStringExtra("nome");
        markerText.setText(nome);


        markerText = findViewById(R.id.marker);
    }
}