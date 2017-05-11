package com.example.bilel.bluetoothfirstapp;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Bilel on 4/1/2017.
 */

public class LogAdapter extends BaseAdapter
{
    private Context context; //context
    private ArrayList<State> states; //data source of the list adapter

    //public constructor
    public LogAdapter(Context context, ArrayList<State> states) {
        this.context = context;
        this.states = states;
    }

    @Override
    public int getCount() {
        return states.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return states.get(position);
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
                    inflate(R.layout.log_item, parent, false);
        }

        // get current item to be displayed
        State currentState = (State) getItem(position);

        // get the TextView for item name and item description
        TextView txtAction = (TextView)
                convertView.findViewById(R.id.actionText);
        ImageView imSensor1 = (ImageView) convertView.findViewById(R.id.sensor1);
        ImageView imSensor2 = (ImageView) convertView.findViewById(R.id.sensor2);
        ImageView imSensor3 = (ImageView) convertView.findViewById(R.id.sensor3);
        ImageView imSensor4 = (ImageView) convertView.findViewById(R.id.sensor4);

        //sets the text for item name and item description from the current item object
        String actionString = "";
        switch(currentState.getAction())
        {
            case 1: actionString = "Backward Left"; break;
            case 2: actionString = "Backward"; break;
            case 3: actionString = "Backward Right"; break;
            case 4: actionString = "Left"; break;
            case 5: actionString = "Stop"; break;
            case 6: actionString = "Right"; break;
            case 7: actionString = "Forward Left"; break;
            case 8: actionString = "Forward"; break;
            case 9: actionString = "Forward Right"; break;
            default: break;
        }
        txtAction.setText(actionString);

        Typeface font = Typeface.createFromAsset(context.getAssets(), "NovaFlat.ttf");
        txtAction.setTypeface(font);

        int sensors = currentState.getSensors();

        if(sensors%2==1)
            imSensor1.setImageResource(R.drawable.green_on);
        else
            imSensor1.setImageResource(R.drawable.red_on);
        if((sensors/2)%2==1)
            imSensor2.setImageResource(R.drawable.green_on);
        else
            imSensor2.setImageResource(R.drawable.red_on);
        if((sensors/4)%2==1)
            imSensor3.setImageResource(R.drawable.green_on);
        else
            imSensor3.setImageResource(R.drawable.red_on);
        if((sensors/8)%2==1)
            imSensor4.setImageResource(R.drawable.green_on);
        else
            imSensor4.setImageResource(R.drawable.red_on);
        // returns the view for the current row
        return convertView;
    }
}
