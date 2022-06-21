package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Usuario;


public class MainActivity extends AppCompatActivity {
    public String email;
    private TextView Nome;
    public String UID;
    private FusedLocationProviderClient fusedLocationClient;
    private BottomNavigationView bottomNavigationView;
    private CircleImageView profileImg;
    private DatabaseReference databaseReference;
    private DatabaseReference d1;
    DatabaseReference dr;
    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    private TextView e;
    private TextView f;
    private int MY_PERMISSION_REQUEST_READ_CONTACTS;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dr = database.getReference();
        user = mAuth.getCurrentUser();


        profileImg = findViewById(R.id.FotoPerfil);
        Nome = findViewById(R.id.aa);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        Intent uid = getIntent();
        UID = uid.getStringExtra("UID");
        f = findViewById(R.id.f);



        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.Menuu);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Usuário");
        Query query = databaseReference.orderByChild("uid").equalTo(user.getUid());

        RecuperarDados(query);

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case  R.id.Menuu:

                    case R.id.Perfill:
                        startActivity(new Intent(getApplicationContext(), Perfil.class));
                        finish();
                        overridePendingTransition(0,0);
                        break;

                    case R.id.Configuracoess:
                        startActivity(new Intent(getApplicationContext(), Configuracoes.class));
                        finish();
                        overridePendingTransition(0,0);
                        return;
                }
            }
        });
    }


   public void RecuperarDados(Query query)
    {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    Usuario user = snapshot.getValue(Usuario.class);
                    String link = user.getImagem();
                    String nome = user.getNome();

                    if (!link.isEmpty()) {
                        Picasso.get().load(link).into(profileImg);
                    }

                    Nome.setText(nome);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Não foi possivel recuperar os dados", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void LatLongFirebase() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String [] {Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_REQUEST_READ_CONTACTS);
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.e("Latitude:", + location.getLatitude() + "Longitude" + location.getLongitude());

                            Map<String,Object> latlang = new HashMap<>();

                            latlang.put("Latitude", location.getLatitude());
                            latlang.put("Longitude", location.getLongitude());

                            dr.child("Ubs").push().setValue(latlang);

                        }
                    }
                });
    }

    public void Mapa (View view){
        Intent menu = new Intent(this, MapaActivity.class);
        startActivity(menu);
    }

    public void Médicos (View view){

        Intent menu = new Intent(this, RecycleView_Medicos.class);
        menu.putExtra("email", email);
        menu.putExtra("UID", UID);
        startActivity(menu);
    }


    public void proximaTela(View view){

        Intent intent = new Intent(this, RecycleView_Remedios.class);
        startActivity(intent);
    }


    public void ConsultasFinalizadas(View view){

        Intent menu = new Intent(this, recycle_view_consultas_antigas.class);
        menu.putExtra("email", email);
        startActivity(menu);
    }

    public void Vacina(View view){

        Intent vacina = new Intent(this, RecycleViewVacina.class);
        startActivity(vacina);
    }

    public void ConsultasMarcadas(View view){

        Intent menu = new Intent(this, RecyclerView_Consultas_Marcadas.class);
        menu.putExtra("email", email);
        menu.putExtra("UID", UID);
        startActivity(menu);
    }


}
