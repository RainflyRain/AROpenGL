package com.wl.opes;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by fly on 2017/9/19.
 */

public class MyGLRenderer implements GLSurfaceView.Renderer{

    private String vetexShader = "attribute vec4 vPosition;\n" +
            " void main() {\n" +
            "     gl_Position = vPosition;\n" +
            " }";

    private String colorShader = "precision mediump float;\n" +
            " uniform vec4 vColor;\n" +
            " void main() {\n" +
            "     gl_FragColor = vColor;\n" +
            " }";

    float triangleCoords[] = {
            0.5f,  0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f  // bottom right
    };

    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };


    FloatBuffer vetextBuff;

    int program;

    int positionHandle;

    int colorHandle;

    //顶点纬度
    static final int COORDS_PER_VERTEX = 3;
    //一个顶点 字节长度
    private final int vertexStride = COORDS_PER_VERTEX * 4;
    //顶点个数
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        GLES20.glClearColor(255f,255f,255f,1.0f);

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(triangleCoords.length*4);
        byteBuffer.order(ByteOrder.nativeOrder());

        vetextBuff = byteBuffer.asFloatBuffer();
        vetextBuff.put(triangleCoords);
        vetextBuff.position(0);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vetexShader);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,colorShader);

        program = GLES20.glCreateProgram();

        GLES20.glAttachShader(program,vertexShader);
        GLES20.glAttachShader(program,fragmentShader);
        GLES20.glLinkProgram(program);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0,0,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(program);

        positionHandle = GLES20.glGetAttribLocation(program,"vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle,COORDS_PER_VERTEX,GLES20.GL_FLOAT,false,vertexStride,vetextBuff);

        colorHandle = GLES20.glGetUniformLocation(program,"vColor");
        GLES20.glUniform4fv(colorHandle,1,color,0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertexCount);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }

    public static int loadShader(int type,String shaderCode){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader,shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
