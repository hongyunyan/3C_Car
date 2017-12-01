package com.example.ok.car_3c;

/**
 * Created by ok on 2017/10/11.
 */
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class GesturemoveActivity extends Activity {
    private GestureDetector gestureDetector;
    private GestureUtils.Screen screen;
    private String input;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    private ListView lv;
    private BluetoothDevice mmDevice;
    private UUID uuid;
    public BluetoothSocket mmSocket;
    private ConnectedThread connectedThread;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesturebymove);

        Button turnback=(Button) findViewById(R.id.turnback);
        turnback.setOnClickListener(listener1);

        gestureDetector = new GestureDetector(this,onGestureListener);
        //得到屏幕的大小
        screen = GestureUtils.getScreenPix(this);
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
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
    GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public void onLongPress(MotionEvent motionEvent){show("stop");input="S";connectedThread.write(input.getBytes());}
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            float x = e2.getX() - e1.getX();
            float y = e2.getY() - e1.getY();
            //限制必须得划过屏幕的1/3才能算划过
            float x_limit = screen.widthPixels / 3;
            float y_limit = screen.heightPixels / 3;
            float x_abs = Math.abs(x);
            float y_abs = Math.abs(y);
            if(x_abs >= y_abs){
                //gesture left or right
                if(x > x_limit || x < -x_limit) {
                    if (x > 0) {
                        //输出向右信号
                        show("right");input="R";
                        connectedThread.write(input.getBytes());

                    } else if (x < 0) {
                        //输出向左信号
                        show("left");input="L";
                        connectedThread.write(input.getBytes());

                    }
                }
            }
            else{
            //gesture down or up
            if(y > y_limit || y < -y_limit){
                if (y > 0) {
                    show("down");
                    input = "B";
                    connectedThread.write(input.getBytes());
                    //输出向下信号

                }else if(y<0){
                    //输出向上信号
                    show("up");input="A";
                    connectedThread.write(input.getBytes());
            }
        }
        }
        return true;
        }
    };
    private View.OnClickListener listener1 = new View.OnClickListener() {
        @Override

        public void onClick(View v) {
            Button btnButton = (Button) v;
            switch (btnButton.getId()) {
                case R.id.turnback:
                    Intent Intent2 = new Intent(GesturemoveActivity.this,Operation.class);
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
    private void show(String value){
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
        }
}
