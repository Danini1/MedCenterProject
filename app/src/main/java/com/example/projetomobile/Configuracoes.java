package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Configuracoes extends AppCompatActivity {

    private TextView email;
    private BottomNavigationView bottomNavigationView;
    private Button BtnSair;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.Configuracoess);
        mAuth = FirebaseAuth.getInstance();

        BtnSair = findViewById(R.id.sair);

        BtnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Saindo();
            }
        });

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case  R.id.Menuu:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        overridePendingTransition(0,0);
                        break;


                    case R.id.Perfill:
                        startActivity(new Intent(getApplicationContext(), Perfil.class));
                        finish();
                        overridePendingTransition(0,0);
                        break;

                    case R.id.Configuracoess:
                }
            }
        });
    }

    private void Saindo()
    {
        mAuth.signOut();
        finish();
        Intent intent = new Intent(Configuracoes.this,Login.class);
        startActivity(intent);
    }
}