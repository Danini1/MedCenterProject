package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class VideoChamadaActivity extends AppCompatActivity

        implements  Session.SessionListener,
        PublisherKit.PublisherListener
{


    private static String API_Key = "46985484";
    private static String SESSION_ID = "1_MX40Njk4NTQ4NH5-MTYwNTEzNjcyNzc0OX5IZitGL29wUUxPNUJoMFFHdmRvdW93UWV-fg";
    private static  String TOKEN = "T1==cGFydG5lcl9pZD00Njk4NTQ4NCZzaWc9MTYwYjc3M2NjOTkxNDBhNTMyYzVmZjViNDkyMzU3MzhmMDFlOTIyNzpzZXNzaW9uX2lkPTFfTVg0ME5qazROVFE0Tkg1LU1UWXdOVEV6TmpjeU56YzBPWDVJWml0R0wyOXdVVXhQTlVKb01GRkhkbVJ2ZFc5M1VXVi1mZyZjcmVhdGVfdGltZT0xNjA1MTM2ODY1Jm5vbmNlPTAuMjAxMjk2MDY4NDg3NjA1OTUmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTYwNzcyODg2NSZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";
    private static final String TAG = VideoChamadaActivity.class.getSimpleName();
    private static final int RC_VIDEO_APP_PERM = 124;

    private FrameLayout mPublisherViewController;
    private FrameLayout mSubscriberViewController;

    private Session mSession;
    private Publisher mPublisher;
    private Subscriber mSubscriber;


    private ImageView BtnFecharVideo;
    private DatabaseReference dr;
    private FirebaseAuth mAuth;
    private String UserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chamada);

        mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getCurrentUser().getUid();
        dr = FirebaseDatabase.getInstance().getReference().child("Usuário");

        BtnFecharVideo = findViewById(R.id.BtnEncerrarVideo);

        BtnFecharVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dr.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                       String  TipoUser = dataSnapshot.child(UserId).child("tipo").getValue().toString();


                        if(dataSnapshot.child(UserId).hasChild("Tocando")  && TipoUser.equals("doctor"))
                        {
                            //dr.child(UserId).child("Tocando").removeValue();

                            if(mPublisher != null)
                            {
                                mPublisher.destroy();
                            }

                            if(mSubscriber != null)
                            {
                                mSubscriber.destroy();
                            }

                            startActivity(new Intent(VideoChamadaActivity.this, MedicoActivy.class));
                            finish();
                        }
                        if(dataSnapshot.child(UserId).hasChild("Ligando")  && TipoUser.equals("user"))
                        {
                           // dr.child(UserId).child("Ligando").removeValue();

                            if(mPublisher != null)
                            {
                                mPublisher.destroy();
                            }

                            if(mSubscriber != null)
                            {
                                mSubscriber.destroy();
                            }

                            startActivity(new Intent(VideoChamadaActivity.this, RecycleView_Medicos.class));
                            finish();
                        }
                        else
                        {
                            Log.e(TAG, "dentro"+ TipoUser);

                            if(mPublisher != null)
                            {
                                mPublisher.destroy();
                            }

                            if(mSubscriber != null)
                            {
                                mSubscriber.destroy();
                            }

                            if(TipoUser.equals("doctor"))
                            {
                                startActivity(new Intent(VideoChamadaActivity.this, MedicoActivy.class));
                                finish();
                            }
                            else if(TipoUser.equals("user"))
                            {
                                startActivity(new Intent(VideoChamadaActivity.this, RecycleView_Medicos.class));
                                finish();
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        requestPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, VideoChamadaActivity.this);
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)

    private void requestPermissions() {
        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};

        if (EasyPermissions.hasPermissions(this, perms)) {
            mSubscriberViewController = findViewById(R.id.subscriber_container);
            mPublisherViewController = findViewById(R.id.publisher_container);

            //Conexão
            mSession = new Session.Builder(this, API_Key, SESSION_ID).build();
            mSession.setSessionListener(VideoChamadaActivity.this);
            mSession.connect(TOKEN);

        }else
            {
                EasyPermissions.requestPermissions(this, "Precisamos da permissão da sua Camera e Microfone, por favor permita", RC_VIDEO_APP_PERM, perms);
            }
    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }

    @Override
    public void onConnected(Session session) {
        Log.i(TAG, "Conectado");
        mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(VideoChamadaActivity.this);

        mPublisherViewController.addView(mPublisher.getView());

        if(mPublisher.getView() instanceof GLSurfaceView)
        {
            ((GLSurfaceView) mPublisher.getView()).setZOrderOnTop(true);
        }

        mSession.publish(mPublisher);

    }

    @Override
    public void onDisconnected(Session session) {

        Log.i(TAG, "Desconectado");
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(TAG, "Stream Recebida");

        if(mSubscriber == null)
        {
            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSession.subscribe(mSubscriber);
            mSubscriberViewController.addView(mSubscriber.getView());
        }

    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {

        Log.i(TAG, "Stream Excluido");

        if(mSubscriber != null)
        {
            mSubscriber = null;
            mSubscriberViewController.removeAllViews();
        }

    }

    @Override
    public void onError(Session session, OpentokError opentokError) {

        Log.i(TAG, "Stream Erro");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}