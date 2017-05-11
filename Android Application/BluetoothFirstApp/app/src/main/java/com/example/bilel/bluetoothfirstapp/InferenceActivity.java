package com.example.bilel.bluetoothfirstapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bayesserver.Network;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class InferenceActivity extends AppCompatActivity
{
    NetworkView netview;
    BayesTest net;
    Button btnStart;
    BluetoothDevice targetDevice;
    boolean started = false;
    BTCopied mBluetoothConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inference);
        Initialisation();
        setEvents();
    }

    private void Initialisation()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Typeface font = Typeface.createFromAsset(getAssets(), "NovaFlat.ttf");

        Intent i0 = getIntent();
        String btAdress = i0.getStringExtra("address");
        Set<BluetoothDevice> liste = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        targetDevice = null;
        for (Iterator<BluetoothDevice> it = liste.iterator(); it.hasNext(); ) {
            BluetoothDevice bt = it.next();
            if (bt.getAddress().equals(btAdress))
            {
                targetDevice = bt;
                break;
            }
        }
        if(targetDevice!=null)
        {
            Toast.makeText(getApplicationContext(),"Selected Device is: " + targetDevice.getName(), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"No Passed Device", Toast.LENGTH_SHORT).show();
        }


        TextView titre31 = (TextView) findViewById(R.id.titre31);
        TextView titre32 = (TextView) findViewById(R.id.titre32);
        titre31.setTypeface(font);
        titre32.setTypeface(font);
        netview = (NetworkView) findViewById(R.id.net);
        net = new BayesTest(getApplicationContext());

        btnStart = (Button) findViewById(R.id.start);

        mBluetoothConnection = new BTCopied(InferenceActivity.this);
        mBluetoothConnection.startClientAutonomous(targetDevice, UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"),netview,net);
    }

    private void setEvents()
    {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!started)
                {
                    started = !started;
                    btnStart.setText("Finish");
                    mBluetoothConnection.enableMotors(true);
                }
                else
                {
                    started = !started;
                    mBluetoothConnection.enableMotors(false);
                    btnStart.setText("Start");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if(mBluetoothConnection != null)
        {
            mBluetoothConnection.stopThread();
            mBluetoothConnection = null;
        }
        super.onDestroy();
    }
}



