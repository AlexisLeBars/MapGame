package com.mapgame.alexislebars.mapgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button buttonF = findViewById(R.id.btFacile);
        final Button buttonM = findViewById(R.id.btMoyen);
        final Button buttonD = findViewById(R.id.btDifficile);
        final Button buttonS = findViewById(R.id.btStat);
        final Spinner sp = findViewById(R.id.gameChoice);
        //sp.setOnItemClickListener(new );
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

                Intent intent = new Intent(MainActivity.this, ScoreView.class);
                startActivity(intent);
            }


        });
    }
}
