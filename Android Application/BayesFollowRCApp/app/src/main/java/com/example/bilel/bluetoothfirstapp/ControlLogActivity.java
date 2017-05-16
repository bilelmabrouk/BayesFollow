package com.example.bilel.bluetoothfirstapp;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ControlLogActivity extends AppCompatActivity {

    ListView lv;
    LogAdapter logAdapter;
    ArrayList<State> states;
    StateDAO myDAO;
    Button updateButton;
    Button overwriteButton;
    Button returnButton;
    Button resetButton;
    BayesianNetwork netBayes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Initialisation();
        setEvents();
    }

    private void Initialisation()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Typeface font = Typeface.createFromAsset(getAssets(), "NovaFlat.ttf");
        lv = (ListView) findViewById(R.id.list_log);
        myDAO = new StateDAO(getApplicationContext());
        myDAO.open();
        states = myDAO.getAllStates();
        myDAO.close();
        logAdapter = new LogAdapter(getApplicationContext(), states);
        lv.setAdapter(logAdapter);
        updateButton = (Button) findViewById(R.id.btnupdate);
        overwriteButton = (Button) findViewById(R.id.btnoverwrite);
        returnButton = (Button) findViewById(R.id.btnreturn);
        resetButton = (Button) findViewById(R.id.btnreset);
        netBayes = new BayesianNetwork(getApplicationContext());
    }

    private void setEvents()
    {

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Complete code (inspired by the overwrite button's event handler)
                double[] vMotor = new double[]{0,0,0,0,0,0,0,0};
                double[] vSensor1 = new double[]{0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1};
                double[] vSensor2 = new double[]{0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1};
                double[] vSensor3 = new double[]{0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1};
                double[] vSensor4 = new double[]{0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1};

                netBayes.copyMotorVector(vMotor);
                netBayes.copySensorVector(1,vSensor1);
                netBayes.copySensorVector(2,vSensor2);
                netBayes.copySensorVector(3,vSensor3);
                netBayes.copySensorVector(4,vSensor4);

                for (int i=1; i<states.size(); i++)
                {
                    State state = states.get(i);
                    int sensors = state.getSensors();
                    boolean s1,s2,s3,s4;
                    if(sensors%2==1)
                        s1 = true;
                    else
                        s1 = false;
                    if((sensors/2)%2==1)
                        s2 = true;
                    else
                        s2 = false;
                    if((sensors/4)%2==1)
                        s3 = true;
                    else
                        s3 = false;
                    if((sensors/8)%2==1)
                        s4 = true;
                    else
                        s4 = false;

                    int action = state.getAction();
                    if(action == 8)
                    {
                        vSensor1[0] = (vSensor1[0]*vMotor[0]*i + (s1?1:0))/(i+1);
                        vSensor1[1] = 1 - vSensor1[0];
                        vSensor2[0] = (vSensor2[0]*vMotor[0]*i + (s2?1:0))/(i+1);
                        vSensor2[1] = 1 - vSensor2[0];
                        vSensor3[0] = (vSensor3[0]*vMotor[0]*i + (s3?1:0))/(i+1);
                        vSensor3[1] = 1 - vSensor3[0];
                        vSensor4[0] = (vSensor4[0]*vMotor[0]*i + (s4?1:0))/(i+1);
                        vSensor4[1] = 1 - vSensor4[0];
                        vMotor[0] = (vMotor[0]*i+1)/(i+1);
                    }
                    else
                    {
                        vMotor[0] = (vMotor[0]*i)/(i+1);
                    }
                    if(action == 6)
                    {
                        vSensor1[2] = (vSensor1[2]*vMotor[1]*i + (s1?1:0))/(i+1);
                        vSensor1[3] = 1 - vSensor1[2];
                        vSensor2[2] = (vSensor2[2]*vMotor[1]*i + (s2?1:0))/(i+1);
                        vSensor2[3] = 1 - vSensor2[2];
                        vSensor3[2] = (vSensor3[2]*vMotor[1]*i + (s3?1:0))/(i+1);
                        vSensor3[3] = 1 - vSensor3[2];
                        vSensor4[2] = (vSensor4[2]*vMotor[1]*i + (s4?1:0))/(i+1);
                        vSensor4[3] = 1 - vSensor4[2];
                        vMotor[1] = (vMotor[1]*i+1)/(i+1);
                    }
                    else
                    {
                        vMotor[1] = (vMotor[1]*i)/(i+1);
                    }
                    if(action == 4)
                    {
                        vSensor1[4] = (vSensor1[4]*vMotor[2]*i + (s1?1:0))/(i+1);
                        vSensor1[5] = 1 - vSensor1[4];
                        vSensor2[4] = (vSensor2[4]*vMotor[2]*i + (s2?1:0))/(i+1);
                        vSensor2[5] = 1 - vSensor2[4];
                        vSensor3[4] = (vSensor3[4]*vMotor[2]*i + (s3?1:0))/(i+1);
                        vSensor3[5] = 1 - vSensor3[4];
                        vSensor4[4] = (vSensor4[4]*vMotor[2]*i + (s4?1:0))/(i+1);
                        vSensor4[5] = 1 - vSensor4[4];
                        vMotor[2] = (vMotor[2]*i+1)/(i+1);
                    }
                    else
                    {
                        vMotor[2] = (vMotor[2]*i)/(i+1);
                    }
                    if(action == 2)
                    {
                        vSensor1[6] = (vSensor1[6]*vMotor[3]*i + (s1?1:0))/(i+1);
                        vSensor1[7] = 1 - vSensor1[6];
                        vSensor2[6] = (vSensor2[6]*vMotor[3]*i + (s2?1:0))/(i+1);
                        vSensor2[7] = 1 - vSensor2[6];
                        vSensor3[6] = (vSensor3[6]*vMotor[3]*i + (s3?1:0))/(i+1);
                        vSensor3[7] = 1 - vSensor3[6];
                        vSensor4[6] = (vSensor4[6]*vMotor[3]*i + (s4?1:0))/(i+1);
                        vSensor4[7] = 1 - vSensor4[6];
                        vMotor[3] = (vMotor[3]*i+1)/(i+1);
                    }
                    else
                    {
                        vMotor[3] = (vMotor[3]*i)/(i+1);
                    }
                    if(action == 9)
                    {
                        vSensor1[8] = (vSensor1[8]*vMotor[4]*i + (s1?1:0))/(i+1);
                        vSensor1[9] = 1 - vSensor1[8];
                        vSensor2[8] = (vSensor2[8]*vMotor[4]*i + (s2?1:0))/(i+1);
                        vSensor2[9] = 1 - vSensor2[8];
                        vSensor3[8] = (vSensor3[8]*vMotor[4]*i + (s3?1:0))/(i+1);
                        vSensor3[9] = 1 - vSensor3[8];
                        vSensor4[8] = (vSensor4[8]*vMotor[4]*i + (s4?1:0))/(i+1);
                        vSensor4[9] = 1 - vSensor4[8];
                        vMotor[4] = (vMotor[4]*i+1)/(i+1);
                    }
                    else
                    {
                        vMotor[4] = (vMotor[4]*i)/(i+1);
                    }
                    if(action == 7)
                    {
                        vSensor1[10] = (vSensor1[10]*vMotor[5]*i + (s1?1:0))/(i+1);
                        vSensor1[11] = 1 - vSensor1[10];
                        vSensor2[10] = (vSensor2[10]*vMotor[5]*i + (s2?1:0))/(i+1);
                        vSensor2[11] = 1 - vSensor2[10];
                        vSensor3[10] = (vSensor3[10]*vMotor[5]*i + (s3?1:0))/(i+1);
                        vSensor3[11] = 1 - vSensor3[10];
                        vSensor4[10] = (vSensor4[10]*vMotor[5]*i + (s4?1:0))/(i+1);
                        vSensor4[11] = 1 - vSensor4[10];
                        vMotor[5] = (vMotor[5]*i+1)/(i+1);
                    }
                    else
                    {
                        vMotor[5] = (vMotor[5]*i)/(i+1);
                    }
                    if(action == 3)
                    {
                        vSensor1[12] = (vSensor1[12]*vMotor[6]*i + (s1?1:0))/(i+1);
                        vSensor1[13] = 1 - vSensor1[12];
                        vSensor2[12] = (vSensor2[12]*vMotor[6]*i + (s2?1:0))/(i+1);
                        vSensor2[13] = 1 - vSensor2[12];
                        vSensor3[12] = (vSensor3[12]*vMotor[6]*i + (s3?1:0))/(i+1);
                        vSensor3[13] = 1 - vSensor3[12];
                        vSensor4[12] = (vSensor4[12]*vMotor[6]*i + (s4?1:0))/(i+1);
                        vSensor4[13] = 1 - vSensor4[12];
                        vMotor[6] = (vMotor[6]*i+1)/(i+1);
                    }
                    else
                    {
                        vMotor[6] = (vMotor[6]*i)/(i+1);
                    }
                    if(action == 1)
                    {
                        vSensor1[14] = (vSensor1[14]*vMotor[7]*i + (s1?1:0))/(i+1);
                        vSensor1[15] = 1 - vSensor1[14];
                        vSensor2[14] = (vSensor2[14]*vMotor[7]*i + (s2?1:0))/(i+1);
                        vSensor2[15] = 1 - vSensor2[14];
                        vSensor3[14] = (vSensor3[14]*vMotor[7]*i + (s3?1:0))/(i+1);
                        vSensor3[15] = 1 - vSensor3[14];
                        vSensor4[14] = (vSensor4[14]*vMotor[7]*i + (s4?1:0))/(i+1);
                        vSensor4[15] = 1 - vSensor4[14];
                        vMotor[7] = (vMotor[7]*i+1)/(i+1);
                    }
                    else
                    {
                        vMotor[7] = (vMotor[7]*i)/(i+1);
                    }
                    Log.d(Constants.TAG, "vMotor (" + i + ") : " + vMotor[0] + ", " + vMotor[1] + ", " + vMotor[2] + ", " + vMotor[3] + ", " + vMotor[4] + ", " + vMotor[5] + ", " + vMotor[6] + ", " + vMotor[7]);
                }
                Log.d(Constants.TAG, "vMotor Final: " + vMotor[0] + ", " + vMotor[1] + ", " + vMotor[2] + ", " + vMotor[3] + ", " + vMotor[4] + ", " + vMotor[5] + ", " + vMotor[6] + ", " + vMotor[7]);
                netBayes.editMotorValues(vMotor);
                netBayes.editSensorValues(1,vSensor1);
                netBayes.editSensorValues(2,vSensor2);
                netBayes.editSensorValues(3,vSensor3);
                netBayes.editSensorValues(4,vSensor4);
                netBayes.saveNetwork();
                Toast.makeText(getApplicationContext(), "UPADTE SUCCESSFULL", Toast.LENGTH_SHORT).show();
            }
        });

        overwriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double[] vMotor = new double[]{0,0,0,0,0,0,0,0};
                double[] vSensor1 = new double[]{0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1};
                double[] vSensor2 = new double[]{0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1};
                double[] vSensor3 = new double[]{0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1};
                double[] vSensor4 = new double[]{0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1};

                for (int i=0; i<states.size(); i++)
                {
                    State state = states.get(i);
                    int sensors = state.getSensors();
                    boolean s1,s2,s3,s4;
                    if(sensors%2==1)
                        s1 = true;
                    else
                        s1 = false;
                    if((sensors/2)%2==1)
                        s2 = true;
                    else
                        s2 = false;
                    if((sensors/4)%2==1)
                        s3 = true;
                    else
                        s3 = false;
                    if((sensors/8)%2==1)
                        s4 = true;
                    else
                        s4 = false;

                    int action = state.getAction();
                    if(action == 8)
                    {
                        vSensor1[0] = (vSensor1[0]*vMotor[0]*i + (s1?1:0))/(i+1);
                        vSensor1[1] = 1 - vSensor1[0];
                        vSensor2[0] = (vSensor2[0]*vMotor[0]*i + (s2?1:0))/(i+1);
                        vSensor2[1] = 1 - vSensor2[0];
                        vSensor3[0] = (vSensor3[0]*vMotor[0]*i + (s3?1:0))/(i+1);
                        vSensor3[1] = 1 - vSensor3[0];
                        vSensor4[0] = (vSensor4[0]*vMotor[0]*i + (s4?1:0))/(i+1);
                        vSensor4[1] = 1 - vSensor4[0];
                        vMotor[0] = (vMotor[0]*i+1)/(i+1);
                    }
                    else
                    {
                        vMotor[0] = (vMotor[0]*i)/(i+1);
                    }
                    if(action == 6)
                    {
                        vSensor1[2] = (vSensor1[2]*vMotor[1]*i + (s1?1:0))/(i+1);
                        vSensor1[3] = 1 - vSensor1[2];
                        vSensor2[2] = (vSensor2[2]*vMotor[1]*i + (s2?1:0))/(i+1);
                        vSensor2[3] = 1 - vSensor2[2];
                        vSensor3[2] = (vSensor3[2]*vMotor[1]*i + (s3?1:0))/(i+1);
                        vSensor3[3] = 1 - vSensor3[2];
                        vSensor4[2] = (vSensor4[2]*vMotor[1]*i + (s4?1:0))/(i+1);
                        vSensor4[3] = 1 - vSensor4[2];
                        vMotor[1] = (vMotor[1]*i+1)/(i+1);
                    }
                    else
                    {
                        vMotor[1] = (vMotor[1]*i)/(i+1);
                    }
                    if(action == 4)
                    {
                        vSensor1[4] = (vSensor1[4]*vMotor[2]*i + (s1?1:0))/(i+1);
                        vSensor1[5] = 1 - vSensor1[4];
                        vSensor2[4] = (vSensor2[4]*vMotor[2]*i + (s2?1:0))/(i+1);
                        vSensor2[5] = 1 - vSensor2[4];
                        vSensor3[4] = (vSensor3[4]*vMotor[2]*i + (s3?1:0))/(i+1);
                        vSensor3[5] = 1 - vSensor3[4];
                        vSensor4[4] = (vSensor4[4]*vMotor[2]*i + (s4?1:0))/(i+1);
                        vSensor4[5] = 1 - vSensor4[4];
                        vMotor[2] = (vMotor[2]*i+1)/(i+1);
                    }
                    else
                    {
                        vMotor[2] = (vMotor[2]*i)/(i+1);
                    }
                    if(action == 2)
                    {
                        vSensor1[6] = (vSensor1[6]*vMotor[3]*i + (s1?1:0))/(i+1);
                        vSensor1[7] = 1 - vSensor1[6];
                        vSensor2[6] = (vSensor2[6]*vMotor[3]*i + (s2?1:0))/(i+1);
                        vSensor2[7] = 1 - vSensor2[6];
                        vSensor3[6] = (vSensor3[6]*vMotor[3]*i + (s3?1:0))/(i+1);
                        vSensor3[7] = 1 - vSensor3[6];
                        vSensor4[6] = (vSensor4[6]*vMotor[3]*i + (s4?1:0))/(i+1);
                        vSensor4[7] = 1 - vSensor4[6];
                        vMotor[3] = (vMotor[3]*i+1)/(i+1);
                    }
                    else
                    {
                        vMotor[3] = (vMotor[3]*i)/(i+1);
                    }
                    if(action == 9)
                    {
                        vSensor1[8] = (vSensor1[8]*vMotor[4]*i + (s1?1:0))/(i+1);
                        vSensor1[9] = 1 - vSensor1[8];
                        vSensor2[8] = (vSensor2[8]*vMotor[4]*i + (s2?1:0))/(i+1);
                        vSensor2[9] = 1 - vSensor2[8];
                        vSensor3[8] = (vSensor3[8]*vMotor[4]*i + (s3?1:0))/(i+1);
                        vSensor3[9] = 1 - vSensor3[8];
                        vSensor4[8] = (vSensor4[8]*vMotor[4]*i + (s4?1:0))/(i+1);
                        vSensor4[9] = 1 - vSensor4[8];
                        vMotor[4] = (vMotor[4]*i+1)/(i+1);
                    }
                    else
                    {
                        vMotor[4] = (vMotor[4]*i)/(i+1);
                    }
                    if(action == 7)
                    {
                        vSensor1[10] = (vSensor1[10]*vMotor[5]*i + (s1?1:0))/(i+1);
                        vSensor1[11] = 1 - vSensor1[10];
                        vSensor2[10] = (vSensor2[10]*vMotor[5]*i + (s2?1:0))/(i+1);
                        vSensor2[11] = 1 - vSensor2[10];
                        vSensor3[10] = (vSensor3[10]*vMotor[5]*i + (s3?1:0))/(i+1);
                        vSensor3[11] = 1 - vSensor3[10];
                        vSensor4[10] = (vSensor4[10]*vMotor[5]*i + (s4?1:0))/(i+1);
                        vSensor4[11] = 1 - vSensor4[10];
                        vMotor[5] = (vMotor[5]*i+1)/(i+1);
                    }
                    else
                    {
                        vMotor[5] = (vMotor[5]*i)/(i+1);
                    }
                    if(action == 3)
                    {
                        vSensor1[12] = (vSensor1[12]*vMotor[6]*i + (s1?1:0))/(i+1);
                        vSensor1[13] = 1 - vSensor1[12];
                        vSensor2[12] = (vSensor2[12]*vMotor[6]*i + (s2?1:0))/(i+1);
                        vSensor2[13] = 1 - vSensor2[12];
                        vSensor3[12] = (vSensor3[12]*vMotor[6]*i + (s3?1:0))/(i+1);
                        vSensor3[13] = 1 - vSensor3[12];
                        vSensor4[12] = (vSensor4[12]*vMotor[6]*i + (s4?1:0))/(i+1);
                        vSensor4[13] = 1 - vSensor4[12];
                        vMotor[6] = (vMotor[6]*i+1)/(i+1);
                    }
                    else
                    {
                        vMotor[6] = (vMotor[6]*i)/(i+1);
                    }
                    if(action == 1)
                    {
                        vSensor1[14] = (vSensor1[14]*vMotor[7]*i + (s1?1:0))/(i+1);
                        vSensor1[15] = 1 - vSensor1[14];
                        vSensor2[14] = (vSensor2[14]*vMotor[7]*i + (s2?1:0))/(i+1);
                        vSensor2[15] = 1 - vSensor2[14];
                        vSensor3[14] = (vSensor3[14]*vMotor[7]*i + (s3?1:0))/(i+1);
                        vSensor3[15] = 1 - vSensor3[14];
                        vSensor4[14] = (vSensor4[14]*vMotor[7]*i + (s4?1:0))/(i+1);
                        vSensor4[15] = 1 - vSensor4[14];
                        vMotor[7] = (vMotor[7]*i+1)/(i+1);
                    }
                    else
                    {
                        vMotor[7] = (vMotor[7]*i)/(i+1);
                    }
                    Log.d(Constants.TAG, "vMotor (" + i + ") : " + vMotor[0] + ", " + vMotor[1] + ", " + vMotor[2] + ", " + vMotor[3] + ", " + vMotor[4] + ", " + vMotor[5] + ", " + vMotor[6] + ", " + vMotor[7]);
                }
                Log.d(Constants.TAG, "vMotor Final: " + vMotor[0] + ", " + vMotor[1] + ", " + vMotor[2] + ", " + vMotor[3] + ", " + vMotor[4] + ", " + vMotor[5] + ", " + vMotor[6] + ", " + vMotor[7]);
                netBayes.editMotorValues(vMotor);
                netBayes.editSensorValues(1,vSensor1);
                netBayes.editSensorValues(2,vSensor2);
                netBayes.editSensorValues(3,vSensor3);
                netBayes.editSensorValues(4,vSensor4);
                netBayes.saveNetwork();
                Toast.makeText(getApplicationContext(), "OVERWRITE SUCCESSFUL", Toast.LENGTH_SHORT).show();
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDAO.open();
                myDAO.emptyDataBase();
                myDAO.close();
                finish();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                netBayes.editMotorValues(new double[] {0.3,0.15,0.15,0.08,0.12,0.12,0.04,0.04});
                netBayes.editSensorValues(1, new double[] {0.3,0.7,0,1,0.8,0.2,0.15,0.85,0,1,0.7,0.3,0,1,0.3,0.7});
                netBayes.editSensorValues(2, new double[] {0.8,0.2,0.3,0.7,0.7,0.3,0.15,0.85,0.5,0.5,0.5,0.5,0.1,0.9,0.2,0.8});
                netBayes.editSensorValues(3, new double[] {0.8,0.2,0.7,0.3,0.3,0.7,0.15,0.85,0.8,0.2,0.8,0.2,0.2,0.8,0.1,0.9});
                netBayes.editSensorValues(4, new double[] {0.3,0.7,0.8,0.2,0,1,0.15,0.85,0.7,0.3,0,1,0.3,0.7,0,1});
                netBayes.saveNetwork();
                Toast.makeText(getApplicationContext(), "RESET SUCCESSFUL",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
