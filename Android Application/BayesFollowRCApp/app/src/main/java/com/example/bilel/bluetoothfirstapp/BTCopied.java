package com.example.bilel.bluetoothfirstapp;

/**
 * Created by Bilel on 3/30/2017.
 */

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import com.bayesserver.Network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by User on 12/21/2016.
 */

public class BTCopied {
    private static final String TAG = "BluetoothConnectionServ";

    private static final String appName = "MYAPP";

    private static final UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    public static final String CONTROL_MODE = "C";

    public static final String AUTONOMOUS_MODE = "A";

    private boolean firstTime = true;

    private boolean started = false;

    private boolean movEnabled = false;

    private String mode;

    private NetworkView netview;
    private SensorsControlView sensorView;
    private BayesTest netBayes;

    private final BluetoothAdapter mBluetoothAdapter;
    Context mContext;

    private AcceptThread mInsecureAcceptThread;
    private ConnectThread mConnectThread;
    private BluetoothDevice mmDevice;
    private UUID deviceUUID;
    private ProgressDialog mProgressDialog;

    private ConnectedThread mConnectedThread;
    private ArrayList<State> recordedStates;

    public BTCopied(Context context) {
        resetRecorderStates();
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        start();
    }

    public void resetRecorderStates()
    {
        recordedStates = new ArrayList<State>();
    }

    public ArrayList<State> getRecordedStates()
    {
        return recordedStates;
    }

    public void enableMotors(boolean isEnabled)
    {
        started = isEnabled;
        autoMove(netBayes.execInference());
    }

    public void enablemov(boolean movEnabled)
    {
        this.movEnabled = movEnabled;
    }

    public void stopThread()
    {
        byte[] binaryString = "H".getBytes(Charset.defaultCharset());
        write(binaryString);
        mConnectThread.cancel();
    }

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {

        // The local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread(){
            BluetoothServerSocket tmp = null;

            // Create a new listening server socket
            try{
                tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appName, MY_UUID_INSECURE);

                Log.d(TAG, "AcceptThread: Setting up Server using: " + MY_UUID_INSECURE);
            }catch (IOException e){
                Log.e(TAG, "AcceptThread: IOException: " + e.getMessage() );
            }

