package com.example.bilel.bluetoothfirstapp;

/**
 * Created by Bilel on 4/1/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static com.example.bilel.bluetoothfirstapp.Constants.*;

public class StateDAO
{
    private SQLiteDatabase maBaseDonnees;
    private OpenHelper baseHelper;
    public StateDAO(Context ctx) {
        baseHelper = new OpenHelper(ctx, BASE_NOM, null, BASE_VERSION);
    }

    public SQLiteDatabase open() {
        maBaseDonnees = baseHelper.getWritableDatabase();
        return maBaseDonnees;
    }

    public void close() {
        maBaseDonnees.close();
    }

    public ArrayList<State> getAllStates() {
        Cursor c = maBaseDonnees.query(TABLE_STATES, new String[]{COLONNE_ID, COLONNE_SENSORS, COLONNE_ACTION}, null, null, null, null, null, null);
        return cursorToStates(c);
    }

    private ArrayList<State> cursorToStates(Cursor c)
    {
        if (c.getCount() == 0) {
            c.close();
            return new ArrayList<>();
        }
        ArrayList<State> retStates = new ArrayList<State>();
        c.moveToFirst();
        do {
            State state = new State();
            state.setSensors(c.getInt(c.getColumnIndex(COLONNE_SENSORS)));
            state.setAction(c.getInt(c.getColumnIndex(COLONNE_ACTION)));
            retStates.add(state);
        } while (c.moveToNext());
        c.close();
        return retStates;

    }

    public void insertAllStates(ArrayList<State> states)
    {
        for(State state: states)
        {
            insertState(state);
        }
    }

    public long insertState(State state)
    {
        ContentValues valeurs = new ContentValues();
        valeurs.put(COLONNE_SENSORS, state.getSensors());
        valeurs.put(COLONNE_ACTION, state.getAction());
        return maBaseDonnees.insert(TABLE_STATES, null, valeurs);
    }
    public void emptyDataBase() {
        maBaseDonnees.execSQL("drop table " + TABLE_STATES + ";");
        maBaseDonnees.execSQL(REQUETE_CREATION_TABLE);
    }
}


