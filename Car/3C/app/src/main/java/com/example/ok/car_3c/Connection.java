package com.example.ok.car_3c;

/**
 * Created by ok on 2017/10/11.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothSocket;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
//
//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.common.api.GoogleApiClient;

public class Connection extends Activity {

    private Button On, Off, Visible, list,send,clearRecvView;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    private ListView lv;
    private BluetoothDevice mmDevice;
    private UUID uuid;
    public BluetoothSocket mmSocket;
    private ConnectedThread connectedThread = null;
    private EditText sendText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection);
        On = (Button) findViewById(R.id.button1);
        Off = (Button) findViewById(R.id.button2);
        Visible = (Button) findViewById(R.id.button3);
        list = (Button) findViewById(R.id.button4);

        lv = (ListView) findViewById(R.id.listView1);

        BA = BluetoothAdapter.getDefaultAdapter();

        clearRecvView = (Button) findViewById(R.id.clear_recv_view);
        sendText = (EditText) findViewById(R.id.send_text);
        send = (Button) findViewById(R.id.send);
        clearRecvView.setOnClickListener(listener1);
        send.setOnClickListener(listener1);
    }

    public void on(View view) {
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Turned on"
                    , Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Already on",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void list(View view) {
        pairedDevices = BA.getBondedDevices();

        ArrayList list = new ArrayList();
        for (BluetoothDevice bt : pairedDevices)
            list.add(bt.getName());

        Toast.makeText(getApplicationContext(), "Showing Paired Devices",
                Toast.LENGTH_SHORT).show();
        final ArrayAdapter adapter = new ArrayAdapter
                (this, android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);

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

    public void off(View view) {
        BA.disable();
        Toast.makeText(getApplicationContext(), "Turned off",
                Toast.LENGTH_LONG).show();
    }

    public void visible(View view) {
        Intent getVisible = new Intent(BluetoothAdapter.
                ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);

    }


    private View.OnClickListener listener1 = new View.OnClickListener() {
        public void onClick(View v) {
            Button btnButton = (Button) v;
            switch (btnButton.getId()) {
                case R.id.clear_recv_view: // 清空接收框
                    Toast.makeText(getApplicationContext(), "clear",
                            Toast.LENGTH_LONG).show();
                    sendText.setText(null);

                    break;
                case R.id.send:
                    //String inputText = sendText.getText().toString();
                    String inputText = "A";
                    Toast.makeText(Connection.this, inputText, Toast.LENGTH_SHORT).show();
                    connectedThread.write(inputText.getBytes());
                    break;
            }

        }
    };


}

