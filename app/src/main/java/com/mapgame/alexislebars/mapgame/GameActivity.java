package com.mapgame.alexislebars.mapgame;

import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

public class GameActivity extends AppCompatActivity implements OnMapReadyCallback, OnStreetViewPanoramaReadyCallback {

    private GoogleMap mMap;
    private StreetViewPanorama streetView = null;
    private DBSpot biblio = new DBSpot();
    private LatLng posToFind;
    private Integer level = 0;
    private Double score = 0.0;
    private int tour = 4;
    private boolean mapR = false,streetR=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        level = i.getIntExtra("Level",1);
        Log.d("pos","difficulter : "+level);
        setContentView(R.layout.activity_game);
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
        if(mapR && streetR )
            setNextPos();
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){

            @Override
            public void onMapClick(LatLng point){

                double dist = SphericalUtil.computeDistanceBetween(point,posToFind)/1000;
                score += dist;

                mMap.addMarker(new MarkerOptions()
                        .position(point)
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                        .draggable(false)
                        .visible(true)
                );

                PolylineOptions l = (new PolylineOptions())
                        .add(point)
                        .add(posToFind)
                        .color(Color.BLACK)
                        .geodesic(true);
                Polyline pl =  mMap.addPolyline(l);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pl.remove();
                setNextPos();
            }
        });
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
        if( mapR & streetR)
            setNextPos();
        streetView = streetViewPanorama;
        streetViewPanorama.setPosition(posToFind);
        Log.d("pos","streetView OK" + posToFind.latitude+ " / "+ posToFind.longitude);
    }
}
