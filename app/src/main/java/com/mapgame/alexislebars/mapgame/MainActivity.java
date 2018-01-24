package com.mapgame.alexislebars.mapgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button buttonF = findViewById(R.id.btFacile);
        buttonF.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("passage",
                        "mode facile");
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("Level",1);
                startActivity(intent);
            }
        });
    }
}
