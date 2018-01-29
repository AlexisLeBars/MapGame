package com.mapgame.alexislebars.mapgame;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by aurelien on 29/01/18.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_scores = "scores";
    public static final String COLUMN_id = "id";
    public static final String COLUMN_level = "nom";
    public static final String COLUMN_score = "score";
    public static final String COLUMN_date = "date";
    public static final String COLUMN_mode = "mode_de_jeu";

    private static final String DATABASE_name = "scores.db";
    private static final int DATABASE_VERSION = 1;

    // Commande sql pour la création de la base de données
    private static final String DATABASE_CREATE = "create table "
            + TABLE_scores + "(" + COLUMN_id
            + " integer primary key autoincrement,"
            + COLUMN_level +  " text not null,"
            + COLUMN_score+ " text not null,"
            + COLUMN_date + " datetime default current_timestamp,"
            + COLUMN_mode + " text not null"
            + ");";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_name, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DataBaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_scores);
        onCreate(db);
    }
}
