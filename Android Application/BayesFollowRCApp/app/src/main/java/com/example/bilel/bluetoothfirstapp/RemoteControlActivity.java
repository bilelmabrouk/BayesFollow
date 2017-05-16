package com.example.bilel.bluetoothfirstapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dualcores.swagpoints.SwagPoints;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class RemoteControlActivity extends AppCompatActivity {

    private BluetoothAdapter myBTAdapter;
    private BluetoothDevice targetDevice;
    private RelativeLayout imgLayout;
    private JoyStick js;
    private View.OnTouchListener touchListener;
    private BluetoothManager mBluetoothConnection;
    private static final UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private int last_state = -1;
    private Button finishBTN;
    String btAdress;
    boolean started = false;
    SensorsControlView sensorsView;
    SwagPoints speedBarView;
    int speed = 255;
    boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i0 = getIntent();
        btAdress = i0.getStringExtra("address");
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

        Initialisation();
        InitJoyStick();
        setEvents();
    }

    private void Initialisation() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        myBTAdapter = BluetoothAdapter.getDefaultAdapter();
        Typeface font = Typeface.createFromAsset(getAssets(), "NovaFlat.ttf");
        TextView titre1 = (TextView) findViewById(R.id.titre1);
        TextView titre2 = (TextView) findViewById(R.id.titre2);
        titre1.setTypeface(font);
        titre2.setTypeface(font);
        finishBTN = (Button) findViewById(R.id.finish);
        speedBarView = (SwagPoints) findViewById(R.id.speedView);
        sensorsView = (SensorsControlView) findViewById(R.id.sensorView);

        speedBarView.setPoints(speed);

        mBluetoothConnection = new BluetoothManager(RemoteControlActivity.this);
        mBluetoothConnection.startClientControl(targetDevice,MY_UUID_INSECURE,sensorsView);

    }

    private void InitJoyStick()
    {
        imgLayout = (RelativeLayout)findViewById(R.id.imgLayout);
        //imgLayout.setBackground(getResources().getDrawable(R.drawable.mainicon));
        js = new JoyStick(getApplicationContext()
                , imgLayout, R.drawable.image_button);
        js.setStickSize(80, 80);
        js.setLayoutSize(350, 350);
        js.setLayoutAlpha(200);
        js.setStickAlpha(200);
        js.setOffset(55);
        js.setMinimumDistance(50);
        imgLayout.setEnabled(false);
    }

    private void setEvents()
    {
        js.setLayoutAlpha(255);
        Toast.makeText(getApplicationContext(), "Start Controlling, click on the blue circle", Toast.LENGTH_SHORT).show();

        speedBarView.setOnSwagPointsChangeListener(new SwagPoints.OnSwagPointsChangeListener() {
            @Override
            public void onPointsChanged(SwagPoints swagPoints, int points, boolean fromUser) {
                speed = points;
                String cmd = String.valueOf((char)('J'+(speed/5)));
                Log.d(Constants.TAG, "SPEED = " + (cmd.charAt(0)-'J')*5);
                byte[] bytes = cmd.getBytes(Charset.defaultCharset());
                mBluetoothConnection.write(bytes);
            }

            @Override
            public void onStartTrackingTouch(SwagPoints swagPoints) {

            }

            @Override
            public void onStopTrackingTouch(SwagPoints swagPoints) {

            }
        });

        finishBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (started)
                {
                    imgLayout.setEnabled(false);
                    started = !started;
                    finishBTN.setText("Start");
                    //To Complete with intent and logging stuff
                    ArrayList<State> recordedStates = mBluetoothConnection.getRecordedStates();
                    mBluetoothConnection.resetRecorderStates();
                    mBluetoothConnection.enablemov(false);
                    StateDAO myDAO = new StateDAO(getApplicationContext());
                    myDAO.open();
                    myDAO.insertAllStates(recordedStates);
                    myDAO.close();
                    Intent intent = new Intent(getApplicationContext(), ControlLogActivity.class);
                    startActivity(intent);

                }
                else
                {
                    mBluetoothConnection.enablemov(true);
                    started = !started;
                    finishBTN.setText("Finish");
                    imgLayout.setEnabled(true);
                }
            }
        });
        touchListener = new View.OnTouchListener() {
        public boolean onTouch(View arg0, MotionEvent arg1) {
            if(firstTime)
            {
                firstTime = false;
                String cmd = String.valueOf((char)('J'+(speed/5)));
                byte[] bytes = cmd.getBytes(Charset.defaultCharset());
                mBluetoothConnection.write(bytes);
            }
            js.drawStick(arg1);
            if(arg1.getAction() == MotionEvent.ACTION_DOWN
                    || arg1.getAction() == MotionEvent.ACTION_MOVE) {
//                    textView1.setText("X : " + String.valueOf(js.getX()));
//                    textView2.setText("Y : " + String.valueOf(js.getY()));
//                    textView3.setText("Angle : " + String.valueOf(js.getAngle()));
//                    textView4.setText("Distance : " + String.valueOf(js.getDistance()));
                int direction = js.get8Direction();
                if (direction != last_state)
                {
                    last_state = direction;
                    String cmd = "";
                    byte[] bytes;
                    if(direction == JoyStick.STICK_UP) {
//                        textView5.setText("Direction : Up");
                        cmd = "8";
                        bytes = cmd.getBytes(Charset.defaultCharset());
                        mBluetoothConnection.write(bytes);
                    } else if(direction == JoyStick.STICK_UPRIGHT) {
//                        textView5.setText("Direction : Up Right");
                        cmd = "9";
                        bytes = cmd.getBytes(Charset.defaultCharset());
                        mBluetoothConnection.write(bytes);
                    } else if(direction == JoyStick.STICK_RIGHT) {
//                        textView5.setText("Direction : Right");
                        cmd = "6";
                        bytes = cmd.getBytes(Charset.defaultCharset());
                        mBluetoothConnection.write(bytes);
                    } else if(direction == JoyStick.STICK_DOWNRIGHT) {
//                        textView5.setText("Direction : Down Right");
                        cmd = "3";
                        bytes = cmd.getBytes(Charset.defaultCharset());
                        mBluetoothConnection.write(bytes);
                    } else if(direction == JoyStick.STICK_DOWN) {
//                        textView5.setText("Direction : Down");
                        cmd = "2";
                        bytes = cmd.getBytes(Charset.defaultCharset());
                        mBluetoothConnection.write(bytes);
                    } else if(direction == JoyStick.STICK_DOWNLEFT) {
//                        textView5.setText("Direction : Down Left");
                        cmd = "1";
                        bytes = cmd.getBytes(Charset.defaultCharset());
                        mBluetoothConnection.write(bytes);
                    } else if(direction == JoyStick.STICK_LEFT) {
                        cmd = "4";
                        bytes = cmd.getBytes(Charset.defaultCharset());
                        mBluetoothConnection.write(bytes);
//                        textView5.setText("Direction : Left");
                    } else if(direction == JoyStick.STICK_UPLEFT) {
//                        textView5.setText("Direction : Up Left");
                        cmd = "7";
                        bytes = cmd.getBytes(Charset.defaultCharset());
                        mBluetoothConnection.write(bytes);
                    } else if(direction == JoyStick.STICK_NONE) {
//                        textView5.setText("Direction : Center");
                        cmd = "5";
                        bytes = cmd.getBytes(Charset.defaultCharset());
                        mBluetoothConnection.write(bytes);
                    }
                }
            } else if(arg1.getAction() == MotionEvent.ACTION_UP) {
                last_state = -1;
//                    textView1.setText("X :");
//                    textView2.setText("Y :");
//                    textView3.setText("Angle :");
//                    textView4.setText("Distance :");
//                    textView5.setText("Direction :");
                String cmd = "5";
                byte[] bytes = cmd.getBytes(Charset.defaultCharset());
                mBluetoothConnection.write(bytes);
            }
            return true;
        }
    };
        imgLayout.setOnTouchListener(touchListener);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        /*
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
                btnStart.setEnabled(true);
                btnStart.setTextColor(Color.parseColor("#FFFFFF"));
                btnStart.setBackgroundColor(Color.parseColor("#28878D"));
            }
        }
        */
    }

    @Override
    protected void onDestroy() {
        if(mBluetoothConnection!=null)
        {
            mBluetoothConnection.stopThread();
            mBluetoothConnection = null;
        }
        super.onDestroy();
    }
}
