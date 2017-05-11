package com.example.bilel.bluetoothfirstapp;

/**
 * Created by Bilel on 4/1/2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.bilel.bluetoothfirstapp.Constants.*;


public class OpenHelper extends SQLiteOpenHelper {

    public OpenHelper(Context context, String nom, SQLiteDatabase.CursorFactory cursorfactory, int version)
    {
        super(context, nom, cursorfactory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(REQUETE_CREATION_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Dans notre cas, nous supprimons la table et ses donnÃ©es pour
        // en crÃ©er une nouvelle ensuite.
        db.execSQL("drop table " + TABLE_STATES + ";");
        // CrÃ©ation de la nouvelle structure.
        onCreate(db);
    }
}