            mmServerSocket = tmp;
        }

        public void run(){
            Log.d(TAG, "run: AcceptThread Running.");

            BluetoothSocket socket = null;

            try{
                // This is a blocking call and will only return on a
                // successful connection or an exception
                Log.d(TAG, "run: RFCOM server socket start.....");

                socket = mmServerSocket.accept();

                Log.d(TAG, "run: RFCOM server socket accepted connection.");

            }catch (IOException e){
                Log.e(TAG, "AcceptThread: IOException: " + e.getMessage() );
            }

            //talk about this is in the 3rd
            if(socket != null){
                connected(socket,mmDevice);
            }

            Log.i(TAG, "END mAcceptThread ");
        }

        public void cancel() {
            Log.d(TAG, "cancel: Canceling AcceptThread.");
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: Close of AcceptThread ServerSocket failed. " + e.getMessage() );
            }
        }

    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid) {
            Log.d(TAG, "ConnectThread: started.");
            mmDevice = device;
            deviceUUID = uuid;
        }

        public void run(){
            BluetoothSocket tmp = null;
            Log.i(TAG, "RUN mConnectThread ");

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                Log.d(TAG, "ConnectThread: Trying to create InsecureRfcommSocket using UUID: "
                        +MY_UUID_INSECURE );
                tmp = mmDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                Log.e(TAG, "ConnectThread: Could not create InsecureRfcommSocket " + e.getMessage());
            }

            mmSocket = tmp;

            // Always cancel discovery because it will slow down a connection
            mBluetoothAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket

            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();

                Log.d(TAG, "run: ConnectThread connected.");
            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                    Log.d(TAG, "run: Closed Socket.");
                } catch (IOException e1) {
                    Log.e(TAG, "mConnectThread: run: Unable to close connection in socket " + e1.getMessage());
                }
                Log.d(TAG, "run: ConnectThread: Could not connect to UUID: " + MY_UUID_INSECURE );
            }

            //will talk about this in the 3rd video
            connected(mmSocket,mmDevice);
        }
        public void cancel() {
            try {
                Log.d(TAG, "cancel: Closing Client Socket.");
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: close() of mmSocket in Connectthread failed. " + e.getMessage());
            }
        }
    }



    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    public synchronized void start() {
        Log.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mInsecureAcceptThread == null) {
            mInsecureAcceptThread = new AcceptThread();
            mInsecureAcceptThread.start();
        }
    }

    /**
     AcceptThread starts and sits waiting for a connection.
     Then ConnectThread starts and attempts to make a connection with the other devices AcceptThread.
     **/

    public void startClientControl(BluetoothDevice device,UUID uuid, SensorsControlView sensorView)
    {
        Log.d(TAG, "startClient: Started Control mode.");

        mode = CONTROL_MODE;

        this.sensorView = sensorView;
        //initprogress dialog
        mProgressDialog = ProgressDialog.show(mContext,"Connecting Bluetooth"
                ,"Please Wait...",true);

        firstTime = true;

        if (mConnectThread == null)
            mConnectThread = new ConnectThread(device, uuid);
        else
        {
            mConnectThread.yield();
        }
        mConnectThread.start();

    }

    public void startClientAutonomous (BluetoothDevice device, UUID uuid, NetworkView netview, BayesTest net)
    {
        Log.d(TAG, "startClient: Started Autonomous mode.");

        mode = AUTONOMOUS_MODE;

        //initprogress dialog
        mProgressDialog = ProgressDialog.show(mContext,"Connecting Bluetooth"
                ,"Please Wait...",true);

        firstTime = true;

        if (mConnectThread == null)
            mConnectThread = new ConnectThread(device, uuid);
        else
        {
            mConnectThread.yield();
        }

        this.netview = netview;
        netBayes = net;

        mConnectThread.start();
    }

    /**
     Finally the ConnectedThread which is responsible for maintaining the BTConnection, Sending the data, and
     receiving incoming data through input/output streams respectively.
     **/
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "ConnectedThread: Starting.");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            //dismiss the progressdialog when connection is established
            try{
                mProgressDialog.dismiss();
            }catch (NullPointerException e){
                e.printStackTrace();
            }


            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run(){
            byte[] buffer = new byte[16384];  // buffer store for the stream
            int bytes; // bytes returned from read()
            String incomingMessage = "";
            String processedMessage = "";
            Log.d(TAG,"THREADERROR INIT");

            if (firstTime)
            {
                byte[] binaryString = mode.getBytes(Charset.defaultCharset());
                write(binaryString);
                firstTime = false;
            }
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                // Read from the InputStream
                Log.d(TAG,"THREADERROR LOOP");
                try {
                    bytes = mmInStream.read(buffer);
                    String tmp = new String(buffer, 0, bytes);
                    Log.d(TAG, "MESSAGE RECIEVED: " + tmp);
                    incomingMessage = incomingMessage + tmp;
                    while(incomingMessage.indexOf('#')!=-1)
                    {
                        //incomingMessage = incomingMessage.substring(0,incomingMessage.length()-1);
                        processedMessage = incomingMessage.substring(0,incomingMessage.indexOf('#'));
                        incomingMessage = incomingMessage.substring(incomingMessage.indexOf('#')+1);
                        Log.d(TAG, "InputStream: " + incomingMessage);
                        Log.d(TAG, "MESSAGE IS: " + processedMessage);

                        if(mode == CONTROL_MODE)
                        {
                            processControlString(processedMessage);
                        }
                        else if (mode == AUTONOMOUS_MODE)
                        {
                            processAutonomousString(processedMessage);
                        }

                    }
                } catch (IOException e) {
                    Log.e(TAG, "write: Error reading Input Stream. " + e.getMessage() );
                    break;
                }
            }
        }

        private void processControlString(String msg)
        {
            Log.d(TAG,"THREADERROR PREPROCESS");
            com.example.bilel.bluetoothfirstapp.State currentState = new com.example.bilel.bluetoothfirstapp.State();
            String[] parts = msg.split("-");
            if(parts.length == 2 && !parts[0].equals("") && !parts[1].equals(""))
            {
                currentState.setSensors(Integer.parseInt(parts[0]));
                currentState.setAction(Integer.parseInt(parts[1]));
                if(movEnabled && currentState.getAction()!=5)
                {
                    recordedStates.add(currentState);
                }
                sensorView.updateSensor(1, (currentState.getSensors()%2==1));
                sensorView.updateSensor(2, ((currentState.getSensors()/2)%2==1));
                sensorView.updateSensor(3, ((currentState.getSensors()/4)%2==1));
                sensorView.updateSensor(4, ((currentState.getSensors()/8)%2==1));
                Log.d(TAG,"THREADERROR POSTPROCESS");
            }

        }

        private void processAutonomousString(String msg)
        {
            //TODO: BLA BLA HERE
            String[] parts = msg.split("-");
            if (parts[0].equals(""))
                return;
            int sensors = Integer.parseInt(parts[0]);
            boolean bool1,bool2,bool3,bool4;
            if(sensors%2==1)
                bool1 = true;
            else
                bool1 = false;
            if((sensors/2)%2==1)
                bool2 = true;
            else
                bool2 = false;
            if((sensors/4)%2==1)
                bool3 = true;
            else
                bool3 = false;
            if((sensors/8)%2==1)
                bool4 = true;
            else
                bool4 = false;

            netBayes.updateObservation(bool1,bool2,bool3,bool4);
            InferenceResult result = netBayes.execInference();
            netview.updateSensor(1,bool1);
            netview.updateSensor(2,bool2);
            netview.updateSensor(3,bool3);
            netview.updateSensor(4,bool4);
            netview.updateMotor(result.getAction(),(int) Math.round(result.getLikelihood()));

            if(started)
            {
                autoMove(result);
            }
            else
            {
                String action = "5";
                byte[] binaryString = action.getBytes(Charset.defaultCharset());
                write(binaryString);
            }

        }

        //Call this from the main activity to send data to the remote device
        public void write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG, "write: Writing to outputstream: " + text);
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e(TAG, "write: Error writing to output stream. " + e.getMessage() );
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {
        Log.d(TAG, "connected: Starting.");

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;

        // Synchronize a copy of the ConnectedThread
        Log.d(TAG, "write: Write Called.");
        //perform the write
        mConnectedThread.write(out);
    }

    public void autoMove(InferenceResult result)
    {
        String action = "";
        if (result.getAction().equals("S"))
        {
            action = "5";
            byte[] binaryString = action.getBytes(Charset.defaultCharset());
            write(binaryString);
        }

        if (result.getAction().equals("F"))
        {
            action = "8";
            byte[] binaryString = action.getBytes(Charset.defaultCharset());
            write(binaryString);
        }

        if (result.getAction().equals("R"))
        {
            action = "6";
            byte[] binaryString = action.getBytes(Charset.defaultCharset());
            write(binaryString);
        }

        if (result.getAction().equals("B"))
        {
            action = "2";
            byte[] binaryString = action.getBytes(Charset.defaultCharset());
            write(binaryString);
        }

        if (result.getAction().equals("L"))
        {
            action = "4";
            byte[] binaryString = action.getBytes(Charset.defaultCharset());
            write(binaryString);
        }

        if (result.getAction().equals("FR"))
        {
            action = "9";
            byte[] binaryString = action.getBytes(Charset.defaultCharset());
            write(binaryString);
        }

        if (result.getAction().equals("FL"))
        {
            action = "7";
            byte[] binaryString = action.getBytes(Charset.defaultCharset());
            write(binaryString);
        }

        if (result.getAction().equals("BR"))
        {
            action = "3";
            byte[] binaryString = action.getBytes(Charset.defaultCharset());
            write(binaryString);
        }

        if (result.getAction().equals("BL"))
        {
            action = "1";
            byte[] binaryString = action.getBytes(Charset.defaultCharset());
            write(binaryString);
        }
    }

}
