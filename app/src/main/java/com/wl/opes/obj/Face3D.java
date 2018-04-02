package com.wl.opes.obj;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

public class Face3D {

    //顶点 和 颜色 数据
    private ArrayList<Float> vertexArray;
    private ArrayList<Float> colorArray;
    private ArrayList<Short> indexArray;

    //顶点buffer
    private FloatBuffer vertexBuffer;
    //颜色buffer
    private FloatBuffer colorBuffer;
    //index buffer
    private ShortBuffer indexBuffer;

    private int verCount;
    private int indexCount;

    public void addVer(Float ver){
        if (vertexArray == null){
            vertexArray = new ArrayList<>();
        }
        vertexArray.add(ver);
        verCount = vertexArray.size()/3;
    }

    public Float getVer(int index){
        return vertexArray.get(index);
    }

    public void addCor(Float cor){
        if (colorArray == null){
            colorArray = new ArrayList<>();
        }
        colorArray.add(cor);
    }

    public void addInd(Short ind){
        if (indexArray == null){
            indexArray = new ArrayList<>();
        }
        indexArray.add((short) (ind-1));
        indexCount = indexArray.size();
    }

    public ShortBuffer getIndexBuffer() {
        int size = indexArray.size();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(size*4);
        byteBuffer.order(ByteOrder.nativeOrder());

        indexBuffer = byteBuffer.asShortBuffer();
        for (int i = 0; i < size; i++) {
            indexBuffer.put(indexArray.get(i));
        }
        indexBuffer.position(0);
        return indexBuffer;
    }

    public FloatBuffer getVerBuffer(){
        int size = vertexArray.size();
        //初始化 顶点buffer 并 添加 顶点数据
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(size*4);
        byteBuffer.order(ByteOrder.nativeOrder());

        vertexBuffer = byteBuffer.asFloatBuffer();
        for (int i = 0; i < size; i++) {
            vertexBuffer.put(vertexArray.get(i));
        }
        vertexBuffer.position(0);
        return vertexBuffer;
    }


    public FloatBuffer getTexBuffer() {
        int size = colorArray.size();
        //初始化 顶点buffer 并 添加 顶点数据
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(size*4);
        byteBuffer.order(ByteOrder.nativeOrder());

        colorBuffer = byteBuffer.asFloatBuffer();
        for (int i = 0; i < size; i++) {
            colorBuffer.put(colorArray.get(i));
        }
        colorBuffer.position(0);
        return colorBuffer;
    }

    public int getVerCount() {
        return verCount;
    }

    public int getIndexCount() {
        return indexCount;
    }
}
