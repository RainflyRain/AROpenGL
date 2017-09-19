package com.wl.opes;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by fly on 2017/9/19.
 */

public class MyGLSurfaceView extends GLSurfaceView{

    private MyGLRenderer myGLRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        myGLRenderer = new MyGLRenderer();
        setRenderer(myGLRenderer);
    }
}
