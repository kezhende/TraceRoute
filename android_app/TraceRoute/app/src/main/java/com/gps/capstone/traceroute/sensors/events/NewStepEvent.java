package com.gps.capstone.traceroute.sensors.events;

/**
 * Created by saryana on 4/29/15.
 */
public class NewStepEvent {
    public float[] oldFace;
    public float[] newFace;

    public NewStepEvent(float[] oldFace, float[] newFace) {
        this.oldFace = oldFace;
        this.newFace = newFace;
    }
}