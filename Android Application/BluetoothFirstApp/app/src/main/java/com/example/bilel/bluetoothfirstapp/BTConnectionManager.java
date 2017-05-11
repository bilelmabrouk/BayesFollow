package com.example.bilel.bluetoothfirstapp;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Bilel on 3/26/2017.
 */

public class BTConnectionManager extends Thread
{
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private final BluetoothSocket socket = null;
    private Handler myHandler;
    private Context ctx;

    public BTConnectionManager(Context pctx) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(Constants.TAG, "Temp sockets not created", e);
        }

        ctx = pctx;
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        Log.i(Constants.TAG, "Begin connectedThread");
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()

        StringBuilder readMessage = new StringBuilder();

        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {

                bytes = mmInStream.read(buffer);
                String read = new String(buffer, 0, bytes);
                readMessage.append(read);

                if (read.contains("\n")) {

                    myHandler.obtainMessage(Constants.MESSAGE_READ, bytes, -1, readMessage.toString()).sendToTarget();
                    readMessage.setLength(0);
                    Toast.makeText(ctx, "Message Received",Toast.LENGTH_SHORT).show();
                }

            } catch (IOException e) {

                Log.e(Constants.TAG, "Connection Lost", e);
                break;
            }
        }
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
            myHandler.obtainMessage(Constants.MESSAGE_WRITE, -1, -1, bytes).sendToTarget();
        } catch (IOException e) {
            Log.e(Constants.TAG, "Exception during write", e);
        }
    }

    /* Call this from the main activity to shutdown the connection */
    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(Constants.TAG, "close() of connect socket failed", e);}
    }
}
