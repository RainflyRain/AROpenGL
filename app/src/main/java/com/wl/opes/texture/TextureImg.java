package com.wl.opes.texture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.wl.opes.Utils;
import com.wl.opes.shape.MyGLRenderer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by fly on 2017/9/20.
 */

public class TextureImg {



    private String vetexShader;

    private String colorShader;

    float triangleCoords[] = {
            -1.0f,1.0f,    //左上角
            -1.0f,-1.0f,   //左下角
            1.0f,1.0f,     //右上角
            1.0f,-1.0f     //右下角
    };

    final float[] sCoord={
            0.0f,0.0f,
            0.0f,1.0f,
            1.0f,0.0f,
            1.0f,1.0f,
    };

    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

    FloatBuffer vetextBuffer;
    FloatBuffer coordBuffer;

    private int positionHandle;
    private int textureHandle;
    private int coordHandle;

    int program;

    public TextureImg(Context context) {

        //vertex buffer
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(triangleCoords.length*4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vetextBuffer = byteBuffer.asFloatBuffer();
        vetextBuffer.put(triangleCoords);
        vetextBuffer.position(0);

        //coordi buffer
        ByteBuffer cbyteBuffer = ByteBuffer.allocateDirect(sCoord.length*4);
        cbyteBuffer.order(ByteOrder.nativeOrder());
        coordBuffer = cbyteBuffer.asFloatBuffer();
        coordBuffer.put(sCoord);
        coordBuffer.position(0);

        //vertex shader \ fragment shader
        vetexShader = Utils.loadFromAssetsFile("texture/texture_vertex.sh",context.getResources());
        colorShader = Utils.loadFromAssetsFile("texture/texture_fragment.sh",context.getResources());
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,vetexShader);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,colorShader);

        program = GLES20.glCreateProgram();

        GLES20.glAttachShader(program,vertexShader);
        GLES20.glAttachShader(program,fragmentShader);

        //连接program
        GLES20.glLinkProgram(program);
    }

    int textureId;

    public void draw(){

        //启用program
        GLES20.glUseProgram(program);

        textureId = createTexture();

        //Vetex
        positionHandle = GLES20.glGetAttribLocation(program,"vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle,2,GLES20.GL_FLOAT,false,0, vetextBuffer);//index size type normalized stride buffer

        //coordinate
        coordHandle = GLES20.glGetAttribLocation(program,"vCoordinate");
        GLES20.glEnableVertexAttribArray(coordHandle);
        GLES20.glVertexAttribPointer(coordHandle,2,GLES20.GL_FLOAT,false,0,coordBuffer);

        //text
        textureHandle = GLES20.glGetUniformLocation(program,"vTexture");
        GLES20.glUniform1i(textureHandle,0);


        //绘制三角形，关闭 顶点着色器 句柄
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }


    Bitmap mBitmap;

    public void setmBitmap(Bitmap bitmap){
        this.mBitmap = bitmap;
    }

    private int createTexture(){
        //纹理ID
        int[] texture=new int[1];
        if(mBitmap!=null&&!mBitmap.isRecycled()){

            //生成纹理：需要的纹理数、存储获取的纹理ID的数组、数组的起始位置
            GLES20.glGenTextures(1,texture,0);

            //绑定纹理:纹理类型、纹理ID
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[0]);

            //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
            //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
            //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
            //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);

            //根据以上指定的参数，将bitmap复制到当前绑定的纹理对象
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
            return texture[0];
        }
        return 0;
    }

}
