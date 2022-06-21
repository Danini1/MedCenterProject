package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MedicoActivy extends AppCompatActivity {

    private Button Agenda;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    private String calledBy = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico_activy);

        Agenda = findViewById(R.id.Agenda);

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Usuário");

        Agenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Agenda(v);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

       // RecebendoLigacao();
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

                            Intent callingIntent = new Intent(MedicoActivy.this, CallingActivity.class);
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

    public void Ligacao (View view){
        Intent menu = new Intent(this, RecycleView_Medicos.class);
        startActivity(menu);
    }

    public void Agenda (View view){
        Intent agenda = new Intent(this, RecyclerView_AgendaMedico.class);
        startActivity(agenda);
    }
}