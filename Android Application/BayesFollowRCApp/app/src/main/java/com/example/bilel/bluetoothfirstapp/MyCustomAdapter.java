package com.example.bilel.bluetoothfirstapp;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by Bilel on 3/24/2017.
 */

public class MyCustomAdapter extends BaseAdapter
{
    private Context context; //context
    private Set<BluetoothDevice> items; //data source of the list adapter

    //public constructor
    public MyCustomAdapter(Context context, Set<BluetoothDevice> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        int i=0;
        for (Iterator<BluetoothDevice> it = items.iterator(); it.hasNext(); ) {
            BluetoothDevice f = it.next();
            if (i==position)
                return f;
            else
                i++;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.paired_device_item, parent, false);
        }

        // get current item to be displayed
        BluetoothDevice currentDevice = (BluetoothDevice) getItem(position);

        // get the TextView for item name and item description
        TextView txtName = (TextView)
                convertView.findViewById(R.id.blueName);
        TextView txtAddress = (TextView)
                convertView.findViewById(R.id.blueAddress);

        //sets the text for item name and item description from the current item object
        txtName.setText(currentDevice.getName());
        txtAddress.setText(currentDevice.getAddress());

        Typeface font = Typeface.createFromAsset(context.getAssets(), "NovaFlat.ttf");
        txtName.setTypeface(font);
        txtAddress.setTypeface(font);
        // returns the view for the current row
        return convertView;
    }



}
