package com.example.myfirstapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;

//import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.util.Log;
import android.widget.TextView;

import android.content.Context;

import java.util.Arrays;

import az.plainpie.PieView;

public class StartScreen extends AppCompatActivity implements SensorEventListener, LocationListener {

    // UI
    public  TextView tvShakeService;
    public  TextView msg;
    TextView textView1,textView2,textView3,textView4,textView5,textView6,textView7,textView8;
    public  Button test;
    public  PieView pieView;

    // Components
    private SensorManager sensorManager;
    private Sensor magnetSensor;

    // Data
    private float x_arr[];
    private float y_arr[];
    private float z_arr[];

    // Hard
    String[][] msgNormal = {
            {"Normal Range", "Device with enclosed metal around", "Electronic Device Present - E.g TV", "Electronic Device Present - E.g Laptop, Desktop"},
            {"Normal Range", "Presence of light wiring", "Presence of medium wiring", "Presence of high wiring"}
    };

    int index;

    int mode = 0;

    int count;

    float rsum_x;
    float rsum_y;
    float rsum_z;

    float min_x;
    float min_y;
    float min_z;

    float max_x;
    float max_y;
    float max_z;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);

         textView1 = (TextView)findViewById(R.id.locationTextView1);
         textView2 = (TextView)findViewById(R.id.locationTextView2);
         textView3 = (TextView)findViewById(R.id.locationTextView3);
         textView4 = (TextView)findViewById(R.id.locationTextView4);

        //+
         textView5 = (TextView)findViewById(R.id.locationTextView5);
         textView6 = (TextView)findViewById(R.id.locationTextView6);
         textView7 = (TextView)findViewById(R.id.locationTextView7);
         textView8 = (TextView)findViewById(R.id.locationTextView8);

        Intent intent = new Intent(this, ShakeService.class);
        startService(intent);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        magnetSensor = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).get(0);
        sensorManager.registerListener(this, magnetSensor, SensorManager.SENSOR_DELAY_NORMAL);

        // pie
        pieView = findViewById(R.id.pieView);
        pieView.setPercentageBackgroundColor(getResources().getColor(R.color.blue));

        tvShakeService = findViewById(R.id.tvShakeService);
        msg = findViewById(R.id.tvMsg);
        test = findViewById(R.id.bTest);

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mode == 1) {
                    mode = 0;
                    test.setText("Mode - Electronic Device");
                }else{
                    mode = 1;
                    test.setText("Mode - Buried Power Lines");
                }


            }
        });

        count = 0;
        x_arr = new float[10];
        Arrays.fill(x_arr, 0);
        y_arr = new float[10];
        Arrays.fill(y_arr, 0);
        z_arr = new float[10];
        Arrays.fill(z_arr, 0);

        rsum_x = 0.0f;
        rsum_y = 0.0f;
        rsum_z = 0.0f;

        index = 0;

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        TextView textView = (TextView)findViewById(R.id.simpleTextView);

        //-

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        double metalpower = Math.round(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)));

        String str = new String("X: " + x + " Y: " + y + " Z: " + z + " Strength: " + metalpower);
        //logger.info(str);
        textView1.setText("X: " + x);
        textView2.setText("Y: " + y);
        textView3.setText("Z: " + z);
        textView4.setText("Strength: " + metalpower);

        //+
        rsum_x = rsum_x - x_arr[index] + x;
        rsum_y = rsum_y - y_arr[index] + y;
        rsum_z = rsum_z - z_arr[index] + z;

        x_arr[index] = x;
        y_arr[index] = y;
        z_arr[index] = z;


        if (9 == index) {
            index = 0;
        } else {
            index++;
        }

        if (min_x > x) {
            min_x = x;
        }
        if (min_y > y) {
            min_y = y;
        }
        if (min_z > z) {
            min_z = z;
        }

        if (max_x > x) {
            max_x = x;
        }
        if (max_y > y) {
            max_y = y;
        }
        if (max_z > z) {
            max_z = z;
        }

        float avg_delta_x = (max_x - min_x) / 2;
        float avg_delta_y = (max_y - min_y) / 2;
        float avg_delta_z = (max_z - min_z) / 2;

        float avg_delta = (avg_delta_x + avg_delta_y + avg_delta_z) / 3;

        float scale_x = 1.0f;
        if (0 != avg_delta_x) {
            scale_x = avg_delta / avg_delta_x;
        }
        float scale_y = 1.0f;
        if (0 != avg_delta_y) {
            scale_y = avg_delta / avg_delta_y;
        }
        float scale_z = 1.0f;
        if (0 != avg_delta_z) {
            scale_z = avg_delta / avg_delta_z;
        }

        float corrected_x = x * scale_x;
        float corrected_y = y * scale_y;
        float corrected_z = z * scale_z;
        float corrected_metalpower = Math.round(Math.sqrt(Math.pow(corrected_x, 2)
                + Math.pow(corrected_y, 2)
                + Math.pow(corrected_z, 2)));

        textView5.setText("Corr X: " + corrected_x);
        textView6.setText("Corr Y: " + corrected_y);
        textView7.setText("Corr Z: " + corrected_z);
        textView8.setText("Corr Strength: " + corrected_metalpower);

        str = new String("Corr X: "
                + corrected_x + " Corr Y: "
                + corrected_y + " Corr Z: "
                + corrected_z + " Corr Strength: "
                + corrected_metalpower);


        // UI setup
        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);

        pieView.setPercentage( corrected_metalpower/2);
        pieView.setInnerText("" + corrected_metalpower);

        if(mode == 1){
            if(corrected_metalpower < 70){
                msg.setTextColor(ContextCompat.getColor(StartScreen.this, R.color.green));
                pieView.setPercentageBackgroundColor(ContextCompat.getColor(StartScreen.this, R.color.green));
                msg.setText(msgNormal[mode][0]);
            }else if(corrected_metalpower >= 70 && corrected_metalpower < 90){
                msg.setTextColor(ContextCompat.getColor(StartScreen.this, R.color.yellow));
                pieView.setPercentageBackgroundColor(ContextCompat.getColor(StartScreen.this, R.color.yellow));
                msg.setText(msgNormal[mode][1]);
                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,300);
            }else if(corrected_metalpower >= 90 && corrected_metalpower < 169){
                msg.setTextColor(ContextCompat.getColor(StartScreen.this, R.color.orange));
                pieView.setPercentageBackgroundColor(ContextCompat.getColor(StartScreen.this, R.color.orange));
                msg.setText(msgNormal[mode][2]);
                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,200);
            }else if(corrected_metalpower >= 170){
                msg.setTextColor(ContextCompat.getColor(StartScreen.this, R.color.red));
                pieView.setPercentageBackgroundColor(ContextCompat.getColor(StartScreen.this, R.color.red));
                msg.setText(msgNormal[mode][3]);
                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,100);
            }
            toneGen1.release();
        }else {
            if(corrected_metalpower < 80){
                msg.setTextColor(ContextCompat.getColor(StartScreen.this, R.color.green));
                pieView.setPercentageBackgroundColor(ContextCompat.getColor(StartScreen.this, R.color.green));
                msg.setText(msgNormal[mode][0]);
            }else if(corrected_metalpower >= 80 && corrected_metalpower < 100){
                msg.setTextColor(ContextCompat.getColor(StartScreen.this, R.color.yellow));
                pieView.setPercentageBackgroundColor(ContextCompat.getColor(StartScreen.this, R.color.yellow));
                msg.setText(msgNormal[mode][1]);
                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,300);
            }else if(corrected_metalpower >= 100 && corrected_metalpower < 200){
                msg.setTextColor(ContextCompat.getColor(StartScreen.this, R.color.orange));
                pieView.setPercentageBackgroundColor(ContextCompat.getColor(StartScreen.this, R.color.orange));
                msg.setText(msgNormal[mode][2]);
                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,200);
            }else if(corrected_metalpower >= 200){
                msg.setTextColor(ContextCompat.getColor(StartScreen.this, R.color.red));
                pieView.setPercentageBackgroundColor(ContextCompat.getColor(StartScreen.this, R.color.red));
                msg.setText(msgNormal[mode][3]);
                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,100);
            }
            toneGen1.release();
        }

        //pieView.setMainBackgroundColor(ContextCompat.getColor(StartScreen.this, R.color.orange));
        //pieView.setInnerBackgroundColor(ContextCompat.getColor(StartScreen.this, R.color.darkred));


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(Location location) {
//        geomagneticField = new GeomagneticField((float)location.getLatitude(),
//                (float)location.getLongitude(), (float)location.getAltitude(),location.getTime());
//
//        float x = geomagneticField.getX();
//        float y = geomagneticField.getY();
//        float z = geomagneticField.getZ();
//
//        double geo_metalpower = Math.round(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)));
//
//
//        textView1.setText("Location long: " + location.getLongitude() + " lat: " + location.getLatitude() + " time: " + location.getTime());
//        textView2.setText("Location X: " + x + " Y: " + y + " Z: " + z + " Geo Strength: " + geo_metalpower);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
