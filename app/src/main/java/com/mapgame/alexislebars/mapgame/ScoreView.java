package com.mapgame.alexislebars.mapgame;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class ScoreView extends ListActivity {
        private ScoreDataSource db;
        private String order;
        private ArrayAdapter<Scores> adapter;
        @Override
        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);

            db = new ScoreDataSource(this);
            db.open();
            order = DataBaseHelper.COLUMN_score;
            List<Scores> values = db.getAllScore(order);
            setContentView(R.layout.activity_score_view);

            adapter = new ScoresAdapter(this,values);
            setListAdapter(adapter);

            final ListView liste = findViewById(android.R.id.list);
            liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View view, int arg2,long itemID) {
                    String a = ((ScoresAdapter)arg0.getAdapter()).getScore();
                    String score = "Here is my score at Guess My Place : "+a;

                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_TEXT, score);

                    startActivity(Intent.createChooser(share, "Share your Score !"));

                }
            });

        }
        public void onClick(View v){
            ArrayAdapter<Scores> adapter = (ArrayAdapter<Scores>) getListAdapter();

            adapter.clear();

            switch (v.getId()){
                case R.id.level:
                    adapter.addAll(db.getAllScore(DataBaseHelper.COLUMN_level));
                    break;
                case R.id.score:
                    adapter.addAll(db.getAllScore(DataBaseHelper.COLUMN_score));
                    break;
                case R.id.date:
                    adapter.addAll(db.getAllScore(DataBaseHelper.COLUMN_date));
                    break;
                case R.id.mode:
                    adapter.addAll(db.getAllScore(DataBaseHelper.COLUMN_mode));
                    break;

            }
            adapter.notifyDataSetChanged();
        }

    @Override
    protected void onResume(){
        db.open();
        super.onResume();
    }

    @Override
    protected void onPause(){
        db.close();
        super.onPause();
    }

}
