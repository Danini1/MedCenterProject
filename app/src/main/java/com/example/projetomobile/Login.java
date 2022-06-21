package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import model.Usuario;


public class Login extends AppCompatActivity {

    private static final String TAG = Login.class.getSimpleName();

    private EditText Email;
    private EditText Senha;
    private String NomeUser = "";
    private String TipoUser = "";
    private Button Logar;
    private Button Forgot;
    private  DatabaseReference dr;
    private ProgressDialog mLoadingBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(Login.this);

        Email = findViewById(R.id.EMAIL);
        Senha = findViewById(R.id.SENHA);

        Logar = findViewById(R.id.LOGAR);
        Forgot = findViewById(R.id.FORGOT);

        Logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = Email.getText().toString().trim();
                String senha = Senha.getText().toString().trim();
                User();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    Email.setError("Email Inválido");
                    Email.setFocusable(true);

                } else {
                    UserLogin( email, senha);


                }

                FloatingActionButton fb = (FloatingActionButton) findViewById(R.id.login);
                fb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), TelaInicio.class));

                    }
                });

            }

        });


        Forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RecuperarSenha();
            }
        });

    }

    public void voltartelainicio(View view) {

        Intent intent = new Intent(this, TelaInicio.class);
        startActivity(intent);
    }


    private void User() {
        String email = Email.getText().toString();
        String senha = Senha.getText().toString();

        if (email == null || email.isEmpty() || email == null || email.isEmpty() || senha == null || senha.isEmpty()) {
            Toast.makeText(this, "Todos os dados devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void UserLogin( String email, String senha) {

        mLoadingBar.setTitle("Login");
        mLoadingBar.setMessage("Por favor aguarde enquanto verificamos sua credencial");
        mLoadingBar.setCanceledOnTouchOutside(false);
        mLoadingBar.show();

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            if(TipoUser  == "") {

                                dr = FirebaseDatabase.getInstance().getReference().child("Usuário").child(mAuth.getCurrentUser().getUid());
                                VerificarUsuario(dr);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mLoadingBar.dismiss();
                Toast.makeText(Login.this, ""  + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void VerificarUsuario(DatabaseReference dr) {

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Usuário").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TipoUser = dataSnapshot.child("tipo").getValue().toString();
                NomeUser = dataSnapshot.child("nome").getValue().toString();
                if (TipoUser.equals("user")) {
                    mLoadingBar.dismiss();
                    Intent Main = new Intent(Login.this, MainActivity.class);
                    Main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(Main);
                    finish();
                    Toast.makeText(Login.this, "Bem vindo " + NomeUser, Toast.LENGTH_SHORT).show();

                } else if (TipoUser.equals("doctor")) {
                    mLoadingBar.dismiss();
                    Intent doctor = new Intent(Login.this, MedicoActivy.class);
                    doctor.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(doctor);
                    finish();
                    Toast.makeText(Login.this, "Bem vindo " + NomeUser, Toast.LENGTH_SHORT).show();

                } else if (TipoUser.equals("admin")) {
                    mLoadingBar.dismiss();
                    Intent Adm = new Intent(Login.this, AdminActivity.class);
                    Adm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(Adm);
                    finish();
                    Toast.makeText(Login.this, "Bem vindo " + NomeUser, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void RecuperarSenha() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Redefinir Senha");

        LinearLayout constraintLayout = new LinearLayout(this);

        final EditText emailEd = new EditText(this);
        emailEd.setHint("Email");
        emailEd.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        emailEd.setMinEms(10);

        constraintLayout.addView(emailEd);
        constraintLayout.setPadding(10,10,10,10);

        builder.setView(constraintLayout);


        builder.setPositiveButton("Recuperar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = emailEd.getText().toString().trim();
                beginRecuperar(email);

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void beginRecuperar(String email) {

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {

                    Toast.makeText(Login.this, "O link de Redefinição de Senha foi enviado para o seu Email", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Login.this, "Não foi possível enviar o link para o seu Email", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}