package com.example.bilel.bluetoothfirstapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class Main2 extends AppCompatActivity {

    private BluetoothAdapter myBTAdapter;
    private BluetoothDevice targetDevice;
    private Button btnConnect;
    private Button btnSelect;
    private ImageButton autonomousMode;
    private ImageButton controlMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Initialisation();
        setEvents();
    }

    private void Initialisation()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Typeface font = Typeface.createFromAsset(getAssets(), "NovaFlat.ttf");
        TextView titre22 = (TextView) findViewById(R.id.titre22);
        titre22.setTypeface(font);

        myBTAdapter = BluetoothAdapter.getDefaultAdapter();
        btnConnect = (Button) findViewById(R.id.connect2);
        btnSelect = (Button) findViewById(R.id.select_dev2);

        autonomousMode = (ImageButton) findViewById(R.id.autoMode);
        controlMode = (ImageButton) findViewById(R.id.controlMode);

        if (myBTAdapter == null) {
            Toast.makeText(getApplicationContext(), "Your device does not support Bluetooth",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Bluetooth works correctly in your device", Toast.LENGTH_SHORT).show();
            if (!myBTAdapter.isEnabled())
            {
                btnConnect.setText("Connect To Bluetooth");
                btnSelect.setEnabled(false);
                btnSelect.setTextColor(Color.parseColor("#5A5A5A"));
                btnSelect.setBackgroundColor(Color.parseColor("#1C1D26"));

                //btnConnect.setBackgroundColor(Color.parseColor("#0d6673"));
            }
            else
            {
                btnConnect.setText("Disconnect From Bluetooth");
                btnSelect.setEnabled(true);
                btnSelect.setTextColor(Color.parseColor("#FFFFFF"));
                btnSelect.setBackgroundColor(Color.parseColor("#28878D"));
                //btnConnect.setBackgroundColor(Color.GRAY);
            }
        }
    }
    private void setEvents()
    {
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!myBTAdapter.isEnabled())
                {
                    if (enableBluetooth())
                    {

                        //btnConnect.setBackgroundColor(Color.parseColor("#0d6673"));
                    }

                }
                else
                {
                    myBTAdapter.disable();
                    btnConnect.setText("Connect To Bluetooth");
                    btnSelect.setEnabled(false);
                    btnSelect.setTextColor(Color.parseColor("#5A5A5A"));
                    btnSelect.setBackgroundColor(Color.parseColor("#1C1D26"));
                }
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selectDeviceIntent = new Intent(getApplicationContext(), SelectDeviceActivity.class);
                startActivityForResult(selectDeviceIntent, 2);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == 1 && resultCode == RESULT_OK)
        {
            btnConnect.setText("Disconnect From Bluetooth");
            btnSelect.setEnabled(true);
            btnSelect.setTextColor(Color.parseColor("#FFFFFF"));
            btnSelect.setBackgroundColor(Color.parseColor("#28878D"));
        }
        if(requestCode == 2 && resultCode == RESULT_OK)
        {
            String address = data.getStringExtra("BTAddress");
            Set<BluetoothDevice> liste = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
            for (Iterator<BluetoothDevice> it = liste.iterator(); it.hasNext(); ) {
                BluetoothDevice bt = it.next();
                if (bt.getAddress().equals(address))
                {
                    targetDevice = bt;
                    break;
                }
            }
            if(targetDevice!=null)
            {
                Toast.makeText(getApplicationContext(), targetDevice.getName() + " is selected", Toast.LENGTH_SHORT).show();
                /*
                btnStart.setEnabled(true);
                btnStart.setTextColor(Color.parseColor("#FFFFFF"));
                btnStart.setBackgroundColor(Color.parseColor("#28878D"));
                */
                controlMode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("address", targetDevice.getAddress());
                        startActivity(intent);
                    }
                });
                autonomousMode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i2 = new Intent(getApplicationContext(), InferenceActivity.class);
                        i2.putExtra("address", targetDevice.getAddress());
                        startActivity(i2);
                    }
                });
            }
        }
    }

    private boolean enableBluetooth()
    {
        if(!myBTAdapter.isEnabled())
        {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, 1);
            return true;
        }
        return false;
    }
}
