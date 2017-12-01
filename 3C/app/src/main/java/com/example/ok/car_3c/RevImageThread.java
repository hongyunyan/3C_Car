package com.example.ok.car_3c;

/**
 * Created by ok on 2017/10/16.
 */

//充当服务器
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class RevImageThread implements Runnable {

    public Socket s;
    public ServerSocket myss;

    //向UI线程发送消息
    private Handler handler;
    // private BufferedImage bufferedImage;
    private Bitmap bitmap;
    private static final int COMPLETED = 0x111;

    public RevImageThread(Handler handler) {
        this.handler = handler;
    }

    public void run() {
        /*InputStream in = null;
        OutputStream out = null;
        Socket socket = null;
        try {
            ServerSocket serversocket = new ServerSocket(6666);
            System.out.println("服务器等待客户端连接");
            socket = serversocket.accept();
            String ip = socket.getLocalAddress().getHostAddress();
            int port = socket.getPort();
            System.out.print("ip:" + ip + ",端口号" + port);
            while (true) {
                in = socket.getInputStream();
                byte[] buffer = new byte[1024];
                int index = in.read(buffer);
                String receive = new String(buffer, 0, index);
                System.out.println("消息：" + receive);
                if (receive.equals("exit")) break;
                out = socket.getOutputStream();
                String mes = "word";
                out.write(mes.getBytes());
                System.out.println("服务器发送的消息" + mes);
            }
            System.out.print("服务器断开连接");
            in.close();
            out.close();
            socket.close();
            serversocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
        byte[] buffer = new byte[1024];
        int len = 0;


        InputStream ins = null;
        while (true) {
            try {
                myss = new ServerSocket(8888);
            } catch (IOException e2) {
                e2.printStackTrace();
            }

            try {
                //Log.e("strat","ljq");
                s = myss.accept();
                ins = s.getInputStream();

                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                while ((len = ins.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                ins.close();
                OutputStream oos = s.getOutputStream();
                byte data[] = outStream.toByteArray();
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                Message msg = handler.obtainMessage();
                msg.what = COMPLETED;
                msg.obj = bitmap;
                handler.sendMessage(msg);

                outStream.flush();
                outStream.close();
                if (!s.isClosed()) {
                    s.close();
                }


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //Bitmap bitmap = BitmapFactory.decodeStream(ins);

        }
    }
}

