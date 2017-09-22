package com.wl.opes.shape;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.wl.opes.shape.Ball;
import com.wl.opes.shape.Circle;
import com.wl.opes.shape.ColorTriangle;
import com.wl.opes.shape.Cone;
import com.wl.opes.shape.Cube;
import com.wl.opes.shape.Cylinder;
import com.wl.opes.shape.Shape;
import com.wl.opes.shape.Triangle;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by fly on 2017/9/19.
 */

public class MyGLRenderer implements GLSurfaceView.Renderer{



    List<Shape> list;
    private  int index = 0;
    static float ratio;
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        GLES20.glClearColor(255f,255f,255f,10.0f);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        list = new ArrayList<>();
        list.add(new Triangle());
        list.add(new ColorTriangle());
        list.add(new Circle());
        list.add(new Cube());
        list.add(new Cone());
        list.add(new Cylinder());
        list.add(new Ball());
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0,0,width,height);

        ratio = (float) width / height;

        initMatrix();
    }

    private void initMatrix() {
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7.0f, 0.0f, 0f, 0.0f, 0.0f, 1.0f, 0.0f);

        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT| GLES20.GL_DEPTH_BUFFER_BIT);
        list.get(index).draw(mMVPMatrix);
    }

    public static int loadShader(int type,String shaderCode){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader,shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    public void setIndex(int index) {
        initMatrix();
        Matrix.scaleM(mMVPMatrix,0,0.5f,0.5f,0.5f);
        this.index = index;
    }
}
