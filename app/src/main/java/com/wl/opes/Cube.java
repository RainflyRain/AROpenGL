package com.wl.opes;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

/**
 * Created by fly on 2017/9/19.
 */

public class Cube {

    FloatBuffer vetextBuffer;
    FloatBuffer colorBuffer;
    ShortBuffer indexBuffer;

    private String vetexShader =
            "attribute vec4 vPosition;\n" +
                    "uniform mat4 vMatrix;\n" +
                    "varying  vec4 vColor;\n" +
                    "attribute vec4 aColor;\n" +
                    "void main() {\n" +
                    "  gl_Position = vMatrix*vPosition;\n" +
                    "  vColor=aColor;\n"+"}";

    private String colorShader =
            "precision mediump float;\n" +
                    "varying vec4 vColor;\n" +
                    "void main() {\n" +
                    "  gl_FragColor =vColor;\n" +
                    "}";

    final float triangleCoords[] = {
            -1.0f,1.0f,1.0f,    //正面左上0
            -1.0f,-1.0f,1.0f,   //正面左下1
            1.0f,-1.0f,1.0f,    //正面右下2
            1.0f,1.0f,1.0f,     //正面右上3
            -1.0f,1.0f,-1.0f,    //反面左上4
            -1.0f,-1.0f,-1.0f,   //反面左下5
            1.0f,-1.0f,-1.0f,    //反面右下6
            1.0f,1.0f,-1.0f,     //反面右上7
    };

    final short index[]={
            0,3,2,0,2,1,    //正面
            0,1,5,0,5,4,    //左面
            0,7,3,0,4,7,    //上面
            6,7,4,6,4,5,    //后面
            6,3,7,6,2,3,    //右面
            6,5,1,6,1,2     //下面
    };

    //八个顶点的颜色，与顶点坐标一一对应
    float color[] = {
            0f,1f,0f,1f,
            0f,1f,0f,1f,
            0f,1f,0f,1f,
            0f,1f,0f,1f,
            1f,0f,0f,1f,
            1f,0f,0f,1f,
            1f,0f,0f,1f,
            1f,0f,0f,1f,
    };


    int program;

    private int positionHandle;
    private int colorHandle;
    private int mMVPMatrixHandle;

    public Cube() {

        //index buffer
        ByteBuffer iBuffer = ByteBuffer.allocateDirect(index.length*2);
        iBuffer.order(ByteOrder.nativeOrder());

        indexBuffer = iBuffer.asShortBuffer();
        indexBuffer.put(index);
        indexBuffer.position(0);

        //color buffer
        ByteBuffer cBuffer = ByteBuffer.allocateDirect(color.length*4);
        cBuffer.order(ByteOrder.nativeOrder());

        colorBuffer = cBuffer.asFloatBuffer();
        colorBuffer.put(color);
        colorBuffer.position(0);

        //vertex buffer
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(triangleCoords.length*4);
        byteBuffer.order(ByteOrder.nativeOrder());

        vetextBuffer = byteBuffer.asFloatBuffer();
        vetextBuffer.put(triangleCoords);
        vetextBuffer.position(0);

        //load vetex and fragment shader
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,vetexShader);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,colorShader);

        program = GLES20.glCreateProgram();

        GLES20.glAttachShader(program,vertexShader);
        GLES20.glAttachShader(program,fragmentShader);

        //link program
        GLES20.glLinkProgram(program);
    }

    public void draw(float[] mvpMatrix){

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT| GLES20.GL_DEPTH_BUFFER_BIT);

        //启用program
        GLES20.glUseProgram(program);


        Matrix.rotateM(mvpMatrix,0,45,1,1,1);

        Matrix.scaleM(mvpMatrix,0,0.5f,0.5f,0.5f);

        Matrix.rotateM(mvpMatrix,0,1f,1f,0,0);

        //获取变换矩阵 句柄，设置变换矩阵的值
        mMVPMatrixHandle = GLES20.glGetUniformLocation(program,"vMatrix");
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle,1,false,mvpMatrix,0);//location count transpose(转置) value offset

        //获取 顶点着色器 句柄，设置 顶点数据
        positionHandle = GLES20.glGetAttribLocation(program,"vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle,3,GLES20.GL_FLOAT,false,0, vetextBuffer);//index size type normalized stride buffer

        //获取 片段着色器 句柄， 设置颜色数据
        colorHandle = GLES20.glGetAttribLocation(program,"aColor");
        GLES20.glEnableVertexAttribArray(colorHandle);//location count data offset
        GLES20.glVertexAttribPointer(colorHandle,4,GLES20.GL_FLOAT,false,0,colorBuffer);

        //绘制三角形，关闭 顶点着色器 句柄
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,index.length, GLES20.GL_UNSIGNED_SHORT,indexBuffer);

        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(colorHandle);
    }

}
