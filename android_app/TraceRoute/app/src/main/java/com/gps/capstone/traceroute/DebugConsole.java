package com.gps.capstone.traceroute;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gps.capstone.traceroute.GLFiles.OpenGL;
import com.gps.capstone.traceroute.listeners.AccelerometerListener;
import com.gps.capstone.traceroute.listeners.BarometerListener;
import com.gps.capstone.traceroute.listeners.DirectionListener;
import com.gps.capstone.traceroute.listeners.GravityListener;
import com.gps.capstone.traceroute.listeners.GyroscopeListner;
import com.gps.capstone.traceroute.listeners.LinearAccelerationListener;
import com.gps.capstone.traceroute.listeners.StepCounterListener;
import com.gps.capstone.traceroute.listeners.StepDetectorListener;
import com.gps.capstone.traceroute.settings.UserSettings;

public class DebugConsole extends ActionBarActivity {
    // Tag used for logging
    private final String TAG = this.getClass().getSimpleName();

    // The sensor data will provide us data and we can register things with it
    private SensorManager sensorManager;

    // Lets grab each of the sensors we will be using and their corresponding listener
    private Sensor mAccelerometer;
    private SensorEventListener mAccelerometerListener;
    private Sensor mGyroscope;
    private SensorEventListener mGyroscopeListener;
    private Sensor mBarometer;
    private SensorEventListener barometerListener;
    private Sensor mLinearAcceleration;
    private SensorEventListener mLinearAccelerationListener;
    private Sensor mGravity;
    private SensorEventListener mGravityListener;
    private Sensor mStepCounter;
    private SensorEventListener mStepCounterListener;
    private Sensor mStepDetector;
    private SensorEventListener mStepDetectorListener;
    private Sensor mDirectionVector;
    private Sensor mGeomagneticDV;
    private SensorEventListener mDirectionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_console);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Grab the mAccelerometer
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccelerometerListener = new AccelerometerListener((RelativeLayout) findViewById(R.id.acc_values));

        // Grab the mGyroscope
        mGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mGyroscopeListener = new GyroscopeListner((RelativeLayout) findViewById(R.id.gyro_values));

        // Grab the mBarometer
        mBarometer = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        barometerListener = new BarometerListener((RelativeLayout) findViewById(R.id.barr_values));

        // Grab the Linear Acceleration (Software sensor I think)
        mLinearAcceleration = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mLinearAccelerationListener = new LinearAccelerationListener((RelativeLayout) findViewById(R.id.lin_acc_values));

        // Grab the mGravity sensor
        mGravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mGravityListener = new GravityListener((RelativeLayout) findViewById(R.id.grav_values));

        // Grab the step counter
        mStepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (mStepCounter == null) {
            Log.e(TAG, "FUCK");
        }
        mStepCounterListener = new StepCounterListener((RelativeLayout) findViewById(R.id.step_vals));

        // Grab the step detector
        mStepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        mStepDetectorListener = new StepDetectorListener((RelativeLayout) findViewById(R.id.step_detect_vals));

        // Get the direction vectors and listener
        mDirectionVector = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mGeomagneticDV = sensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR);
        if (mGeomagneticDV == null) {
            Log.e(TAG, "AHH");
        }
        mDirectionListener = new DirectionListener((RelativeLayout) findViewById(R.id.direction_vals));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "Registering the listeners", Toast.LENGTH_SHORT).show();

        Log.d(TAG, "Registered the listeners");

        // Register all the sensors with the listeners
        sensorManager.registerListener(mAccelerometerListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(mGyroscopeListener, mGyroscope, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(barometerListener, mBarometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(mLinearAccelerationListener, mLinearAcceleration, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(mGravityListener, mGravity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(mStepCounterListener, mStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(mStepDetectorListener, mStepDetector, SensorManager.SENSOR_DELAY_NORMAL, Sensor.REPORTING_MODE_SPECIAL_TRIGGER);
        sensorManager.registerListener(mDirectionListener, mDirectionVector, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(mDirectionListener, mGeomagneticDV, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "Unregister the listeners", Toast.LENGTH_SHORT).show();

        Log.d(TAG, "Unregistered the listeners");

        // Unregister the listeners, I'm not sure how this will factor in with the phone going to sleep
        sensorManager.unregisterListener(mAccelerometerListener);
        sensorManager.unregisterListener(mGyroscopeListener);
        sensorManager.unregisterListener(barometerListener);
        sensorManager.unregisterListener(mLinearAccelerationListener);
        sensorManager.unregisterListener(mGravityListener);
        sensorManager.unregisterListener(mStepCounterListener);
        sensorManager.unregisterListener(mStepDetectorListener);
        sensorManager.unregisterListener(mDirectionListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Depending on what the user clicks lets start that activity
        Intent i = null;
        switch (id) {
            case R.id.user_settings:
                i = new Intent(this, UserSettings.class);
                break;
            case R.id.open_gl_view:
                i = new Intent(this, OpenGL.class);
                break;
        }
        if (i != null)
            startActivity(i);
        return super.onOptionsItemSelected(item);
    }

}