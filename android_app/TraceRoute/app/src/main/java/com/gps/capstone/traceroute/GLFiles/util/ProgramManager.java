package com.gps.capstone.traceroute.GLFiles.util;

import android.opengl.GLES20;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

/**
 * Stores the master OpenGL shader and program code, and allows user to fetch
 * the compiled shaders and the openGL runtime environment
 */
public class ProgramManager {
    // Stores the openGL program
    private Integer mProgram;

    // Stores a shaderID for the compiled vertex shader code.
    private Integer vertexShader;
    // Stores a shaderID for the compiled fragment shader code.
    private Integer fragmentShader;

    // Vertex Shader
    private String vertexShaderCode;

    // Fragment Shader
    private String fragmentShaderCode;

    /**
     * Initializes a program manager.
     */
    public ProgramManager() {
        vertexShaderCode = "";
        fragmentShaderCode = "";
        grabShaders();
    }

    // Load the shaders from disk.
    private void grabShaders() {
        File vertexShader = new File("shaders/vertexShader.c");
        File fragmentShader = new File("shaders/fragmentShader.c");
        if (!vertexShader.exists() || !fragmentShader.exists()) {
            Log.i(getClass().getSimpleName(), "Failed to load shaders!");
        } else {
            // Prepare the files for reading.
            BufferedReader reader1 = null;
            BufferedReader reader2 = null;
            try {
                reader1 = new BufferedReader(new FileReader(vertexShader));
                reader2 = new BufferedReader(new FileReader(fragmentShader));
            } catch (FileNotFoundException e) {}

            try {
                // read in both files.
                String cur = reader1.readLine();
                while (cur != null) {
                    vertexShaderCode += cur;
                    cur = reader1.readLine();
                }
                cur = reader2.readLine();
                while (cur != null) {
                    fragmentShaderCode += cur;
                    cur = reader2.readLine();
                }
            } catch (IOException e) {
                Log.i(getClass().getSimpleName(), "File Read Failure!");
            }
            

        }
    }

    /**
     *
     * @return A handle for the vertex shader used in the openGL program.
     */
    public int getVertexShader() {
        if (vertexShader == null) {
            vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
                    vertexShaderCode);
        }
        return vertexShader;
    }

    /**
     *
     * @return A handle for the fragment shader used in the openGL program.
     */
    public int getFragmentShader() {
        if (fragmentShader == null) {
            fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
                    fragmentShaderCode);
        }
        return fragmentShader;
    }

    // Compiles the shader (stored in shaderCode) of the given type.
    private int loadShader(int type, String shaderCode){
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
     *
     * @return A handle to the openGL program.
     */
    public int getProgram() {
        if (mProgram == null) {
            // create empty OpenGL ES Program
            mProgram = GLES20.glCreateProgram();

            // add the vertex shader to program
            GLES20.glAttachShader(mProgram, getVertexShader());

            // add the fragment shader to program
            GLES20.glAttachShader(mProgram, getFragmentShader());

            // Bind attributes
            GLES20.glBindAttribLocation(mProgram, 0, "a_Position");
            GLES20.glBindAttribLocation(mProgram, 1, "a_Color");

            // Links the shaders together.
            GLES20.glLinkProgram(mProgram);
        }
        return mProgram;
    }
}