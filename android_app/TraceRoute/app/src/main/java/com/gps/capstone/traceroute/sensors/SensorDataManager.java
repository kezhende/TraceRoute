package com.gps.capstone.traceroute.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import com.gps.capstone.traceroute.BusProvider;
import com.squareup.otto.Bus;

/**
 * Created by saryana on 4/11/15.
 */
public class SensorDataManager implements SensorEventListener {
    // Tag for logging
    private final String TAG = this.getClass().getSimpleName();

    // Context we were created in
    private Context mContext;
    // Sensor manager we are using
    private SensorManager mSensorManager;
    // Current acceleration values
    private float[] mAccelVals;
    // Current gravity values
    private float[] mGravVals;
    // Gravity sensor
    private Sensor mGravitySensor;
    // Accelerometer sensor
    private Sensor mAccelerationSensor;

    // Singleton instance of the SensorDataManager
//    private static SensorDataManager mInstance;

//    public static void createInstance(Context c, Bus b) {
//        if (mInstance == null) {
//            mInstance = new SensorDataManager(c, b);
//        }
//    }

    /**
     * Creates a new SensorDataManager that post evens about new data being received from the
     * accelerometer and gravity sensor for now
     * @param context Context we are being called in
     */
    public SensorDataManager(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mContext = context;
        // Grab and register listeners for the accelerometer and the gravity sensors
        mAccelerationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGravitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        register();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mAccelVals = event.values;
        } else {
            mGravVals = event.values;
        }
        // If we don't have any new data, we can't compute the orientation and post an event
        if (mAccelVals != null && mGravVals != null) {
            // Rotation matrix
            float[] R = new float[16];
            // Inclination of the phone
            float[] I = new float[16];

            // Did we get valid data?
            if (SensorManager.getRotationMatrix(R, I, mAccelVals, mGravVals) &&
                    R != null) {
//                float[] orientation = new float[3];
//                orientation = SensorManager.getOrientation(R, orientation);
//                if (R.length == 16) {
//                    Log.d(TAG, "WE HAVE A 4x4");
//                }
                BusProvider.getInstance().post(new OrientationChangeEvent(R, 0));
            } else {
                Log.e(TAG, "Didn't et information from rotation matrix");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Currently not used
    }

    /**
     * Registers the sensor's listeners
     */
    public void register() {
        mSensorManager.registerListener(this, mAccelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Unregister the sensor listener when we are destroying the activity
     */
    public void unregister() {
        mSensorManager.unregisterListener(this);
    }
}