package com.example.pavlo.wwords.local_db_manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pavlo on 20.07.16.
 */
public class LocalDatabaseHelper extends SQLiteOpenHelper{

    public LocalDatabaseHelper(Context context) {
        super(context, DatabaseConfig.DB_NAME, null, DatabaseConfig.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DatabaseConfig.DICTIONARY_TABLE + " ("
                + DatabaseConfig.WORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabaseConfig.ENGLISH + " TEXT, "
                + DatabaseConfig.UKRAINIAN + " TEXT" + ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
