package com.gps.capstone.traceroute.GLFiles;

import android.opengl.GLES20;

import com.gps.capstone.traceroute.GLFiles.GLPrimitives.DrawableObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by keith619 on 4/14/15.
 */
public class Cube extends DrawableObject {



    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float cubeCoords[] = {   // in counterclockwise order:
            -0.3f,  0.3f, 0.3f,  // Front top left
            -0.3f, -0.3f, 0.3f,  // Front bottom left
            0.3f,  0.3f, 0.3f,   // Front top right
            0.3f, -0.3f, 0.3f,   // Front bottom left
            -0.3f,  0.3f, -0.3f, // Back top left
            -0.3f, -0.3f, -0.3f, // Back bottom left
            0.3f, 0.3f, -0.3f, // Back top right
            0.3f, -0.3f, -0.3f  // Back bottom left
    };

    // The vertex draw order for the cube.
    private final short drawOrder[] = {0,1,2, 1,3,2, 2,3,6, 3,7,6, 6,7,5, 7,5,4, 4,5,1, 5,1,0, 4,0,6, 0,2,6, 1,5,7, 5,7,3}; // order to draw vertices

    // Stores the openGL-compliant draw list.
    private final ShortBuffer drawListBuffer;

    //private final int vertexCount = axisLineCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = { 1f, 0.0f, 0.0f, 1.0f };

    /**
     * The constructor for the cube.
     * @param graphicsEnv The graphics environment where the cube will be rendered.
     * @param coords The coordinates that the cube will be rendered with
     * @param preBaked Whether or not we'll used pre-baked coordinates.
     */
    public Cube(ProgramManager graphicsEnv, float[] coords, boolean preBaked) {
        // call the superclass constructor to start things off.
        super(graphicsEnv, (preBaked ? cubeCoords : coords));
        // convert the draw order into an openGL-compatible format.
        drawListBuffer = convertDrawList(drawOrder);

    }

    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(programHandle);

        // Enable a handle to the axis vertices
        GLES20.glEnableVertexAttribArray(mVertexPositionHandle);

        // Prepare the triangle coordinate values
        GLES20.glVertexAttribPointer(mVertexPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, getVertexData());

        // Set color for drawing the axis
        GLES20.glUniform4fv(mVertexColorHandle, 1, color, 0);

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw Cube
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);


        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mVertexPositionHandle);
    }

}