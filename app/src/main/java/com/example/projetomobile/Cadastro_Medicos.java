package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Medicos;

import static android.widget.Toast.LENGTH_SHORT;

public class Cadastro_Medicos extends AppCompatActivity {

    private EditText Nome;
    private EditText Especialidade;
    private EditText Local;
    private EditText Senha;
    private EditText Email;
    private CircleImageView profileImg;
    private Button addFoto;
    private Button Cadastrar;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private StorageReference storageProfilePicsRef;
    private Uri imageUri;
    private String myUri = "";
    private StorageTask uploadTask;
    private Medicos medicos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro__medicos);

        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Foto de Perfil");
        mAuth = FirebaseAuth.getInstance();


        medicos = new Medicos();

        Nome = findViewById(R.id.NOME);
        Especialidade = findViewById(R.id.Especialidadee);
        Email = findViewById(R.id.EMAIL);
        Senha = findViewById(R.id.SENHA);
        Local = findViewById(R.id.Hospital);
        profileImg = findViewById(R.id.FotoPerfil);
        addFoto = findViewById(R.id.addFot);
        Cadastrar = findViewById(R.id.CADASTRAR);

        Cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = Nome.getText().toString().trim();
                String especialidade = Especialidade.getText().toString().trim();
                String local = Local.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String senha = Senha.getText().toString().trim();


                UploadProfileImage();
                CreateUser (nome, email, senha, especialidade, local,myUri);

            }
        });

        FloatingActionButton fb = (FloatingActionButton) findViewById(R.id.faddcadastro);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminActivity.class));

            }
        });

        addFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1,1).start(Cadastro_Medicos.this);

            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileImg.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Não foi possivel adicionar a imagem", LENGTH_SHORT).show();
        }
    }

    private void UploadProfileImage() {

        if(imageUri != null)
        {
            final StorageReference fileRef;

            fileRef = storageProfilePicsRef
                    .child(mAuth.getCurrentUser() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task <Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri dowloadUrl = task.getResult();
                        myUri = dowloadUrl.toString();

                        database = FirebaseDatabase.getInstance();
                        myRef = database.getReference("Usuário");
                        myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imagem").setValue(myUri);

                    }
                }
            });
        }
        else
        {
            Toast.makeText(Cadastro_Medicos.this, "Nenhuma Imagem foi selecionada", LENGTH_SHORT).show();
        }
    }
    private void CreateUser(final String nome, final String email, final String senha, final String especialidade, final String local, final String myUri)
    {

        if(nome == null || nome.isEmpty() || especialidade == null || especialidade.isEmpty() || email == null || email.isEmpty() || senha == null || senha.isEmpty() || local == null || local.isEmpty() )
        {
            Toast.makeText(this, "Todos os dados devem ser preenchidos", LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser user = mAuth.getCurrentUser();

                    String email = user.getEmail();
                    String uid = user.getUid();
                    Log.i("Imagem", myUri);

                    CadastrarUsuario(uid, nome, email, senha, especialidade, local, myUri);

                }
            }
        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.i("Teste", e.getMessage());

                    }
                });

    }

    private void CadastrarUsuario (String uid, String nome, String email, String senha,  String especialidade, String local, String myUri)
    {

        medicos.setNome(nome);
        medicos.setTipo("doctor");
        medicos.setUid(uid);
        medicos.setImagem(myUri);
        medicos.setEmail(email);
        medicos.setSenha(senha);
        medicos.setLocal(local);
        medicos.setEspecialidade(especialidade);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Usuário");
        myRef.child(uid).setValue(medicos);

        Toast.makeText(Cadastro_Medicos.this,"Cadastrando "+nome,Toast.LENGTH_SHORT).show();

    }

}