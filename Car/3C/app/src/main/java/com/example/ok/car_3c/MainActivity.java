package com.example.ok.car_3c;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends Activity {
    private Button On, Off, Visible, list;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    private ListView lv;
    private BluetoothDevice mmDevice;
    private UUID uuid;
    public BluetoothSocket mmSocket;

    private ConnectedThread connectedThread;

    private OnClickListener listener1 = new OnClickListener() {
            @Override

            public void onClick(View v) {
                Button btnButton = (Button) v;
                switch (btnButton.getId()) {

                    case R.id.connection:
                        Intent Intent1 = new Intent(MainActivity.this,Connection.class);
                        startActivity(Intent1);
                        break;
                    case R.id.operation:
                        Intent Intent2 = new Intent(MainActivity.this,Operation.class);
                        startActivity(Intent2);
                        break;

                }

            }
        };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button connection=(Button) findViewById(R.id.connection);
        connection.setOnClickListener(listener1);

        Button operation=(Button) findViewById(R.id.operation);
        operation.setOnClickListener(listener1);



    }


}
