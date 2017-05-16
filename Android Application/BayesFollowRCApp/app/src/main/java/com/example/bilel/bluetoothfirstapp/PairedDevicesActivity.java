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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

public class PairedDevicesActivity extends AppCompatActivity {

    private BluetoothAdapter myBTAdapter;
    private Set<BluetoothDevice> listPairedDevices;
    private ArrayList<String> liste = new ArrayList<String> ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_device);
        myBTAdapter = BluetoothAdapter.getDefaultAdapter();
        Initialisation();
    }


    private void Initialisation()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Typeface font = Typeface.createFromAsset(getAssets(), "NovaFlat.ttf");
        TextView txtPaired = (TextView) findViewById(R.id.txtPaired);
        txtPaired.setTypeface(font);
        listPairedDevices = myBTAdapter.getBondedDevices();
        ListView pairedlv = (ListView) findViewById(R.id.pairedListView);
        ArrayAdapter<String> avadapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1 ,liste);
        pairedlv.setAdapter(avadapter);
        final BluetoothDeviceListAdapter adapter = new BluetoothDeviceListAdapter(getApplicationContext(), listPairedDevices);
        pairedlv.setAdapter(adapter);
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
