package com.example.pavlo.wwords.local_db_manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pavlo.wwords.models.WordsPair;

import java.util.ArrayList;

/**
 * Created by pavlo on 20.07.16.
 */
public class LocalDatabaseManager {

    private Context context;

    private LocalDatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public LocalDatabaseManager(Context context) {
        this.context = context;
        databaseHelper = new LocalDatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
    }

    public long addWord(String english, String ukrainian) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseConfig.ENGLISH, english);
        contentValues.put(DatabaseConfig.UKRAINIAN, ukrainian);
        long id = database.insert(DatabaseConfig.DICTIONARY_TABLE, null, contentValues);

        return id;
    }

    public String searchWord(String fromLanguage, String toLanguage, String word) {
        String translatedWord = "";
        Cursor cursor = null;
        int rowId = -1;

        cursor = database.rawQuery("SELECT " + toLanguage + " FROM "
                        + DatabaseConfig.DICTIONARY_TABLE
                        + " WHERE " + fromLanguage + " = '" + word + "'",
                null);

        if (cursor.moveToFirst() && cursor!= null) {
            rowId = cursor.getColumnIndex(toLanguage);
            do {
                translatedWord = cursor.getString(rowId);
            } while (cursor.moveToNext());
        }

        return translatedWord;
    }

    public ArrayList<WordsPair> getDictionary() {
        ArrayList<WordsPair> wordsPairs = new ArrayList<>();

        Cursor cursor = database.query(DatabaseConfig.DICTIONARY_TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst() && cursor != null) {
            int englishId = cursor.getColumnIndex(DatabaseConfig.ENGLISH);
            int ukrainianId = cursor.getColumnIndex(DatabaseConfig.UKRAINIAN);
            int idWordId = cursor.getColumnIndex(DatabaseConfig.WORD_ID);
            do {
                String englishWord = cursor.getString(englishId);
                String ukrainianWord = cursor.getString(ukrainianId);
                int id = cursor.getInt(idWordId);
                WordsPair wordsPair = new WordsPair(englishWord, ukrainianWord, id);
                wordsPairs.add(wordsPair);
            } while (cursor.moveToNext());
        }

        return wordsPairs;
    }

    public long delete(long wordId) {
        long id = database.delete(DatabaseConfig.DICTIONARY_TABLE, DatabaseConfig.WORD_ID + " = " + wordId, null);

        return id;
    }
}
