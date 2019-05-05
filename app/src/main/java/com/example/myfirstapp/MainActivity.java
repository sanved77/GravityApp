package com.example.myfirstapp;

//import android.Manifest;
//import android.app.Activity;
//import android.content.pm.PackageManager;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
//import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.util.Log;
import android.widget.TextView;

import android.content.Context;

import java.util.Arrays;
//import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    private SensorManager sensorManager;
    private Sensor magnetSensor;

    private Location location;
    protected LocationManager locationManager;
    private Context mContext;

    // flag for GPS status
    public boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1; // 1 minute

    private GeomagneticField geomagneticField;
    private org.apache.log4j.Logger logger;

    private int count;
    private float x_arr[];
    private float y_arr[];
    private float z_arr[];

    int index;
//    int rear;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        magnetSensor = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).get(0);
        sensorManager.registerListener(this, magnetSensor, SensorManager.SENSOR_DELAY_NORMAL);

        //Log4jHelper log4jh = new Log4jHelper(getApplicationContext());
        //logger = log4jh.getLogger("MainActivity");

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

        float min_x = Float.MAX_VALUE;
        float min_y = Float.MAX_VALUE;
        float min_z = Float.MAX_VALUE;

        float max_x = Float.MIN_VALUE;
        float max_y = Float.MIN_VALUE;
        float max_z = Float.MIN_VALUE;
//        rear = 0;

//        /* Get Location */
//        mContext = getApplicationContext();
//
//        try {
//            locationManager = (LocationManager) mContext
//                    .getSystemService(Context.LOCATION_SERVICE);
//
//            // getting GPS status
//            isGPSEnabled = locationManager
//                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//            Log.v("isGPSEnabled", "=" + isGPSEnabled);
//
//            // getting network status
//            isNetworkEnabled = locationManager
//                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//            Log.v("isNetworkEnabled", "=" + isNetworkEnabled);
//
//            if (isGPSEnabled == false && isNetworkEnabled == false) {
//                // no network provider is enabled
//            } else {
//                this.canGetLocation = true;
//                if (isNetworkEnabled) {
//                    location = null;
//                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        // TODO: Consider calling
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more details.
//                        Log.e("Permission issue","Permission not granted to access location");
//                        Toast.makeText(mContext,"Permission not granted to access location", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    locationManager.requestLocationUpdates(
//                            LocationManager.NETWORK_PROVIDER,
//                            MIN_TIME_BW_UPDATES,
//                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                    Log.d("Network", "Network");
//                    if (locationManager != null) {
//                        location = locationManager
//                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                        if (location != null) {
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//                        }
//                    }
//                }
//                // if GPS Enabled get lat/long using GPS Services
//                if (isGPSEnabled) {
//                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        // TODO: Consider calling
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more details.
//                        Log.e("Permission issue","Permission not granted to access location");
//                        Toast.makeText(mContext,"Permission not granted to access location", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    location=null;
//                    if (location == null) {
//                        locationManager.requestLocationUpdates(
//                                LocationManager.GPS_PROVIDER,
//                                MIN_TIME_BW_UPDATES,
//                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                        Log.d("GPS Enabled", "GPS Enabled");
//                        if (locationManager != null) {
//                            location = locationManager
//                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                            if (location != null) {
//                                latitude = location.getLatitude();
//                                longitude = location.getLongitude();
//                            }
//                        }
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(mContext,"Exception in obtaining location", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        geomagneticField = new GeomagneticField((float)location.getLatitude(),
//                (float)location.getLongitude(), (float)location.getAltitude(),location.getTime());
//
//        float x = geomagneticField.getX();
//        float y = geomagneticField.getY();
//        float z = geomagneticField.getZ();
//
//        double geo_metalpower = Math.round(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)));
//
//        TextView textView1 = (TextView)findViewById(R.id.locationTextView);
//        TextView textView2 = (TextView)findViewById(R.id.locationMagneticStrengthTextView);
//
//        textView1.setText("Location long: " + location.getLongitude() + " lat: " + location.getLatitude() + " time: " + location.getTime());
//        textView2.setText("Location X: " + x + " Y: " + y + " Z: " + z + " Geo Strength: " + geo_metalpower);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        TextView textView = (TextView)findViewById(R.id.simpleTextView);
        TextView textView1 = (TextView)findViewById(R.id.locationTextView1);
        TextView textView2 = (TextView)findViewById(R.id.locationTextView2);
        TextView textView3 = (TextView)findViewById(R.id.locationTextView3);
        TextView textView4 = (TextView)findViewById(R.id.locationTextView4);

        //+
        TextView textView5 = (TextView)findViewById(R.id.locationTextView5);
        TextView textView6 = (TextView)findViewById(R.id.locationTextView6);
        TextView textView7 = (TextView)findViewById(R.id.locationTextView7);
        TextView textView8 = (TextView)findViewById(R.id.locationTextView8);
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
        //logger.info(str);
        //-


//        x_arr[count] = x;
//        y_arr[count] = y;
//        z_arr[count] = z;
//        count++;
//
//
//        if (10 == count) {
//            float min_x = x_arr[0], min_y = y_arr[0], min_z = z_arr[0];
//            float max_x = x_arr[0], max_y = y_arr[0], max_z = z_arr[0];
//
//            for (int i = 1; i < count; i++) {
//                if (min_x > x_arr[i]) {
//                    min_x = x_arr[i];
//                }
//                if (min_y > y_arr[i]) {
//                    min_y = y_arr[i];
//                }
//                if (min_z > y_arr[i]) {
//                    min_z = y_arr[i];
//                }
//
//                if (max_x < x_arr[i]) {
//                    max_x = x_arr[i];
//                }
//                if (max_y < y_arr[i]) {
//                    max_y = y_arr[i];
//                }
//                if (max_z < z_arr[i]) {
//                    max_z = z_arr[i];
//                }
//            }
//
//            float avg_delta_x = (max_x - min_x) / 2;
//            float avg_delta_y = (max_y - min_y) / 2;
//            float avg_delta_z = (max_z - min_z) / 2;
//
//            float avg_delta = (avg_delta_x + avg_delta_y + avg_delta_z) / 3;
//
//            float scale_x = 1.0f;
//            if (0 != avg_delta_x) {
//                scale_x = avg_delta / avg_delta_x;
//            }
//            float scale_y = 1.0f;
//            if (0 != avg_delta_y) {
//                scale_y = avg_delta / avg_delta_y;
//            }
//            float scale_z = 1.0f;
//            if (0 != avg_delta_z) {
//                scale_z = avg_delta / avg_delta_z;
//            }
//
//            float corrected_x = x * scale_x;
//            float corrected_y = y * scale_y;
//            float corrected_z = z * scale_z;
//            float corrected_metalpower = Math.round(Math.sqrt(Math.pow(corrected_x, 2)
//                    + Math.pow(corrected_y, 2)
//                    + Math.pow(corrected_z, 2)));
//
//            textView5.setText("Corr X: " + corrected_x);
//            textView6.setText("Corr Y: " + corrected_y);
//            textView7.setText("Corr Z: " + corrected_z);
//            textView8.setText("Corr Strength: " + corrected_metalpower);
//
//            str = new String("Corr X: "
//                    + corrected_x + " Corr Y: "
//                    + corrected_y + " Corr Z: "
//                    + corrected_z + " Corr Strength: "
//                    + metalpower);
//            logger.info(str);
//
//            count = 0;
//        }

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
