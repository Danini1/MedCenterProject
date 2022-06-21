package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import model.Medicos;

import static java.util.Calendar.HOUR;

public class MarcarConsulta extends AppCompatActivity {

    EditText Data;
    EditText Hora;
    Button btnDia;
    Button btnHora;
    Button btnConsulta;
    EditText Nome;
    EditText Especialidade;
    EditText Medico;
    EditText Local;
    EditText Cpf;
    String email;
    String UID;
    String local;
    String medico;
    String especialidade;
    DatabaseReference dr;
    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    Context context;
    Dialog dialog;
    String dateString;
    String ImgPaciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcar_consulta);

        Intent uid = getIntent();
        UID = uid.getStringExtra("UID");

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        Intent intent1 = getIntent();
        medico = intent1.getStringExtra("medico");

        Intent intent2 = getIntent();
        especialidade = intent2.getStringExtra("especialidade");

        Intent intent3 = getIntent();
        local = intent3.getStringExtra("local");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dr = database.getReference("Usuário");


        context = this;
        Data = findViewById(R.id.DATA);
        Hora = findViewById(R.id.HORA);
        Nome = findViewById(R.id.NOME);
        Cpf = findViewById(R.id.CPF);
        Especialidade = findViewById(R.id.ESPECIALIDADE);
        Medico = findViewById(R.id.MEDICO);
        Local = findViewById(R.id.LOCAL);
        btnDia = findViewById(R.id.BTNdata);
        btnHora = findViewById(R.id.BTNhora);
        btnConsulta = findViewById(R.id.btnMarcarConsulta);

        RecuperarNome();

        Medico.setText(medico);
        Especialidade.setText(especialidade);
        Local.setText(local);

        DesabilitarHeditText(Medico);
        DesabilitarHeditText(Especialidade);
        DesabilitarHeditText(Local);

        btnConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validação();

            }
        });

        btnDia.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                ButtonData();
            }
        });

        btnHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonHora();
            }
        });

        FloatingActionButton fb = (FloatingActionButton) findViewById(R.id.marcar);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RecycleView_Medicos.class));

            }
        });

    }

    private void RecuperarNome() {

        dr.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String NomeUser = dataSnapshot.child("nome").getValue().toString();
                ImgPaciente = dataSnapshot.child("imagem").getValue().toString();
                Nome.setText(NomeUser);
                DesabilitarHeditText(Nome);




            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void DesabilitarHeditText(EditText editText) {
        editText.setEnabled(false);
    }


    private void Validação() {
        String d = Data.getText().toString();
        String h = Hora.getText().toString();
        String n = Nome.getText().toString();
        String e = Especialidade.getText().toString();
        String m = Medico.getText().toString();
        String l = Local.getText().toString();

        if(n .isEmpty())
        {
            Nome.setError("Insira seu nome");
            Nome.requestFocus();

        }
        else if(e.isEmpty())
        {
            Especialidade.setError("Insira a especialidade do médico");
            Especialidade.requestFocus();
        }

        else if(m.isEmpty())
        {
            Medico.setError("Insira o nome do médico");
            Medico.requestFocus();
        }

        else if(l.isEmpty())
        {
            Local.setError("Insira o local da consulta");
            Local.requestFocus();

        }
        else if(d.isEmpty())
        {
            Data.setError("Coloque a data");
            Data.requestFocus();
        }
        else if(h.isEmpty())
        {
            Hora.setError("Coloque a hora");
            Hora.requestFocus();
        }
        else{
            processinsert();
        }

    }

    private void ButtonHora() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);

        boolean formato24horas = DateFormat.is24HourFormat(context);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                String time = + hour +":" + minute;
                Hora.setText(time);


            }
        }, HOUR, MINUTE, formato24horas);
        timePickerDialog.show();

    }

    private void ButtonData() {

        Calendar calendar = Calendar.getInstance();

        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);


        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                dateString = date + "/" + month + "/" + year;
                Data.setText(dateString);

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);

                CharSequence dateCharSequence = DateFormat.format("EEEE, dd MMM yyyy", calendar1);
                Data.setText(dateCharSequence);


            }
        },YEAR,MONTH,DATE);
        datePickerDialog.show();
    }

    private void processinsert() {

        Map<String,Object> map = new HashMap<>();

        map.put("Nome",Nome.getText().toString());
        map.put("FotoPaciente",ImgPaciente);
        map.put("Uid", user.getUid());
        map.put("Medico", Medico.getText().toString());
        map.put("Especialidade", Especialidade.getText().toString());
        map.put("Data", Data.getText().toString());
        map.put("Hora", Hora.getText().toString());
        map.put("Local", Local.getText().toString());

        if(Especialidade.getText().toString().equals("Odontologia"))
        {
            map.put("Foto", "https://images.educamaisbrasil.com.br/content/noticias/conheca-as-principais-especializacoes-de-odontologia_g.jpg");
        }
        else  if(Especialidade.getText().toString().equals("Pediatra"))
        {
            map.put("Foto", "https://saudebusiness.com/wp-content/uploads/2017/05/pediatra.jpg");
        }

        FirebaseDatabase.getInstance().getReference().child("Consultas Marcadas").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Nome.setText("");
                        Medico.setText("");
                        Especialidade.setText("");
                        Data.setText("");
                        Hora.setText("");
                        Local.setText("");

                    }
                });

        Confirmacao();
    }

    private void Confirmacao() {

        dialog = new Dialog(MarcarConsulta.this);
        dialog.setContentView(R.layout.dialog_confirmado);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP )
        {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        Button ok;
        ok = dialog.findViewById(R.id.BtnOk);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(MarcarConsulta.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        dialog.show();
    }

}