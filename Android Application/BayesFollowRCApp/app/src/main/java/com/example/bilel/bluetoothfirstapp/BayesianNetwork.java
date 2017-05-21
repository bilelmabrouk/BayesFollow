package com.example.bilel.bluetoothfirstapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.bayesserver.*;
import com.bayesserver.State;
import com.bayesserver.inference.*;

import java.text.DecimalFormat;

/**
 * Created by Bilel on 4/14/2017.
 */

public class BayesianNetwork
{
    Context ctx;
    Network naiveNet;
    NetworkNodeCollection nodes;
    NetworkLinkCollection links;
    Node sensor1;
    State s1ON;
    State s1OFF;
    Node sensor2;
    State s2ON;
    State s2OFF;
    Node sensor3;
    State s3ON;
    State s3OFF;
    Node sensor4;
    State s4ON;
    State s4OFF;
    Node motor;
    Table tableM;
    State mF;
    State mR;
    State mL;
    State mB;
    State mFR;
    State mFL;
    State mBR;
    State mBL;
    Node[] sensors;
    Table[] tabSensors = new Table[4];
    State[] stSensors;
    double[][] vSensors;
    State[] stMotor;
    double[] vMotor;

    boolean s1 = false;
    boolean s2 = false;
    boolean s3 = false;
    boolean s4 = false;


    public BayesianNetwork(Context ctx)
    {
        this.ctx = ctx;
        initNetwork();
        initMotorState();
        initSensorsStates();
        initLinks();
        loadNetwork();
        setInitialTableMotor();
        setInitialTableSensors();
    }

    public void editMotorValues(double[] paramMotor)
    {
        for(int i=0; i<paramMotor.length; i++)
        {
            vMotor[i] = paramMotor[i];
        }
    }

    public void editSensorValues(int rank, double[] paramSensor)
    {
        for(int i=0; i<paramSensor.length; i++)
        {
            vSensors[rank-1][i] = paramSensor[i];
        }
    }

    private void initNetwork()
    {
        naiveNet = new Network("Naive_Classifier");
        nodes = naiveNet.getNodes();
        links = naiveNet.getLinks();
    }

    private void initMotorState()
    {
        mF = new State("F");
        mR = new State("R");
        mL = new State("L");
        mB = new State("B");
        mFR = new State("FR");
        mFL = new State("FL");
        mBR = new State("BR");
        mBL = new State("BL");
        motor = new Node("Motor", mF, mR, mL, mB,mFR,mFL,mBR,mBL);
        nodes.add(motor);
        stMotor = new State[]{mF,mR,mL,mB,mFR,mFL,mBR,mBL};
        vMotor = new double[]{0.125,0.125,0.125,0.125,0.125,0.125,0.125,0.125};
    }

    private void initSensorsStates()
    {
        s1ON = new State("ON");
        s1OFF = new State("OFF");
        sensor1 = new Node("Sensor1",s1ON,s1OFF);
        nodes.add(sensor1);

        s2ON = new State("ON");
        s2OFF = new State("OFF");
        sensor2 = new Node("Sensor2",s2ON,s2OFF);
        nodes.add(sensor2);

        s3ON = new State("ON");
        s3OFF = new State("OFF");
        sensor3 = new Node("Sensor3",s3ON,s3OFF);
        nodes.add(sensor3);

        s4ON = new State("ON");
        s4OFF = new State("OFF");
        sensor4 = new Node("Sensor4",s4ON,s4OFF);
        nodes.add(sensor4);

        sensors = new Node[]{sensor1,sensor2,sensor3,sensor4};
        stSensors = new State[]{s1ON,s1OFF,s2ON,s2OFF,s3ON,s3OFF,s4ON,s4OFF};
        vSensors = new double[4][];
        for (int i=0; i<4; i++)
        {
            vSensors[i] = new double[] {0.75,0.25,0.75,0.25,0.75,0.25,0.75,0.25,0.75,0.25,0.75,0.25,0.75,0.25,0.75,0.25};
        }

    }

    private void initLinks()
    {
        links.add(new Link(motor,sensor1));
        links.add(new Link(motor,sensor2));
        links.add(new Link(motor,sensor3));
        links.add(new Link(motor,sensor4));
    }

    private void setInitialTableMotor()
    {
        tableM = motor.newDistribution().getTable();
        for (int i=0; i<stMotor.length; i++)
        {
            tableM.set(vMotor[i], stMotor[i]);
            Log.d(Constants.TAG, "BAYES DONE "+i);
        }
        motor.setDistribution(tableM);
    }

