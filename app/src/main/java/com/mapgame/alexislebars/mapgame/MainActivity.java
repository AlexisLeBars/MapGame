package com.mapgame.alexislebars.mapgame;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final int MY_PERMISSIONS_REQUEST_INTERNET = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button buttonF = findViewById(R.id.btFacile);
        final Button buttonM = findViewById(R.id.btMoyen);
        final Button buttonD = findViewById(R.id.btDifficile);
        final Button buttonS = findViewById(R.id.btStat);
        final Spinner sp = findViewById(R.id.gameChoice);

        // Permission check
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_INTERNET);

            }
        }

        buttonF.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("level",1);
                intent.putExtra("mode",String.valueOf(sp.getSelectedItem()));
                startActivity(intent);
            }


        });
        buttonM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("level",2);
                intent.putExtra("mode",String.valueOf(sp.getSelectedItem()));
                startActivity(intent);
            }


        });
        buttonD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("level",3);
                intent.putExtra("mode",String.valueOf(sp.getSelectedItem()));
                startActivity(intent);
            }


        });
        buttonS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
                startActivity(intent);
            }


        });
        Toast.makeText(getApplicationContext(), "Select a Game Mode between Normal, Inverse, Country and click on one difficulty to play", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        final Button buttonF = findViewById(R.id.btFacile);
        final Button buttonM = findViewById(R.id.btMoyen);
        final Button buttonD = findViewById(R.id.btDifficile);
        final Spinner sp = findViewById(R.id.gameChoice);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_INTERNET: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    buttonF.setClickable(true);
                    buttonM.setClickable(true);
                    buttonD.setClickable(true);
                    sp.setClickable(true);

                } else {
                    buttonF.setClickable(false);
                    buttonM.setClickable(false);
                    buttonD.setClickable(false);
                    sp.setClickable(false);
                }
            }
        }
    }
}
