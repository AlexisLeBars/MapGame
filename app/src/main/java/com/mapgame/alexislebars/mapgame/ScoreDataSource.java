package com.mapgame.alexislebars.mapgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by aurelien on 29/01/18.
 */

public class ScoreDataSource {
    private SQLiteDatabase database;
    private DataBaseHelper dbHelper;
    private String[] allColumns = { DataBaseHelper.COLUMN_id,
            DataBaseHelper.COLUMN_nom, DataBaseHelper.COLUMN_score,
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

    public Scores createScore(String nom,String score, String mode) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.COLUMN_nom, nom);
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

    public void deleteComment(Scores s) {
        long id = s.getId();
        System.out.println("Scores deleted with id: " + id);
        database.delete(DataBaseHelper.TABLE_scores, DataBaseHelper.COLUMN_id
                + " = " + id, null);
    }

    public List<Scores> getAllScore(String order) {
        List <Scores> s = new ArrayList<>();
        String sort = "DESC";
        if(order == null || order.equals("nom") || order.equals("mode")){
            sort = "";
        }
        if(order == null)
            order = "";
        Cursor cursor = database.query(DataBaseHelper.TABLE_scores,
                allColumns, null, null, null, null, order+ sort);

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
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy : hh:mm", Locale.FRANCE);

        score.setId(cursor.getLong(0));
        score.setNom(cursor.getString(1));
        score.setScore(cursor.getString(2));
        score.setDate(sdf.format(cursor.getString(3)));
        score.setMode(cursor.getString(4));
        return score;
    }
}
