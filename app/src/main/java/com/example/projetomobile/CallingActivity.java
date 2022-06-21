package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class CallingActivity extends AppCompatActivity {

    private TextView Nome;
    private ImageView Perfil;
    private ImageView BtnAtender;
    private ImageView BtnCancelar;

    private String IdRecebendoLigacao = "";
    private String NomeRecebendoLigacao = "";
    private String ImagemRecebendoLigacao = "";

    private String callingId = "";
    private String ringningId = "";

    private String FazendoLigacao_ID = "";
    private String FazendoLigacaO_NOME = "";
    private String FazendoLigacao_IMAGEM = "";

    private String Checker = "";
    private MediaPlayer mediaPlayer;
    private DatabaseReference MedicoRef;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);

        mAuth = FirebaseAuth.getInstance();

        FazendoLigacao_ID = mAuth.getCurrentUser().getUid();


        IdRecebendoLigacao = getIntent().getExtras().get("UID_LIGACAO").toString();
        MedicoRef = FirebaseDatabase.getInstance().getReference().child("Usuário");


        mediaPlayer = MediaPlayer.create(this, R.raw.toque);
        Nome = findViewById(R.id.nome_calling);
        Perfil = findViewById(R.id.profile_image_calling);
        BtnAtender = findViewById(R.id.make_call);
        BtnCancelar = findViewById(R.id.cancel_call);

        BtnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Checker = "clicked";

                mediaPlayer.stop();


            }
        });

       BtnAtender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final HashMap<String, Object> callingPickUp= new HashMap<>();
                callingPickUp.put("atendido", "atendido");

                MedicoRef.child(FazendoLigacao_ID).child("Tocando")
                        .updateChildren(callingPickUp)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isComplete())
                                {
                                  Intent VideoChamada = new Intent(CallingActivity.this, VideoChamadaActivity.class);
                                    startActivity(VideoChamada);
                                }

                            }
                        });


            }
        });



        getAndSetUserProfile();

    }


    private void getAndSetUserProfile() {

        MedicoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(IdRecebendoLigacao).exists())
                {
                    ImagemRecebendoLigacao = dataSnapshot.child(IdRecebendoLigacao).child("imagem").getValue().toString();
                    NomeRecebendoLigacao = dataSnapshot.child(IdRecebendoLigacao).child("nome").getValue().toString();

                    Nome.setText(NomeRecebendoLigacao);
                    Picasso.get().load(ImagemRecebendoLigacao).placeholder(R.drawable.profile_image).into(Perfil);

                }

                if(dataSnapshot.child(FazendoLigacao_ID).exists())
                {
                    FazendoLigacao_IMAGEM = dataSnapshot.child(FazendoLigacao_ID).child("imagem").getValue().toString();
                    FazendoLigacaO_NOME = dataSnapshot.child(FazendoLigacao_ID).child("nome").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        MedicoRef.child(IdRecebendoLigacao)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!Checker.equals("clicked") && !dataSnapshot.hasChild("Ligando") && !dataSnapshot.hasChild("Tocando"))
                        {
                            final HashMap<String, Object> LigandoInfo = new HashMap<>();

                            LigandoInfo.put("nome", NomeRecebendoLigacao);
                            LigandoInfo.put("ligando", IdRecebendoLigacao);
                            LigandoInfo.put("tipo", "user");

                            MedicoRef.child(FazendoLigacao_ID).child("Ligando")
                                    .updateChildren(LigandoInfo)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful())
                                            {
                                                final HashMap<String, Object> TocandoInfo = new HashMap<>();

                                                TocandoInfo.put("nome", FazendoLigacaO_NOME);
                                                TocandoInfo.put("tocando", FazendoLigacao_ID);
                                                TocandoInfo.put("tipo", "doctor");

                                                MedicoRef.child(IdRecebendoLigacao).child("Tocando")
                                                        .updateChildren(TocandoInfo);
                                            }
                                        }
                                    });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        MedicoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(FazendoLigacao_ID).hasChild("Tocando") && !dataSnapshot.child(FazendoLigacao_ID).hasChild("Ligando"))
                {
                    BtnAtender.setVisibility(View.VISIBLE);
                }

                if(dataSnapshot.child(IdRecebendoLigacao).child("Tocando").hasChild("atendido")){
                    Intent intent = new Intent(CallingActivity.this, VideoChamadaActivity.class);
                 startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void cancelarCallingUser() {
        //Fazendo a Ligação
        MedicoRef.child(FazendoLigacao_ID)
                .child("Ligando")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists() && dataSnapshot.hasChild("ligando"))
                        {
                            callingId = dataSnapshot.child("ligando").getValue().toString();

                            MedicoRef.child(callingId)
                                    .child("Tocando")
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful())
                                            {
                                                MedicoRef.child(FazendoLigacao_ID)
                                                        .child("Ligando")
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                startActivity(new Intent(CallingActivity.this, RecycleView_Medicos.class));
                                                                finish();

                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }else
                        {
                            startActivity(new Intent(CallingActivity.this, MedicoActivy.class));
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //Recebendo a Ligação

        MedicoRef.child(FazendoLigacao_ID)
                .child("Tocando")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists() && dataSnapshot.hasChild("tocando"))
                        {
                            ringningId = dataSnapshot.child("tocando").getValue().toString();

                            MedicoRef.child(ringningId)
                                    .child("Ligando")
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful())
                                            {
                                                MedicoRef.child(FazendoLigacao_ID)
                                                        .child("Tocando")
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                startActivity(new Intent(CallingActivity.this, MedicoActivy.class));
                                                                finish();

                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }else
                        {
                            startActivity(new Intent(CallingActivity.this, MainActivity.class));
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

}