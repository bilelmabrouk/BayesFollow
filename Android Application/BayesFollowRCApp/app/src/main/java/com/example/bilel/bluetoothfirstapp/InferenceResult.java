package com.example.bilel.bluetoothfirstapp;

/**
 * Created by Bilel on 5/7/2017.
 */

public class InferenceResult
{
    private String action;
    private double likelihood;

    public InferenceResult(String action, double likelihood) {
        this.action = action;
        this.likelihood = likelihood;
    }

    public InferenceResult()
    {

    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public double getLikelihood() {
        return likelihood*100;
    }

    public void setLikelihood(double likelihood) {
        this.likelihood = likelihood;
    }
}
