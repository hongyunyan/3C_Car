package com.example.ok.car_3c;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by ok on 2017/10/11.
 */
public class Operation extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);

        Button move_by_button=(Button) findViewById(R.id.move_by_button);
        move_by_button.setOnClickListener(listener1);

        Button move_by_gesture=(Button) findViewById(R.id.move_by_gesture);
        move_by_gesture.setOnClickListener(listener1);

        Button move_by_voice=(Button) findViewById(R.id.move_by_voice);
        move_by_voice.setOnClickListener(listener1);

        Button move_by_gravity=(Button) findViewById(R.id.move_by_gravity);
        move_by_gravity.setOnClickListener(listener1);

        Button line=(Button) findViewById(R.id.Line);
        line.setOnClickListener(listener1);

        Button camera=(Button) findViewById(R.id.camera);
        camera.setOnClickListener(listener1);

    }

    private View.OnClickListener listener1 = new View.OnClickListener() {
        @Override

        public void onClick(View v) {
            Button btnButton = (Button) v;
            switch (btnButton.getId()) {

                case R.id.move_by_button:
                        Intent Intent1 = new Intent(Operation.this,ButtonmoveActivity.class);
                    startActivity(Intent1);
                    break;
                case R.id.move_by_gesture:
                    Intent Intent2 = new Intent(Operation.this,GesturemoveActivity.class);
                    startActivity(Intent2);
                    break;
                case R.id.move_by_voice:
                    Intent Intent3 = new Intent(Operation.this,VoicemoveActivity.class);
                    startActivity(Intent3);
                    break;
                case R.id.move_by_gravity:
                    Intent Intent4 = new Intent(Operation.this,GravitymoveActivity.class);
                    startActivity(Intent4);
                    break;
                case R.id.Line:
                    Intent Intent5 = new Intent(Operation.this,LineActivity.class);
                    startActivity(Intent5);
                    break;
                case R.id.camera:
                    Intent Intent6 = new Intent(Operation.this,CameraActivity.class);
                    startActivity(Intent6);
                    break;
            }

        }
    };
}
