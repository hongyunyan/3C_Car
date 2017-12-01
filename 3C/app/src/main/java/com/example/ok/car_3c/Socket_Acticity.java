package com.example.ok.car_3c;

/**
 * Created by ok on 2017/10/16.
 */
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.ServerSocket;

//import com.example.Thread.RevImageThread;

public class Socket_Acticity extends Activity {
    RevImageThread revImageThread;
    public static ImageView image;
    private static Bitmap bitmap;
    private static final int COMPLETED = 0x111;
    private MyHandler handler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.socket);
        image=(ImageView)findViewById(R.id.imageView1);
        handler = new MyHandler();
        revImageThread = new RevImageThread(handler);
        new Thread(revImageThread).start();

    }

    static class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            if (msg.what == COMPLETED) {
                bitmap = (Bitmap)msg.obj;
                image.setImageBitmap(bitmap);
                super.handleMessage(msg);
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}