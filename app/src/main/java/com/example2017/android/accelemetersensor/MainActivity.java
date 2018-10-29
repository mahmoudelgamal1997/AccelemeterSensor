package com.example2017.android.accelemetersensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private Sensor mSensor;
    private SensorManager mSensorManager;
    private TextView txt;
    float last_x=0;
    float last_y=0;
    float last_z=0;
    long lastUpdate;
    int ThreeShold=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mSensor= mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        txt=(TextView)findViewById(R.id.text);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x= sensorEvent.values[0];
        float y= sensorEvent.values[1];
        float z= sensorEvent.values[2];

        long currentTime= System.currentTimeMillis();

        if (currentTime - lastUpdate > 100){
            long differ=currentTime - lastUpdate;
            lastUpdate=currentTime;

            float speed= Math.abs(x+y+z - last_x - last_y -last_z)/differ*10000;

            if (speed > ThreeShold){

                Vibrator v= (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(500);

                txt.setText(String.valueOf(speed));

            }

            last_x=x;
            last_y=y;
            last_z=z;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }
}
