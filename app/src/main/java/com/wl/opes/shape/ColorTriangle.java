package com.wl.opes.shape;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by fly on 2017/9/19.
 */

public class ColorTriangle extends Shape{

    FloatBuffer vetextBuffer;
    FloatBuffer colorBuffer;

    private String vetexShader =
            "attribute vec4 vPosition;\n" +
             "uniform mat4 vMatrix;\n"+
             "varying vec4 vColor;\n"+
             "attribute vec4 aColor;\n"+
            " void main() {\n" +
            "     gl_Position = vMatrix*vPosition;\n" +
            "     vColor = aColor;\n" +
            " }";

    private String colorShader = "precision mediump float;\n" +
            " varying vec4 vColor;\n" +
            " void main() {\n" +
            "     gl_FragColor = vColor;\n" +
            " }";

    float triangleCoords[] = {
            0.5f,  0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f  // bottom right
    };

    float color[] = {
            0.0f, 1.0f, 0.0f, 1.0f ,
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f
    };

    int program;

    //顶点纬度
    static final int COORDS_PER_VERTEX = 3;
    //一个顶点 字节长度
    private final int vertexStride = COORDS_PER_VERTEX * 4;
    //顶点个数
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;

    private int positionHandle;
    private int colorHandle;
    private int mMVPMatrixHandle;

    //构造方法
    public ColorTriangle() {

        //color buffer
        ByteBuffer cBuffer = ByteBuffer.allocateDirect(color.length*4);
        cBuffer.order(ByteOrder.nativeOrder());

        colorBuffer = cBuffer.asFloatBuffer();
        colorBuffer.put(color);
        colorBuffer.position(0);

        //初始化 顶点buffer 并 添加 顶点数据
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(triangleCoords.length*4);
        byteBuffer.order(ByteOrder.nativeOrder());

        vetextBuffer = byteBuffer.asFloatBuffer();
        vetextBuffer.put(triangleCoords);
        vetextBuffer.position(0);

        //加载 顶点着色器 和 片段着色器 并 添加到 GLES中
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,vetexShader);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,colorShader);

        //创建program
        program = GLES20.glCreateProgram();

        GLES20.glAttachShader(program,vertexShader);
        GLES20.glAttachShader(program,fragmentShader);

        //连接program
        GLES20.glLinkProgram(program);
    }

    //绘制函数
    public void draw(float[] mvpMatrix){

        //启用program
        GLES20.glUseProgram(program);

        //获取变换矩阵 句柄，设置变换矩阵的值
        mMVPMatrixHandle = GLES20.glGetUniformLocation(program,"vMatrix");
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle,1,false,mvpMatrix,0);//location count transpose(转置) value offset

        //获取 顶点着色器 句柄，设置 顶点数据
        positionHandle = GLES20.glGetAttribLocation(program,"vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle,COORDS_PER_VERTEX,GLES20.GL_FLOAT,false,vertexStride, vetextBuffer);//index size type normalized stride（偏移量） buffer

        //获取 片段着色器 句柄， 设置颜色数据
        colorHandle = GLES20.glGetAttribLocation(program,"aColor");
        GLES20.glEnableVertexAttribArray(colorHandle);
        GLES20.glVertexAttribPointer(colorHandle,4,GLES20.GL_FLOAT,false,0,colorBuffer);//location count type normalized offset data

        //绘制三角形，关闭 顶点着色器 句柄
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertexCount);
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(colorHandle);
    }

}