    private void setInitialTableSensors()
    {

        for(int i=0; i<sensors.length; i++)
        {
            tabSensors[i] = sensors[i].newDistribution().getTable();
            for (int j=0; j<stMotor.length; j++)
            {
                tabSensors[i].set(vSensors[i][2*j], stMotor[j], stSensors[2*i]);
                tabSensors[i].set(vSensors[i][2*j+1], stMotor[j], stSensors[2*i+1]);
            }
            sensors[i].setDistribution(tabSensors[i]);
        }

    }

    public void saveNetwork()
    {
        SharedPreferences sp = ctx.getSharedPreferences("Bayes",0);

        SharedPreferences.Editor editor = sp.edit();

        String sMotor = "";
        for (int i=0; i<vMotor.length; i++)
        {
            sMotor += String.valueOf(vMotor[i]);
            if(i<vMotor.length-1)
                sMotor+=",";
        }
        editor.putString("vMotor",sMotor);

        String[] sSensors = new String[4];
        for (int i=0; i<vSensors.length; i++)
        {
            sSensors[i] = "";
            for(int j=0; j<vSensors[i].length; j++)
            {
                sSensors[i] += String.valueOf(vSensors[i][j]);
                if(j<vSensors[i].length-1)
                    sSensors[i] += ',';
            }
            editor.putString("vSensor"+i,sSensors[i]);
        }

        editor.commit();
    }

    public void loadNetwork()
    {
        SharedPreferences sp = ctx.getSharedPreferences("Bayes",0);
        String sMotor = sp.getString("vMotor","0.125,0.125,0.125,0.125,0.125,0.125,0.125,0.125");
        String[] vStringMotor = sMotor.split(",");
        for (int i=0; i<vStringMotor.length; i++)
        {
            vMotor[i] = Double.parseDouble(vStringMotor[i]);
        }

        String[] sSensors = new String[4];
        for (int i=0; i<vSensors.length; i++)
        {
            sSensors[i] = sp.getString("vSensor"+i, "0.75,0.25,0.75,0.25,0.75,0.25,0.75,0.25,0.75,0.25,0.75,0.25,0.75,0.25,0.75,0.25");
            String[] vStringSensors = sSensors[i].split(",");
            for(int j=0; j<vStringSensors.length; j++)
            {
                vSensors[i][j] = Double.parseDouble(vStringSensors[j]);
            }
        }
    }

    public void updateObservation(boolean s1, boolean s2, boolean s3, boolean s4)
    {
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
        this.s4 = s4;
    }

    public InferenceResult execInference()
    {
        InferenceFactory factory = new RelevanceTreeInferenceFactory();
        Inference inference = factory.createInferenceEngine(naiveNet);
        QueryOptions queryOptions = factory.createQueryOptions();
        QueryOutput queryOutput = factory.createQueryOutput();
        inference.getEvidence().setState(s1?s1ON:s1OFF);
        inference.getEvidence().setState(s2?s2ON:s2OFF);
        inference.getEvidence().setState(s3?s3ON:s3OFF);
        inference.getEvidence().setState(s4?s4ON:s4OFF);
        Table queryMotor = new Table(motor);
        inference.getQueryDistributions().add(queryMotor);
        try
        {
            inference.query(queryOptions, queryOutput);
        }
        catch (InconsistentEvidenceException e)
        {
            e.printStackTrace();
            Log.d(Constants.TAG, "ERROR BAYES: "+ e.getMessage());
        }

        double maxValue = 0;
        String maxState = "";
        for (int i=0; i<stMotor.length; i++)
        {
            if(maxValue<queryMotor.get(stMotor[i]))
            {
                maxValue=queryMotor.get(stMotor[i]);
                maxState=stMotor[i].getName();
            }
        }
        Log.d(Constants.TAG, "RESULT BAYES: " + "P(Motor|S1=OFF,S2=ON,S3=ON,S4=ON) = {" + queryMotor.get(mF) + "," + queryMotor.get(mR)
                + "," + queryMotor.get(mL) + "," + queryMotor.get(mB) + "," + queryMotor.get(mFR) + "," + queryMotor.get(mFL) + "," +
                queryMotor.get(mBR) + "," + queryMotor.get(mBL) + "}.");
        InferenceResult result = new InferenceResult(maxState,maxValue);
        DecimalFormat df = new DecimalFormat("#.00");
        Log.d(Constants.TAG, "MAX BAYES: " + result.getAction()+ " : " + df.format(result.getLikelihood()) + "%");
        return result;

    }

    public void getMotorVector(double[] vector)
    {
        for (int i=0; i<vMotor.length; i++)
        {
            vector[i] = vMotor[i];
        }
    }

    public void getSensorVector(int id, double[] vector)
    {
        for (int i=0; i<vSensors[id-1].length; i++)
        {
            vector[i] = vSensors[id-1][i];
        }
    }

}
