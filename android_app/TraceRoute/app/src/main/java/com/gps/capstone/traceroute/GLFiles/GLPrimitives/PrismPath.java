package com.gps.capstone.traceroute.GLFiles.GLPrimitives;

import android.util.Log;

import com.gps.capstone.traceroute.GLFiles.util.ProgramManager;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by gammoa on 5/2/15.
 */
public class PrismPath {
    // stores the head of the path.
    private float[] previousPoint;

    // The size of the path.
    private static final float SIZE = 0.3f;

    private ArrayList<SmartRectangularPrism> path;

    public PrismPath() {
        previousPoint = new float[3];
        path = new ArrayList<SmartRectangularPrism>();
    }

    /**
     * Add a point to this path, which will
     * be drawn on the next frame.
     * @param coords
     */
    public void addPoint(float[] coords) {
        Log.d("AH", "ADDING POINT " + Arrays.toString(coords));
        SmartRectangularPrism end = new SmartRectangularPrism();
        end.setDimensions(previousPoint, coords);
        path.add(end);
        previousPoint = coords;
    }

    /**
     * Draw the path using the given mvpMatrix.
     * @param mvpMatrix
     */
    public void draw(float[] mvpMatrix) {
        for (int i = 0; i < path.size(); i++) {
            SmartRectangularPrism cur = path.get(i);
            cur.draw(mvpMatrix);
        }
    }
}
