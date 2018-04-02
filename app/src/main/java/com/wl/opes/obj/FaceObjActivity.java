package com.wl.opes.obj;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.wl.opes.R;
import com.wl.opes.Utils;
import com.wl.opes.shape.Face;
import com.wl.opes.shape.TestFace;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class FaceObjActivity extends AppCompatActivity implements View.OnTouchListener {

    private GLSurfaceView surfaceView;
    private LinearLayout constraintLayout;

    private Face face;

    private TestFace testFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_obj);
        constraintLayout = (LinearLayout) findViewById(R.id.ll_container);
        constraintLayout.setOnTouchListener(this);
        surfaceView = (GLSurfaceView) findViewById(R.id.face_obj_view);
        surfaceView.setEGLContextClientVersion(2);


//        testFace = new TestFace(getResources());
//        初始化数据
        face = new Face(getResources());
        InputStream faceinput;
        try {
            faceinput = getAssets().open("asd.obj");
            Face3D face3D = ObjReader.readFace(faceinput);
            face.setFace3D(face3D);
        } catch (IOException e) {
            e.printStackTrace();
        }

        surfaceView.setRenderer(new GLSurfaceView.Renderer() {
            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                face.create();
//                testFace.create();
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
                float[] matrix= Utils.getOriginalMatrix();
                Matrix.translateM(matrix,0,-1.8f,-0.4f,-1);
                Matrix.scaleM(matrix,0,0.02f,0.02f*width/height,0.02f);
                face.setMvpMatrix(matrix);
                GLES20.glViewport(0,0,width,height);
            }

            @Override
            public void onDrawFrame(GL10 gl) {
//                GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
                GLES20.glEnable(GLES20.GL_DEPTH_TEST);
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
//                Matrix.rotateM(face.getMvpMatrix(),0,0.3f,0,1,0);
                face.draw();
//                testFace.draw();
            }
        });
    }


    float disX = 0;
    float disY = 0;
    float startX = 0;
    float startY = 0;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                disX = event.getX() - startX;
                disY = startY - event.getY();
                break;
            case MotionEvent.ACTION_UP:
                Log.i("@fei", "onTouch  移动了: "+disX/80+"=="+disY/80);
                Matrix.translateM(face.getMvpMatrix(),0,disX/80,disY/80,0.01f);
                disX = 0;
                disY = 0;
                startX = 0;
                startY = 0;
                break;
        }
        return true;
    }
}
