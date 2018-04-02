package com.wl.opes.shape;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.wl.opes.obj.Face3D;

import java.io.InputStream;

import static com.wl.opes.obj.AFilter.glError;
import static com.wl.opes.obj.AFilter.uLoadShader;

public class Face extends Shape{

    private Face3D face3D;
    private int mProgram;
    private int mHPosition;
    private int mHColor;
    private int mHMatrix;

    private Resources resources;


    public void setFace3D(Face3D face3D) {
        this.face3D = face3D;
    }

    public Face(Resources resources) {
        this.resources = resources;
    }

    public void create(){
        //创建program程序
        createProgram("3dres/obj2.vert","3dres/obj2.frag");
    }


    @Override
    public void draw() {
        //启用program
        GLES20.glUseProgram(mProgram);

        //设置属性值
        GLES20.glEnableVertexAttribArray(mHPosition);
        GLES20.glVertexAttribPointer(mHPosition,3,GLES20.GL_FLOAT,true,0, face3D.getVerBuffer());//index size type normalized stride（偏移量） buffer

        GLES20.glEnableVertexAttribArray(mHColor);
        GLES20.glVertexAttribPointer(mHColor,3,GLES20.GL_FLOAT,false,0, face3D.getTexBuffer());

        GLES20.glUniformMatrix4fv(mHMatrix,1,false,mvpMatrix,0);//location count transpose(转置) value offset

        //绘制三角形，关闭 顶点着色器 句柄
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,face3D.getVerCount());
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,face3D.getIndexBuffer().capacity(), GLES20.GL_UNSIGNED_SHORT,face3D.getIndexBuffer());
        GLES20.glDisableVertexAttribArray(mHPosition);
        GLES20.glDisableVertexAttribArray(mHColor);
    }

    @Override
    public void draw(float[] mvpMatrix) {

    }


    protected final void createProgram(String vertex,String fragment){
        //创建program
        mProgram= uCreateGlProgram(uRes(resources,vertex),uRes(resources,fragment));
        //绑定句柄
        mHPosition= GLES20.glGetAttribLocation(mProgram, "vPosition");
        mHColor=GLES20.glGetAttribLocation(mProgram,"vColor");
        mHMatrix=GLES20.glGetUniformLocation(mProgram,"vMatrix");
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
