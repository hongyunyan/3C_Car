package com.example.ok.car_3c;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import static java.lang.Math.abs;

/**
 * Created by ok on 2017/10/13.
 */
public class GravitymoveActivity extends Activity {
    private SensorManager sensorMgr;
    boolean flag=false;
    Sensor sensor;
    TextView textX = null;
    TextView textY = null;
    TextView textZ = null;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    private ListView lv;
    private BluetoothDevice mmDevice;
    private UUID uuid;
    public BluetoothSocket mmSocket;
    private ConnectedThread connectedThread = null;
    private String input;
    private float x, y, z;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravitybymove);

        Button turnback = (Button) findViewById(R.id.turnback);
        turnback.setOnClickListener(listener1);
        Button stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(listener1);
        Button start = (Button) findViewById(R.id.start);
        start.setOnClickListener(listener1);

        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        textX = (TextView) findViewById(R.id.textx);
        textY = (TextView) findViewById(R.id.texty);
        textZ = (TextView) findViewById(R.id.textz);
        BA = BluetoothAdapter.getDefaultAdapter();


        pairedDevices = BA.getBondedDevices();

        if (pairedDevices.size() != 0)
            mmDevice = pairedDevices.iterator().next();
        uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        try {
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        } catch (Exception e) {
        }
        try {
            mmSocket.connect();
            connectedThread = new ConnectedThread(mmSocket);
            connectedThread.start();
        } catch (IOException connectException) {
        }

        SensorEventListener lsn = new SensorEventListener() {
            public void onSensorChanged(SensorEvent e) {
                x = e.values[0];
                y = e.values[1];
                z = e.values[2];
                float x1 = Math.abs(x);
                float y1 = Math.abs(y);
                float z1 = Math.abs(z - 9);
                if (flag){
                if ((x1 > y1) && (x1 > 3)) {
                    if (x > 0) {showing("left");input="L";connectedThread.write(input.getBytes());}
                    else {showing("right");input="R";connectedThread.write(input.getBytes());}
                } else if ((x1 < y1) && (y1 > 3)) {
                    if (y < 0) {showing("down");input="B";connectedThread.write(input.getBytes());}
                    else {showing("up");input="A";connectedThread.write(input.getBytes());}
                } else {showing("stop");input="S";connectedThread.write(input.getBytes());}}
            }
            public void onAccuracyChanged(Sensor s, int accuracy) {
            }
        };
        // 注册listener，第三个参数是检测的精确度
        sensorMgr.registerListener(lsn, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    private View.OnClickListener listener1 = new View.OnClickListener() {
        @Override

        public void onClick(View v) {
            Button btnButton = (Button) v;
            switch (btnButton.getId()) {
                case R.id.stop:
                    flag=false;
                    break;
                case R.id.start:
                    flag=true;
                    break;
                case R.id.turnback:
                    Intent Intent2 = new Intent(GravitymoveActivity.this,Operation.class);
                    String input="S";
                    connectedThread.write(input.getBytes());
                    try {
                        mmSocket.close();
                    } catch (IOException connectException) {
                    }
                    startActivity(Intent2);
                    break;
            }

        }
    };
    private void showing(String value){
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
    }
}
