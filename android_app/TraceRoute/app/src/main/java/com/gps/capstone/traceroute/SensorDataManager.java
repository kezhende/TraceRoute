package com.gps.capstone.traceroute;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.squareup.otto.Bus;

/**
 * Created by saryana on 4/11/15.
 */
public class SensorDataManager implements SensorEventListener {

    private final String TAG = this.getClass().getSimpleName();

    // Bus system used for communication
    public Bus mBus;
    // Sensor manager we are using
    private SensorManager mSensorManager;
    // Current acceleration values
    private float[] mAccelVals;
    // Current gravity values
    private float[] mGravVals;

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
     * @param bus Bus for communication
     */
    public SensorDataManager(Context context, Bus bus) {
        mBus = bus;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        // Grab and register listeners for the accelerometer and the gravity
        Sensor accel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor grav = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mSensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, grav, SensorManager.SENSOR_DELAY_NORMAL);
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
            float[] R = null;
            // Inclination of the phone
            float[] I = null;

            // Did we get valid data?
            if (SensorManager.getRotationMatrix(R, I, mAccelVals, mGravVals) &&
                    R != null) {
//                float[] orientation = new float[3];
//                orientation = SensorManager.getOrientation(R, orientation);
                mBus.post(new OrientationChangeEvent(R, 0));
            } else {
                Log.e(TAG, "Didn't et information from rotation matrix");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public void unregister() {
        mSensorManager.unregisterListener(this);
    }
}
