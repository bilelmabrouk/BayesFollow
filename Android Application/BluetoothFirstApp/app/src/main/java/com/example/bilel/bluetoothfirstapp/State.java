package com.example.bilel.bluetoothfirstapp;

/**
 * Created by Bilel on 4/1/2017.
 */


public class State {
    private int sensors;
    private int action;

    public State()
    {

    }

    public State(int sensors, int action) {
        this.sensors = sensors;
        this.action = action;
    }

    public int getSensors() {
        return sensors;
    }

    public void setSensors(int sensors) {
        this.sensors = sensors;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
