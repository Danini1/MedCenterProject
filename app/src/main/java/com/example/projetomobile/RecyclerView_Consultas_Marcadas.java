package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import adapters.AdapterConsultasMarcadas;
import model.ConsultasMarcadas;

public class RecyclerView_Consultas_Marcadas extends AppCompatActivity {

    RecyclerView recview;
    DatabaseReference dr;
    TextView date;
    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ArrayList<ConsultasMarcadas> list;
    AdapterConsultasMarcadas adapter;
    String today;
    String dataConsulta;
    Date todayDate;
    Date ConsultaDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view__consultas__marcadas);
        setTitle("Consultas Marcadas");

        date = (TextView) findViewById(R.id.datee);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

         today = DateFormat.getDateInstance(SimpleDateFormat.DATE_FIELD).format(calendar.getTime());
        try {
            todayDate = dateFormat.parse("09/11/2020");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //date.setText(today);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        recview = (RecyclerView)findViewById(R.id.reciview);
        recview.setLayoutManager(new LinearLayoutManager(this));

        list= new ArrayList<ConsultasMarcadas>();



        dr = FirebaseDatabase.getInstance().getReference().child("Consultas Marcadas");
        Query query = dr.orderByChild("Uid").equalTo(user.getUid());


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    dataConsulta = dataSnapshot.child("Data").getValue().toString();
                    try {
                        ConsultaDate = dateFormat.parse("17/11/2020");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    ConsultasMarcadas c = dataSnapshot.getValue(ConsultasMarcadas.class);
                    list.add(c);

                    //date.setText(dataConsulta);


                }

                adapter = new AdapterConsultasMarcadas(RecyclerView_Consultas_Marcadas.this, list);
                recview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(RecyclerView_Consultas_Marcadas.this, "Erro", Toast.LENGTH_SHORT).show();
            }
        });

        //if ( todayDate.compareTo(ConsultaDate) < 1 )

        FloatingActionButton fb = (FloatingActionButton) findViewById(R.id.marcadas);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_remedio,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s.toString());
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


}