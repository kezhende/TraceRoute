package com.gps.capstone.traceroute.sensors.listeners;

import android.content.Context;
import android.hardware.SensorManager;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationManagerCompat;

import com.gps.capstone.traceroute.Utils.BusProvider;
import com.squareup.otto.Bus;

/**
 * Created by saryana on 4/18/15.
 */
public abstract class MySensorListener {
    // Tag for debugging
    private final String TAG = getClass().getSimpleName();

    // Context we are getting called from
    Context mContext;
    // Bus for communication
    Bus mBus;
    // Sensor manager
    SensorManager mSensorManager;
    // Notification for more up to date data
    NotificationManagerCompat mNotificationManager;
    // Notification builder
    Builder mBuilder;
    // Check to see if the device is registered so we don't get double registration or unregistration
    boolean mIsRegistered;

    /**
     * Initialize the listener that we will be using
     * @param context Context we are getting called in
     */
    public MySensorListener(Context context) {
        mContext = context;
        mBus = BusProvider.getInstance();
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mNotificationManager = NotificationManagerCompat.from(mContext);
        mBuilder = new Builder(mContext);
        mIsRegistered = false;
    }

    /**
     * Register the sensors and the bus
     */
    public void register() {
        mIsRegistered = true;
        mBus.register(this);
    }

    /**
     * Unregister sensors and from the bus
     */
    public void unregister() {
        mIsRegistered = false;
        mBus.unregister(this);
    }

    public boolean isRegistered() {
        return mIsRegistered;
    }


}
