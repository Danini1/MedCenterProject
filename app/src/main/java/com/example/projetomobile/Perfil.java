package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Usuario;

public class Perfil extends AppCompatActivity {

    private static final String TAG ="Perfil" ;
    private BottomNavigationView bottomNavigationView;

    private TextView Email;
    private TextView Nome;
    private TextView Senha;
    private TextView Dtnsc;
    private TextView Cpf;
    private TextView Uid;
    private CircleImageView profileImg;
    private DatabaseReference databaseReference;

    private FloatingActionButton fb;
    private StorageReference storageProfilePicsRef;
    private Uri imageUri;
    private AlertDialog myDialog;
    private AlertDialog myDialogSenha;
    private String myUri = "";
    private StorageTask uploadTask;
    private FirebaseUser user;
    private FirebaseUser useremail;
    private Query query;
    private DatabaseReference dr;
    private FirebaseAuth mAuth;
    private String Nome_User;
    private String Senha_User;
    private String Cpf_User;
    private String Nascimento_User;
    private String Email_User;
    private String Link_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.Perfill);


        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Foto de Perfil");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        useremail = mAuth.getCurrentUser();



        Nome = findViewById(R.id.NomeUser);
        Cpf = findViewById(R.id.cpf);
        Dtnsc = findViewById(R.id.dtnsc);
        Email = findViewById(R.id.email);
        profileImg = findViewById(R.id.FotoUser);
        fb = findViewById(R.id.editarrr);

        /*
        SimpleMaskFormatter smf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher mtw = new MaskTextWatcher(Cpf, smf);
        Cpf.addTextChangedListener(mtw);

        SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher maskTextWatcher = new MaskTextWatcher(Dtnsc, simpleMaskFormatter);
        Dtnsc.addTextChangedListener(maskTextWatcher);

         */

        /*
        DesabilitarHeditText(Nome);
        DesabilitarHeditText(Cpf);
        DesabilitarHeditText(Dtnsc);
        DesabilitarHeditText(Email);
        DesabilitarHeditText(Senha);

         */


        dr =  FirebaseDatabase.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Usuário");
        query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        RecuperarDados(query);


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


                    case R.id.Configuracoess:
                        startActivity(new Intent(getApplicationContext(), Configuracoes.class));
                        finish();
                        overridePendingTransition(0,0);
                        break;
                }
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              ConfirmarSenha();

            }
        });

    }
    private void ConfirmarSenha() {

        View view = LayoutInflater.from(Perfil.this).inflate(R.layout.dialog_confirmar_senha, null);

        final EditText SenhaConfirmar = view.findViewById(R.id.ConfirmarSenha);
        Button BtnConfimar = view.findViewById(R.id.CONFIRMAR_SENHA);

        final AlertDialog.Builder builder = new AlertDialog.Builder(Perfil.this);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();

        BtnConfimar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Senha = SenhaConfirmar.getText().toString();

                if(TextUtils.isEmpty(Senha))
                {
                    Toast.makeText(Perfil.this, "Coloque sua Senha", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(Senha.equals(Senha_User))
                {
                    EditarPerfil();
                }
                else
                {
                    Toast.makeText(Perfil.this,"Senha Incorreta", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

    }



    private void UpdateSenha(final String NovaSenha) {

        if(NovaSenha == null || NovaSenha.isEmpty() )
        {
            Toast.makeText(getApplicationContext(),"Sua Senha é a mesma: "+Senha_User, Toast.LENGTH_SHORT).show();
        }
        else
        {
            user.updatePassword(NovaSenha).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    dr.child("Usuário").child(user.getUid()).child("senha").setValue(NovaSenha);
                    Senha_User = NovaSenha;
                    Toast.makeText(getApplicationContext(), "Senha Atualizada", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getApplicationContext(), "Não foi possivel atualizar sua Senha: "+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }


    }

    private void EditarPerfil() {

        AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);

        final CharSequence[] Opcoes = {"Editar Foto de Perfil",
                "Editar Dados"};


        myBuilder.setItems(Opcoes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {

                Toast.makeText(getApplicationContext(), Opcoes[position].toString(), Toast.LENGTH_SHORT).show();


                if( position == 0)
                {
                    CropImage.activity().setAspectRatio(1,1).start(Perfil.this);




                }
                else if( position == 1)
                {
                    UpdateDados();


                }
            }
        });

        myDialog = myBuilder.create();
        myDialog.show();

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileImg.setImageURI(imageUri);
            UploadProfileImage();
        }
        else
        {
            Toast.makeText(this, "Não foi possivel adicionar a imagem", Toast.LENGTH_SHORT).show();
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

                        dr.child("Usuário").child(user.getUid()).child("imagem").setValue(myUri);
                        Link_user = myUri;
                        Toast.makeText(Perfil.this, "Imagem Atualizada\n", Toast.LENGTH_SHORT).show();


                    }
                }
            });
        }
        else
        {
            Toast.makeText(Perfil.this, "Nenhuma Imagem foi selecionada", Toast.LENGTH_SHORT).show();
        }
    }


    private void RecuperarDados(Query query) {

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    Usuario user = snapshot.getValue(Usuario.class);
                    Link_user = user.getImagem();
                    Nome_User = user.getNome();
                    Cpf_User = user.getCpf();
                    Nascimento_User = user.getDtsnc();
                    Email_User = user.getEmail();
                    Senha_User = user.getSenha();

                    if (!Link_user.isEmpty()) {
                        Picasso.get().load(Link_user).into(profileImg);

                    }
                    Nome.setText(Nome_User);
                    Email.setText(Email_User);
                    Cpf.setText(Cpf_User);
                    Dtnsc.setText(Nascimento_User);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Perfil.this, "Não foi possivel recuperar os dados", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void UpdateDados ()
    {

        View view = LayoutInflater.from(Perfil.this).inflate(R.layout.dialog_update, null);

        final EditText Nome = view.findViewById(R.id.NomeEditar);
        final EditText Senha = view.findViewById(R.id.SenhaEditar);
        final EditText Cpf =  view.findViewById(R.id.CpfEditar);
        final EditText Dtnsc = view.findViewById(R.id.DtnscEditar);

        SimpleMaskFormatter smf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher mtw = new MaskTextWatcher(Cpf, smf);
        Cpf.addTextChangedListener(mtw);

        SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher maskTextWatcher = new MaskTextWatcher(Dtnsc, simpleMaskFormatter);
        Dtnsc.addTextChangedListener(maskTextWatcher);



        final AlertDialog.Builder Atualizar = new AlertDialog.Builder(Perfil.this);
        Atualizar.setView(view);



        Atualizar.setPositiveButton("Atualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String nome = Nome.getText().toString().trim();
                final String email = Email.getText().toString().trim();
                final String senha = Senha.getText().toString().trim();
                final String cpf = Cpf.getText().toString().trim();
                final String dtns = Dtnsc.getText().toString().trim();

                
                    UpdateSenha(senha);


                dr.child("Usuário").child(user.getUid()).child("nome").setValue(nome)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Nome_User = Nome.getText().toString();


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(Perfil.this, "Não foi possivel atualizar seu nome\n", Toast.LENGTH_SHORT).show();
                                }
                            });

                    dr.child("Usuário").child(user.getUid()).child("cpf").setValue(cpf)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Cpf_User = Cpf.getText().toString();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(Perfil.this, "Erro ao atualizar seu Cpf\n", Toast.LENGTH_SHORT).show();
                                }
                            });

                    dr.child("Usuário").child(user.getUid()).child("dtsnc").setValue(dtns)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Nascimento_User = Dtnsc.getText().toString();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(Perfil.this, "Não foi possivel atualizar sua Data de Nascimento\n", Toast.LENGTH_SHORT).show();
                                }
                            });

                    Toast.makeText(Perfil.this, " Atualização Realizada com Sucesso !\n", Toast.LENGTH_SHORT).show();



            }
        });

        Atualizar.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        Atualizar.create().show();

    }

}
