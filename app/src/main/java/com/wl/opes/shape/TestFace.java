package com.wl.opes.shape;

import android.content.res.Resources;
import android.opengl.GLES20;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static com.wl.opes.obj.AFilter.glError;
import static com.wl.opes.obj.AFilter.uLoadShader;

public class TestFace {

    FloatBuffer vetextBuffer;
    FloatBuffer colorBuffer;

    private int mProgram;
    private int mHPosition;
    private int mHColor;

    float triangleCoords[] = {
            0.5f,  0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f  // bottom right
    };

    float color[] = {
            0.63671875f, 0.76953125f, 0.22265625f,
            0.63671875f, 0.76953125f, 0.22265625f,
            0.63671875f, 0.76953125f, 0.22265625f,
    };

    private Resources resources;


    public FloatBuffer getVerBuffer(){
        return vetextBuffer;
    }

    public FloatBuffer getColorBuffer() {
        return colorBuffer;
    }

    public TestFace(Resources resources) {
        this.resources = resources;

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

    }

    public void create(){
        createProgram("3dres/obj2.vert","3dres/obj2.frag");
    }


    public void draw() {
        //启用program
        GLES20.glUseProgram(mProgram);

        //设置属性值
        GLES20.glEnableVertexAttribArray(mHPosition);
        GLES20.glVertexAttribPointer(mHPosition,3,GLES20.GL_FLOAT,true,0, vetextBuffer);//index size type normalized stride（偏移量） buffer
        GLES20.glEnableVertexAttribArray(mHColor);
        GLES20.glVertexAttribPointer(mHColor,3,GLES20.GL_FLOAT,false,0, colorBuffer);

        //绘制三角形，关闭 顶点着色器 句柄
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,triangleCoords.length);
//        GLES20.glDrawElements(GLES20.GL_TRIANGLES,face3D.getIndexCount(), GLES20.GL_UNSIGNED_SHORT,face3D.getIndexBuffer());
        GLES20.glDisableVertexAttribArray(mHPosition);
        GLES20.glDisableVertexAttribArray(mHColor);
    }

    protected final void createProgram(String vertex, String fragment){
        //创建program
        mProgram= uCreateGlProgram(uRes(resources,vertex),uRes(resources,fragment));
        //绑定句柄
        mHPosition= GLES20.glGetAttribLocation(mProgram, "vPosition");
        mHColor=GLES20.glGetAttribLocation(mProgram,"vColor");
    }

    //创建GL程序 并连接
    public static int uCreateGlProgram(String vertexSource, String fragmentSource){
        int vertex=uLoadShader(GLES20.GL_VERTEX_SHADER,vertexSource);
        if(vertex==0)return 0;
        int fragment=uLoadShader(GLES20.GL_FRAGMENT_SHADER,fragmentSource);
        if(fragment==0)return 0;
        int program= GLES20.glCreateProgram();
        if(program!=0){
            GLES20.glAttachShader(program,vertex);
            GLES20.glAttachShader(program,fragment);
            GLES20.glLinkProgram(program);
            int[] linkStatus=new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS,linkStatus,0);
            if(linkStatus[0]!= GLES20.GL_TRUE){
                glError(1,"Could not link program:"+ GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program=0;
            }
        }
        return program;
    }

    //通过路径加载Assets中的文本内容
    public static String uRes(Resources mRes, String path){
        StringBuilder result=new StringBuilder();
        try{
            InputStream is=mRes.getAssets().open(path);
            int ch;
            byte[] buffer=new byte[1024];
            while (-1!=(ch=is.read(buffer))){
                result.append(new String(buffer,0,ch));
            }
        }catch (Exception e){
            return null;
        }
        return result.toString().replaceAll("\\r\\n","\n");
    }

}
