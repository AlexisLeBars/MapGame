package com.mapgame.alexislebars.mapgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class ScoreDataSource {
    private SQLiteDatabase database;
    private DataBaseHelper dbHelper;
    private String[] allColumns = { DataBaseHelper.COLUMN_id,
            DataBaseHelper.COLUMN_level, DataBaseHelper.COLUMN_score,
            DataBaseHelper.COLUMN_date, DataBaseHelper.COLUMN_mode};

    public ScoreDataSource(Context context) {
        dbHelper = new DataBaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Scores createScore(String level,String score, String mode) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.COLUMN_level, level);
        values.put(DataBaseHelper.COLUMN_score, score);
        values.put(DataBaseHelper.COLUMN_mode, mode);

        long insertId = database.insert(DataBaseHelper.TABLE_scores, null,
                values);
        Cursor cursor = database.query(DataBaseHelper.TABLE_scores,
                allColumns, DataBaseHelper.COLUMN_id + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Scores score_ = cursorToScore(cursor);
        cursor.close();
        return score_;
    }

    public void deleteScore(Scores s) {
        long id = s.getId();
        System.out.println("Scores deleted with id: " + id);
        database.delete(DataBaseHelper.TABLE_scores, DataBaseHelper.COLUMN_id
                + " = " + id, null);
    }

    public List<Scores> getAllScore(String order) {
        List <Scores> s = new ArrayList<>();
        String sort = " DESC";
        Cursor cursor;
        if(order.equals("level") || order.equals("mode")){
            sort = "";
        }
        if(order.equals("score")){
            cursor = database.query(DataBaseHelper.TABLE_scores,
                    allColumns, null, null, null, null, "cast("+order+" as unsigned)"+sort+","+DataBaseHelper.COLUMN_mode+" DESC");

        }
        else
            cursor = database.query(DataBaseHelper.TABLE_scores,
                allColumns, null, null, null, null, order+ sort+","+DataBaseHelper.COLUMN_mode+" DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Scores cur_s = cursorToScore(cursor);
            s.add(cur_s);
            cursor.moveToNext();
        }
        // assurez-vous de la fermeture du curseur
        cursor.close();
        return s;
    }

    private Scores cursorToScore(Cursor cursor) {
        Scores score = new Scores();
        score.setId(cursor.getLong(0));
        score.setLevel(cursor.getString(1));
        score.setScore(cursor.getString(2));
        score.setDate(cursor.getString(3));
        score.setMode(cursor.getString(4));
        return score;
    }
}
