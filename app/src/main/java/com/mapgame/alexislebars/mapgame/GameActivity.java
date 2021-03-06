package com.mapgame.alexislebars.mapgame;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GameActivity extends AppCompatActivity implements OnMapReadyCallback, OnStreetViewPanoramaReadyCallback {

    private GoogleMap mMap;
    private StreetViewPanorama streetView = null;
    private DBSpot biblio;
    private LatLng posToFind;
    private Integer level = 0;
    private Double score = 0.0;
    private String mode = "";
    private int tour = 4, nbClick = 0;
    private boolean mapR = false,streetR=false;
    private LatLng lastMarker;
    private boolean savedState = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        biblio = new DBSpot();
        if(savedInstanceState != null){
            restoreState(savedInstanceState);
            savedState = true;
        }

        connectivityCheck();

        Intent i = getIntent();
        level = i.getIntExtra("level",1);
        mode = i.getStringExtra("mode");
        String message = "";
        switch (mode){
            case "Normal":
                message = "Try to get the lowest score possible by getting the closest to the point of interest !";
                break;
            case "Country":
                message = "Try to guess in what country is the point of interest !";
                break;
            case "Inverse":
                message = "Try to get the highest score by getting as far as possible from the point of interest !";
                break;
        }
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        setContentView(R.layout.activity_game);
        Log.d("pos",mode);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        StreetViewPanoramaFragment streetViewPanoramaFragment =
                (StreetViewPanoramaFragment) getFragmentManager()
                        .findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapR = true;
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        final LatLng pos = new LatLng(0, 0);
        if(streetR){
            setNextPos();
            googleMapClickListener();
        }
        if(lastMarker != null){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(lastMarker));
        }
        else {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
        }
    }
    //Déclenche la fin du jeu et mets le score en base
    public void endGame(){
        Log.d("pos","fin de la partie:"+score);

        ScoreDataSource db = new ScoreDataSource(this);
        db.open();
        String levelS = "";
        switch (level){
            case 1:
                levelS = "Easy";
                break;
            case 2:
                levelS = "Medium";
                break;
            case 3:
                levelS = "Hard";
        }
        biblio.reset();
        db.createScore(levelS,score.toString(),mode);
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Game Over !")
                .setMessage("Your Score : "+Math.round(score))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(GameActivity.this, ScoreActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
    //Charge un nouveau lieux à chercher
    public void setNextPos(){
        nbClick = 0;
        connectivityCheck();
        if( tour > 0){
            if(savedState){
                savedState = false;
                posToFind = biblio.getCurPos();
                Log.d("pos","streetview pos restored "+posToFind.latitude+ " / "+ posToFind.longitude);
            }
            else {
                posToFind = biblio.getNewSpot(level);
                tour--;
            }
            if(streetView != null){
                streetView.setPosition(posToFind);
            }

        }else{
            endGame();
        }

        setTitle("Location "+(4-tour)+"/4");

    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        streetR = true;

        if(level == 3 || mode.equals("Country")){
            streetViewPanorama.setStreetNamesEnabled(false);
            streetViewPanorama.setUserNavigationEnabled(false);
        }

        if(mapR){

            setNextPos();
            googleMapClickListener();
        }

        streetView = streetViewPanorama;
        streetViewPanorama.setPosition(posToFind);
        Log.d("pos","streetView OK" + posToFind.latitude+ " / "+ posToFind.longitude);
    }
    private void googleMapClickListener(){
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){

            @Override
            public void onMapClick(LatLng point){
                connectivityCheck();
                mMap.getUiSettings().setAllGesturesEnabled(false);
                nbClick++;
                if(nbClick == 1) {
                    Geocoder gcd;

                    double dist = 0;
                    //message a envoyer a l'utilisateur
                    String message;
                    lastMarker = new LatLng(point.latitude, point.longitude);
                    if (mode.equals("Country")) {
                        gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                        List<Address> addrToFind = null;
                        List<Address> addrUser = null;
                        try {
                            addrToFind = gcd.getFromLocation(posToFind.latitude, posToFind.longitude, 1);
                            addrUser = gcd.getFromLocation(point.latitude, point.longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (addrToFind != null && addrToFind.get(0).getCountryName().equals(addrUser.get(0).getCountryName())) {
                            dist = 1;
                            message = "Good Country ! + 1";
                        } else
                            message = "Bad Country ...";
                    } else {
                        dist = SphericalUtil.computeDistanceBetween(point, posToFind) / 1000;
                        message = "" + Math.round(dist) + " Km away from the answer";
                    }
                    score += dist;
                    //Ajout des marqueurs recherché et testé
                    mMap.addMarker(new MarkerOptions()
                            .position(point)
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                            .draggable(false)
                            .visible(true)
                    );
                    mMap.addMarker(new MarkerOptions()
                            .position(posToFind)
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            .draggable(false)
                            .visible(true)
                    );

                    addLineBetween(point, posToFind);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                    biblio.curIdxs.add(biblio.cur);
                    moveCamera();

                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mMap.clear();
                            setNextPos();
                            if(tour > 0)
                                mMap.getUiSettings().setAllGesturesEnabled(true);

                        }
                    }, 5500);
                }
            }
        });
    }
    //Ajoute une ligne entre les marqueurs recherché et testé
    private void addLineBetween(LatLng a, LatLng b){
        PolylineOptions l = (new PolylineOptions())
                .add(a)
                .add(b)
                .color(Color.BLACK)
                ;
        mMap.addPolyline(l);
    }
    //Effectue le déplacement de la caméra vers le point recherché avec un zoom-dézoom
    private void moveCamera(){

        final GoogleMap.CancelableCallback b = new GoogleMap.CancelableCallback(){
            @Override
            public void onFinish() {
                mMap.animateCamera(CameraUpdateFactory.zoomTo(6),800,null);
            }

            @Override
            public void onCancel() {

            }
        };
        final GoogleMap.CancelableCallback a = new GoogleMap.CancelableCallback(){
            @Override
            public void onFinish() {
                CameraPosition.Builder cp = new CameraPosition.Builder().target(posToFind);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp.build()),1200,b);
            }

            @Override
            public void onCancel() {

            }
        };
        mMap.animateCamera(CameraUpdateFactory.zoomTo(1), 800,a);
    }

    //Sauvegarde l'état du tour du jeu, l'index, le marqueur du lieu courant et l'index des lieux visités
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList("index",biblio.curIdxs);
        outState.putInt("curIdx", biblio.cur);
        outState.putInt("tour",tour);
        Log.d("idx","cont "+biblio.curIdxs.toString());
        if(lastMarker != null) {
            outState.putBoolean("lastM",true);
            outState.putDouble("lat", lastMarker.latitude);
            outState.putDouble("lon", lastMarker.longitude);
        }
        else{
            outState.putBoolean("lastM",false);
        }
    }
    //Restaure l'état du jeu
    public void restoreState(Bundle state){
        if(state != null){
            biblio.curIdxs = state.getIntegerArrayList("index");
            biblio.setViewedSpots();
            if(state.getBoolean("lastM")) {
                lastMarker = new LatLng(state.getDouble("lat"), state.getDouble("lon"));
            }
            biblio.cur = state.getInt("curIdx");
            tour = state.getInt("tour");
        }
    }
    //Teste l'état de la connection internet
    public void connectivityCheck(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        if(!connected){
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Internet Access")
                    .setMessage("Connectivity Issue !")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
}

