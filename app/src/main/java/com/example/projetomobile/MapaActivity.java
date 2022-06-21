package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Mapa.MapAdapter;
import Mapa.MapModel;
import model.Medicos;

public class MapaActivity extends FragmentActivity implements OnMapReadyCallback {

    private SearchView mSearch_Text;
    private static final String TAG = "MapaActivity";
    private GoogleMap mMap;
    private ImageView LupAumentar;
    private ImageView LupaDiminuir;
    private float ZOOM = 14.5f;
    private DatabaseReference databaseReference;
    private int MY_PERMISSION_REQUEST_READ_CONTACTS;
    Query queryy;
    ArrayList<MapModel> list;
    DatabaseReference dr;
    ArrayList<Marker> TpmTimeMarker = new ArrayList<>();
    ArrayList<Marker> RealTimeMarkers = new ArrayList<>();
    Marker mMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        PermitirLocalizacao();


        list = new ArrayList<MapModel>();
        mSearch_Text = findViewById(R.id.input_search);
        LupAumentar = findViewById(R.id.lupAumentar);
        LupaDiminuir = findViewById(R.id.lupaDiminuir);


        dr = FirebaseDatabase.getInstance().getReference();


        mSearch_Text.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = mSearch_Text.getQuery().toString();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Ubs");
                queryy = databaseReference.orderByChild("Nome").equalTo(location.toUpperCase());

                DadosLocal(queryy, location);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        /*mSearch_Text.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = mSearch_Text.getQuery().toString();
                List<Address> listModel = null;
                if( location != null || !location.equals(""))
                {
                    Geocoder geocoder = new Geocoder(MapaActivity.this);

                    try {
                        listModel = geocoder.getFromLocationName(location, 1);

                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    Address address = listModel.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mapFragment.getMapAsync(this);
        */


    }

    private void DadosLocal(Query query, final String Lugar) {

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    MapModel mm = snapshot.getValue(MapModel.class);
                    Double Latitude = mm.getLatitude();
                    Double Longitude = mm.getLongitude();

                    LatLng latLng = new LatLng(Latitude, Longitude);
                    mMap.addMarker(new MarkerOptions().position(latLng).title(Lugar));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void countDownTimer() {
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.e("seconds remaining: ", "" + millisUntilFinished / 1000);

            }

            public void onFinish() {
                Toast.makeText(MapaActivity.this, "", Toast.LENGTH_SHORT).show();
                onMapReady(mMap);
            }
        }.start();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setPadding(5, 400, 5, 5);
        /*mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(),R.raw.style_map));*/
        LatLng Manaus = new LatLng(-3.119028, -60.021732);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Manaus, ZOOM));



        dr.child("Ubs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for( Marker marker: RealTimeMarkers)
                {
                    marker.remove();
                }
                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    MapModel r = snapshot.getValue(MapModel.class);
                    list.add(r);

                    MapModel mm = snapshot.getValue(MapModel.class);
                    Double Latitude = mm.getLatitude();
                    Double Longitude = mm.getLongitude();

                    mMap.setInfoWindowAdapter(new MapAdapter(MapaActivity.this, list ));


                    if(mm != null)
                    {
                        try {
                            String snippet = "Endereco: " + mm.getEndereco() + "\n" +
                                    "Dia: " + mm.getDia() + "\n" +
                                    "Horario: " + mm.getHorario() + "\n" +
                                    "Telefone: " + mm.getTelefone() + "\n";

                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(new LatLng(Latitude, Longitude))
                                    .title(mm.getNome())
                                    .snippet(snippet);
                            mMarker = mMap.addMarker(markerOptions);
                            TpmTimeMarker.add(mMap.addMarker(markerOptions));

                        }catch (NullPointerException e){
                            Log.e(TAG, "NullPointerException: " + e.getMessage());
                        }
                    }else
                    {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(Latitude, Longitude)));
                    }
                }
                RealTimeMarkers.clear();
                RealTimeMarkers.addAll(TpmTimeMarker);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        LupAumentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMap.animateCamera(CameraUpdateFactory.zoomBy(1));

                /*
                Log.d(TAG, "onClick: clicked info place");
                try {
                    if(mMarker.isInfoWindowShown())
                    {
                        mMarker.hideInfoWindow();
                    }
                    else{
                        mMarker.showInfoWindow();
                    }

                }catch (NullPointerException e){
                    Log.e(TAG, "onClick: NullPointerException: " + e.getMessage());
                }

                 */
            }
        });

        LupaDiminuir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomBy(-1));
            }
        });
    }

    private void PermitirLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapaActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_REQUEST_READ_CONTACTS);
        }
    }
}