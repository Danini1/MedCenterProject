package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Usuario;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;


public class Cadastro extends AppCompatActivity {

    private Button Cadastrar;
    private Button Voltar;
    private Button addFoto;
    private EditText Nome;
    private EditText Senha;
    private EditText Email;
    private EditText Cpf;
    private EditText Dtns;
    private CircleImageView profileImg;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private StorageReference storageProfilePicsRef;
    private Uri imageUri;
    private String myUri = "";
    private StorageTask uploadTask;
    private static final int Gallery_Intent = 1;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);


        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Foto de Perfil");
        mAuth = FirebaseAuth.getInstance();
        usuario = new Usuario();

        Nome = findViewById(R.id.NOME);
        Email = findViewById(R.id.EMAIL);
        Senha = findViewById(R.id.SENHA);
        Cpf = findViewById(R.id.CPF);
        Dtns = findViewById(R.id.DTNS);
        Cadastrar = findViewById(R.id.CADASTRAR);
        Voltar= findViewById(R.id.VOLTAR);
        addFoto = findViewById(R.id.addFot);
        profileImg = findViewById(R.id.FotoPerfil);

        SimpleMaskFormatter smf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher mtw = new MaskTextWatcher(Cpf, smf);
        Cpf.addTextChangedListener(mtw);

        SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher maskTextWatcher = new MaskTextWatcher(Dtns, simpleMaskFormatter);
        Dtns.addTextChangedListener(maskTextWatcher);

        Cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = Nome.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String senha = Senha.getText().toString().trim();
                String cpf = Cpf.getText().toString().trim();
                String dtns = Dtns.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Email.setError("Email inválido");
                    Email.setFocusable(true);
                }
                else if (senha.length()<6)
                {
                    Senha.setError("A senha precisa ter no minimo 6 caracteres");
                    Senha.setFocusable(true);
                }
                else {
                    UploadProfileImage();
                    CreateUser (nome, email, senha, cpf, dtns, myUri);
                }





            }
        });

        FloatingActionButton fb = (FloatingActionButton) findViewById(R.id.faddcadastro);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TelaInicio.class));

            }
        });

        addFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1,1).start(Cadastro.this);

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
                        myRef.child(mAuth.getCurrentUser().getUid()).child("imagem").setValue(myUri)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        String link = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRYMbrvQyU5UD3uHsccpkfanFWLzPSl2wJg0g&usqp=CAU";

                                        Picasso.get().load(link).into(profileImg);
                                    }
                                });
                    }
                }
            });
        }
        else
        {
            Toast.makeText(Cadastro.this, "Nenhuma Imagem foi selecionada", LENGTH_SHORT).show();
        }
    }

    public void voltarteladeinicio (View view){

        Intent intent = new Intent(this, TelaInicio.class);
        startActivity(intent);
    }



    private void CreateUser(final String nome, final String email, final String senha, final String cpf, final String dtns, final String myUri)
    {

        if(nome == null || nome.isEmpty() || email == null || email.isEmpty() || senha == null || senha.isEmpty() || cpf == null || senha.isEmpty() || dtns == null || dtns.isEmpty())
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

                    CadastrarUsuario(uid, nome, email, senha, cpf, dtns);

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

    private void CadastrarUsuario (String uid, String nome, String email, String senha, String cpf, String dtnsc)
    {

        usuario.setNome(nome);
        usuario.setTipo("user");
        usuario.setUid(uid);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setCpf(cpf);
        usuario.setDtsnc(dtnsc);

        /*Map<String,Object> CadastrarUser = new HashMap<>();

        CadastrarUser.put("nome", nome);
        CadastrarUser.put("tipo", "user");
        CadastrarUser.put("uid", uid);
        CadastrarUser.put("email", email);
        CadastrarUser.put("senha", senha);
        CadastrarUser.put("cpf", cpf);
        CadastrarUser.put("dtnsc", dtnsc);

         */


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Usuário");
        myRef.child(uid).setValue(usuario)
               .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Nome.setText("");
                        Email.setText("");
                        Senha.setText("");
                        Cpf.setText("");
                        Dtns.setText("");
                    }
                });


        Toast.makeText(Cadastro.this,"Cadastrando "+nome,Toast.LENGTH_SHORT).show();

    }


}