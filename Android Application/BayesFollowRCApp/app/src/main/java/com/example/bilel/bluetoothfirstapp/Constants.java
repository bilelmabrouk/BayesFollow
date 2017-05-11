package com.example.bilel.bluetoothfirstapp;

import java.util.UUID;

/**
 * Created by Bilel on 3/26/2017.
 */

public interface Constants
{
    String TAG = "Arduino - Android";
    int REQUEST_ENABLE_BT = 1;

    // message types sent from the BluetoothChatService Handler
    int MESSAGE_STATE_CHANGE = 1;
    int MESSAGE_READ = 2;
    int MESSAGE_WRITE = 3;
    int MESSAGE_SNACKBAR = 4;

    // Constants that indicate the current connection state
    int STATE_NONE = 0;       // we're doing nothing
    int STATE_ERROR = 1;
    int STATE_CONNECTING = 2; // now initiating an outgoing connection
    int STATE_CONNECTED = 3;  // now connected to a remote device


    UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    // Key names received from the BluetoothChatService Handler
    String EXTRA_DEVICE  = "EXTRA_DEVICE";
    String SNACKBAR = "toast";

    //DATABASE (AS GIVEN BY OM)
    public static final int BASE_VERSION = 3;
    public static final String BASE_NOM = "states.db";
    public static final String TABLE_STATES = "table_state";
    public static final String COLONNE_ID = "id";
    public static final String COLONNE_SENSORS="sensors";
    public static final String COLONNE_ACTION = "action";

    public static final java.lang.String REQUETE_CREATION_TABLE =  "create table " + TABLE_STATES + " ("
            + COLONNE_ID + " integer primary key autoincrement, " + COLONNE_SENSORS + " integer not null, " +
            COLONNE_ACTION + " integer not null );" ;

}
