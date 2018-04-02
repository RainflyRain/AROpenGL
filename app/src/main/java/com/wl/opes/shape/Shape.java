package com.wl.opes.shape;

/**
 * Created by fly on 2017/9/21.
 */

public abstract class Shape {

    public void setMvpMatrix(float[] mvpMatrix) {
        this.mvpMatrix = mvpMatrix;
    }

    float[] mvpMatrix;

    public float[] getMvpMatrix() {
        return mvpMatrix;
    }

    public Shape() {

    }

    public void draw(){

    }

    public abstract void draw(float[] mvpMatrix);

}
