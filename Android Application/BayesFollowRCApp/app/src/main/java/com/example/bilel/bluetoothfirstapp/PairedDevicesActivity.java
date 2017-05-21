package com.example.bilel.bluetoothfirstapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

public class PairedDevicesActivity extends AppCompatActivity {

    private BluetoothAdapter myBTAdapter;
    private Set<BluetoothDevice> listPairedDevices;
    ListView pairedlv;
    BluetoothDeviceListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paired_devices);
        Initialisation();
        setEvents();
    }


    private void Initialisation()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Typeface font = Typeface.createFromAsset(getAssets(), "NovaFlat.ttf");
        TextView txtPaired = (TextView) findViewById(R.id.txtPaired);
        txtPaired.setTypeface(font);
        myBTAdapter = BluetoothAdapter.getDefaultAdapter();
        listPairedDevices = myBTAdapter.getBondedDevices();
        pairedlv = (ListView) findViewById(R.id.pairedListView);
        adapter = new BluetoothDeviceListAdapter(getApplicationContext(), listPairedDevices);
        pairedlv.setAdapter(adapter);
    }


    public void setEvents()
    {
        pairedlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BluetoothDevice currentDevice = (BluetoothDevice)adapter.getItem(i);
                Intent BackIntent = new Intent();
                BackIntent.putExtra("BTAddress",currentDevice.getAddress());
                setResult(RESULT_OK, BackIntent);
                finish();
            }
        });

    }
}
