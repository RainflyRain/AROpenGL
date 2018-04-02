package com.wl.opes.shape;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * Created by fly on 2017/9/19.
 */

public class Ball extends Shape{

    FloatBuffer vetextBuffer;

    private String vetexShader =
            "uniform mat4 vMatrix;\n" +
                    "varying vec4 vColor;\n" +
                    "attribute vec4 vPosition;\n" +
                    "\n" +
                    "void main(){\n" +
                    "    gl_Position=vMatrix*vPosition;\n" +
                    "    float color;\n" +
                    "    if(vPosition.z>0.0){\n" +
                    "        color=vPosition.z;\n" +
                    "    }else{\n" +
                    "        color=-vPosition.z;\n" +
                    "    }\n" +
                    "    vColor=vec4(color,color,color,1.0);\n" +
                    "}";

    private String colorShader = "precision mediump float;\n" +
            " varying vec4 vColor;\n" +
            " void main() {\n" +
            "     gl_FragColor = vColor;\n" +
            " }";

    float triangleCoords[] = createPositions(360,0.5f);

    int program;

    //顶点纬度
    static final int COORDS_PER_VERTEX = 3;
    //一个顶点 字节长度
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    private int positionHandle;
    private int mMVPMatrixHandle;

    public Ball() {

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

    public void draw(float[] mvpMatrix){

        //启用program
        GLES20.glUseProgram(program);

        Matrix.rotateM(mvpMatrix,0,1,1,0,0);

        //获取变换矩阵 句柄，设置变换矩阵的值
        mMVPMatrixHandle = GLES20.glGetUniformLocation(program,"vMatrix");
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle,1,false,mvpMatrix,0);//location count transpose(转置) value offset

        //获取 顶点着色器 句柄，设置 顶点数据
        positionHandle = GLES20.glGetAttribLocation(program,"vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle,COORDS_PER_VERTEX,GLES20.GL_FLOAT,false,vertexStride, vetextBuffer);//index size type normalized stride(步幅) buffer

        //绘制三角形，关闭 顶点着色器 句柄
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,triangleCoords.length/3);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }

    private float[]  createPositions(int n,float radius){
        ArrayList<Float> data=new ArrayList<>();
        float r1,r2;
        float h1,h2;
        float sin,cos;
        float step = 1.0f;
        for(float i=-90;i<90+step;i+=step){
            r1 = (float)Math.cos(i * Math.PI / 180.0);
            r2 = (float)Math.cos((i + step) * Math.PI / 180.0);
            h1 = (float)Math.sin(i * Math.PI / 180.0);
            h2 = (float)Math.sin((i + step) * Math.PI / 180.0);
            // 固定纬度, 360 度旋转遍历一条纬线
            float step2=step*2;
            for (float j = 0.0f; j <360.0f+step; j +=step2 ) {
                cos = (float) Math.cos(j * Math.PI / 180.0);
                sin = -(float) Math.sin(j * Math.PI / 180.0);

                data.add(r2 * cos);
                data.add(h2);
                data.add(r2 * sin);
                data.add(r1 * cos);
                data.add(h1);
                data.add(r1 * sin);
            }
        }
        float[] f=new float[data.size()];
        for(int i=0;i<f.length;i++){
            f[i]=data.get(i);
        }
        return f;
    }

}
