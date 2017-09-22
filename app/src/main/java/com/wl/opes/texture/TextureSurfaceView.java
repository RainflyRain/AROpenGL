package com.wl.opes.texture;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by fly on 2017/9/21.
 */

public class TextureSurfaceView extends GLSurfaceView{

    TextRenderer textRenderer;

    public TextureSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        textRenderer = new TextRenderer();
        textRenderer.setContext(context);
        setRenderer(textRenderer);
    }

}
