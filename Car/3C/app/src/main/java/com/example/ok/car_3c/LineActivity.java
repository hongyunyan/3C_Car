package com.example.ok.car_3c;

import android.app.Activity;

import java.io.IOException;
import java.security.PublicKey;
import java.util.Set;
import java.util.UUID;

import android.R.integer;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by ok on 2017/10/13.
 */
public class LineActivity extends Activity {
    private Drawl bDrawl;
    private GestureUtils.Screen screen;
    private String input;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    private ListView lv;
    private BluetoothDevice mmDevice;
    private UUID uuid;
    public BluetoothSocket mmSocket;
    private ConnectedThread connectedThread=null;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bDrawl = new Drawl(this);
        setContentView(bDrawl);//将view视图放到Activity中显示

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

    //新建一个类继承View

    public class Drawl extends View {

        private int mov_x;//声明起点坐标
        private int mov_y;
        private Paint paint;//声明画笔
        private Canvas canvas;//画布
        private Bitmap bitmap;//位图
        private int blcolor;

        public Drawl(Context context) {
            super(context);
            screen = GestureUtils.getScreenPix(LineActivity.this);
            paint = new Paint(Paint.DITHER_FLAG);//创建一个画笔
            bitmap = Bitmap.createBitmap(screen.widthPixels, screen.heightPixels, Bitmap.Config.ARGB_8888); //设置位图的宽高
            canvas = new Canvas();
            canvas.setBitmap(bitmap);

            paint.setStyle(Style.STROKE);//设置非填充
            paint.setStrokeWidth(5);//笔宽5像素
            paint.setColor(Color.RED);//设置为红笔
            paint.setAntiAlias(true);//锯齿不显示

        }

        //画位图
        @Override
        protected void onDraw(Canvas canvas) {
//  super.onDraw(canvas);
            canvas.drawBitmap(bitmap, 0, 0, null);
        }

        //触摸事件
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {//如果拖动
                canvas.drawLine(mov_x, mov_y, event.getX(), event.getY(), paint);//画线
                float k=(event.getY()-mov_y)/(event.getX()-mov_x);
                if (k>0)
                {//到时候再处理一下度数
                    if (event.getX()-mov_x>0)
                    {
                        if (k>1) {showing("后");input="B";connectedThread.write(input.getBytes());}
                        else {showing("左");input="L";connectedThread.write(input.getBytes());}
                    }
                    else
                    {
                        if (k>1) {showing("前");input="A";connectedThread.write(input.getBytes());}
                        else {showing("右");input="R";connectedThread.write(input.getBytes());}
                    }
                }
                else
                if (event.getX()-mov_x<0)
                {
                    if (k<-1) {showing("后");input="B";connectedThread.write(input.getBytes());}
                    else {showing("左");input="L";connectedThread.write(input.getBytes());}
                }
                else
                {
                    if (k<-1) {showing("前");input="A";connectedThread.write(input.getBytes());}
                    else {showing("右");input="R";connectedThread.write(input.getBytes());}
                }

                invalidate();
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN) {//如果点击
                canvas.drawColor(Color.WHITE);
                String inputS="S";
                connectedThread.write(inputS.getBytes());
                showing("停");

            }
            if (event.getAction() == MotionEvent.ACTION_UP) {//如果L
                String inputS="S";
                connectedThread.write(inputS.getBytes());
                showing("停");

            }
            mov_x = (int) event.getX();
            mov_y = (int) event.getY();
            return true;

        }
    }
    private void showing(String value){
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
    }
}






