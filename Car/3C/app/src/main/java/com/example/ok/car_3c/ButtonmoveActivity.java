package com.example.ok.car_3c;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/**
 * Created by ok on 2017/10/11.
 */
public class ButtonmoveActivity extends Activity {

    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    private ListView lv;
    private BluetoothDevice mmDevice;
    private UUID uuid;
    public BluetoothSocket mmSocket;
    private ConnectedThread connectedThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottonbymove);


        Button left=(Button) findViewById(R.id.left);
        left.setOnClickListener(listener1);

        Button right=(Button) findViewById(R.id.right);
        right.setOnClickListener(listener1);

        Button head=(Button) findViewById(R.id.head);
        head.setOnClickListener(listener1);

        Button back=(Button) findViewById(R.id.back);
        back.setOnClickListener(listener1);

        Button stop=(Button) findViewById(R.id.stop);
        stop.setOnClickListener(listener1);

        Button goback=(Button) findViewById(R.id.goback);
        goback.setOnClickListener(listener1);

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

    }

    private View.OnClickListener listener1 = new View.OnClickListener() {
        @Override

        public void onClick(View v) {
            Button btnButton = (Button) v;
            switch (btnButton.getId()) {

                case R.id.left:
                    String inputL="L";
                    connectedThread.write(inputL.getBytes());
                    Toast.makeText(ButtonmoveActivity.this, "left", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.right:
                    String inputR="R";
                    connectedThread.write(inputR.getBytes());
                    Toast.makeText(ButtonmoveActivity.this, "right", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.head:
                    String inputH="A";
                    connectedThread.write(inputH.getBytes());
                    Toast.makeText(ButtonmoveActivity.this, "head", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.back:
                    String inputB="B";
                    connectedThread.write(inputB.getBytes());
                    Toast.makeText(ButtonmoveActivity.this, "back", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.stop:
                    String inputS="S";
                    connectedThread.write(inputS.getBytes());
                    Toast.makeText(ButtonmoveActivity.this, "stop", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.goback:
                    Intent Intent2 = new Intent(ButtonmoveActivity.this,Operation.class);
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
}