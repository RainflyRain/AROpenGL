package com.wl.opes.obj;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wl.opes.R;
import com.wl.opes.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ObjLoadActivity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;
    private List<ObjFilter2> filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obj_load);
        glSurfaceView = (GLSurfaceView) findViewById(R.id.glview_obj);
        glSurfaceView.setEGLContextClientVersion(2);
        List<Obj3D> model = ObjReader.readMultiObj(this,"assets/3dres/pikachu.obj");

        filters = new ArrayList<>();

        for (int i = 0; i < model.size(); i++) {
            ObjFilter2 filter = new ObjFilter2(getResources());
            filter.setObj3D(model.get(i));
            filters.add(filter);
        }

        glSurfaceView.setRenderer(new GLSurfaceView.Renderer() {
            @Override
            public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
                for (ObjFilter2 f:filters){
                    f.onCreate();
                }
            }

            @Override
            public void onSurfaceChanged(GL10 gl10, int width, int height) {

                for (ObjFilter2 f:filters){
                    f.onSizeChanged(width, height);
                    float[] matrix= Utils.getOriginalMatrix();
                    Matrix.translateM(matrix,0,0,-0.3f,0);
                    Matrix.scaleM(matrix,0,0.008f,0.008f*width/height,0.008f);
                    f.setMatrix(matrix);
                }
            }

            @Override
            public void onDrawFrame(GL10 gl10) {
                GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
                for (ObjFilter2 f:filters){
                    Matrix.rotateM(f.getMatrix(),0,0.3f,0,1,0);
                    f.draw();
                }
            }
        });
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

    }


    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }
}
