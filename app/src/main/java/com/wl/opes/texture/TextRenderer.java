package com.wl.opes.texture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by fly on 2017/9/21.
 */

public class TextRenderer implements GLSurfaceView.Renderer{

    float ratio;

    TextureImg textureImg;

    Context context;

    Bitmap mBitmap;


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        GLES20.glClearColor(255f,255f,255f,10.0f);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        textureImg = new TextureImg(context);

        try {
            mBitmap = BitmapFactory.decodeStream(context.getResources().getAssets().open("texture/fengj.png"));
            textureImg.setmBitmap(mBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0,0,width,height);
    }


    public void setContext(Context context){
        this.context = context;
    }



    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT| GLES20.GL_DEPTH_BUFFER_BIT);
        textureImg.draw();
    }

    public static int loadShader(int type,String shaderCode){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader,shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

}
