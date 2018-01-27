package com.mapgame.alexislebars.mapgame;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GameActivity extends AppCompatActivity implements OnMapReadyCallback, OnStreetViewPanoramaReadyCallback {

    private GoogleMap mMap;
    private StreetViewPanorama streetView = null;
    private DBSpot biblio = new DBSpot();
    private LatLng posToFind;
    private Integer level = 0;
    private Double score = 0.0;
    private String mode = "";
    private int tour = 4;
    private boolean mapR = false,streetR=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        level = i.getIntExtra("level",1);
        mode = i.getStringExtra("mode");
        setContentView(R.layout.activity_game);
        Log.d("pos",mode);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //FloatingActionButton fab = findViewById(R.id.fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

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
        // Add a marker in Sydney, Australia, and move the camera.
        final LatLng sydney = new LatLng(-34, 151);
        if(streetR){
            setNextPos();
            googleMapClickListener();
        }


       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


    }

    public void endGame(){
        Log.d("pos","fin de la partie:"+score);
    }

    public void setNextPos(){

        if( tour > 0){
            posToFind = biblio.getNewSpot(level);
            if(streetView != null){
                streetView.setPosition(posToFind);
                Log.d("pos","streeview pos change "+posToFind.latitude+ " / "+ posToFind.longitude);
            }

            Log.d("pos",posToFind.latitude+ " / "+ posToFind.longitude);
        }else{
            endGame();
        }

        tour--;
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        streetR = true;
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
                Geocoder gcd = null;

                double dist = 0;
                String message = null;//message a envoyer a l'utilisateur

                if(mode.equals("Pays")){
                    //if GameMode is Pays
                    gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> addrToFind = null;
                    List<Address> addrUser = null;
                    try {
                        addrToFind = gcd.getFromLocation(posToFind.latitude,posToFind.longitude,1);
                        addrUser = gcd.getFromLocation(point.latitude,point.longitude,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(addrToFind.get(0).getCountryName().equals(addrUser.get(0).getCountryName())){
                        dist = 1;
                    }
                }else{
                    //for other GameMode
                    dist = SphericalUtil.computeDistanceBetween(point,posToFind)/1000;
                }
                    score += dist;
                //add marker at the user point
                mMap.addMarker(new MarkerOptions()
                        .position(point)
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                        .draggable(false)
                        .visible(true)
                );

                Polyline pl = addLineBetween(point,posToFind);
                //send message to the user about the distance miss
                Toast.makeText(getApplicationContext(),""+dist+ " away for the answer",Toast.LENGTH_LONG).show();
                //clear the map after 7 sec
                //while this time we have to unzoom and zoom on the right place

                mMap.getUiSettings().setAllGesturesEnabled(false);

                moveCamera(mMap);


                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mMap.clear();
                        setNextPos();
                        mMap.getUiSettings().setAllGesturesEnabled(true);

                    }
                },7000);
            }
        });
    }

    private Polyline addLineBetween(LatLng a, LatLng b){
        PolylineOptions l = (new PolylineOptions())
                .add(a)
                .add(b)
                .color(Color.BLACK)
                //.geodesic(true)
                ;
        return mMap.addPolyline(l);
    }
    private void moveCamera(GoogleMap m){

        final GoogleMap.CancelableCallback b = new GoogleMap.CancelableCallback(){
            @Override
            public void onFinish() {
                mMap.animateCamera(CameraUpdateFactory.zoomTo(6),1000,null);
            }

            @Override
            public void onCancel() {

            }
        };
        final GoogleMap.CancelableCallback a = new GoogleMap.CancelableCallback(){
            @Override
            public void onFinish() {
                CameraPosition.Builder cp = new CameraPosition.Builder().target(posToFind);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp.build()),2000,b);
            }

            @Override
            public void onCancel() {

            }
        };

        mMap.animateCamera(CameraUpdateFactory.zoomTo(1), 1000,a);
        //zoom in

        //CameraPosition.Builder cp = new CameraPosition.Builder().target(posToFind);
        //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp.build()),2000,null);

       // mMap.animateCamera(CameraUpdateFactory.zoomTo(8),1000,null);
    }
}

