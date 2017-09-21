package com.wl.opes.shape;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * Created by fly on 2017/9/19.
 */

public class Circle extends Shape {

    FloatBuffer vetextBuffer;

    private String vetexShader =
            "attribute vec4 vPosition;\n" +
             "uniform mat4 vMatrix;\n"+
            " void main() {\n" +
            "     gl_Position = vMatrix*vPosition;\n" +
            " }";

    private String colorShader = "precision mediump float;\n" +
            " uniform vec4 vColor;\n" +
            " void main() {\n" +
            "     gl_FragColor = vColor;\n" +
            " }";

    float triangleCoords[] = createPositions(50,0.5f);

    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

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

    public Circle() {

        //初始化 顶点buffer 并 添加 顶点数据
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(triangleCoords.length*4);
        byteBuffer.order(ByteOrder.nativeOrder());

        vetextBuffer = byteBuffer.asFloatBuffer();
        vetextBuffer.put(triangleCoords);
        vetextBuffer.position(0);

        //加载 顶点着色器 和 片段着色器 并 添加到 GLES中
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,vetexShader);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,colorShader);

        program = GLES20.glCreateProgram();

        GLES20.glAttachShader(program,vertexShader);
        GLES20.glAttachShader(program,fragmentShader);

        //连接program
        GLES20.glLinkProgram(program);
    }

    @Override
    public void draw(float[] mvpMatrix){

        //启用program
        GLES20.glUseProgram(program);

        //获取变换矩阵 句柄，设置变换矩阵的值
        mMVPMatrixHandle = GLES20.glGetUniformLocation(program,"vMatrix");
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle,1,false,mvpMatrix,0);//location count transpose(转置) value offset

        //获取 顶点着色器 句柄，设置 顶点数据
        positionHandle = GLES20.glGetAttribLocation(program,"vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle,COORDS_PER_VERTEX,GLES20.GL_FLOAT,false,vertexStride, vetextBuffer);//index size type normalized stride buffer

        //获取 片段着色器 句柄， 设置颜色数据
        colorHandle = GLES20.glGetUniformLocation(program,"vColor");
        GLES20.glUniform4fv(colorHandle,1,color,0);//location count data offset

        //绘制三角形，关闭 顶点着色器 句柄
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,triangleCoords.length/3);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }

    private float[]  createPositions(int n,float radius){
        ArrayList<Float> data=new ArrayList<>();
        data.add(0.0f);             //设置圆心坐标
        data.add(0.0f);
        data.add(0.0f);
        float angDegSpan=360f/n;
        for(float i=0;i<360+angDegSpan;i+=angDegSpan){
            data.add((float) (radius*Math.sin(i*Math.PI/180f)));
            data.add((float)(radius*Math.cos(i*Math.PI/180f)));
            data.add(0.0f);
        }
        float[] f=new float[data.size()];
        for (int i=0;i<f.length;i++){
            f[i]=data.get(i);
        }
        return f;
    }

}
