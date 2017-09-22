package com.wl.opes.texture;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wl.opes.R;

public class TextureActivity extends AppCompatActivity {

    private TextureSurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surfaceView = new TextureSurfaceView(this);
        setContentView(surfaceView);
    }

}
