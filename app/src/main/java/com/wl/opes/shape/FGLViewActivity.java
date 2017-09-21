package com.wl.opes.shape;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.wl.opes.R;

public class FGLViewActivity extends AppCompatActivity {

    private MyGLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shape,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.trangle:
                mGLView.setIndex(0);
                break;
            case R.id.colorTriangle:
                mGLView.setIndex(1);
                break;
            case R.id.cicle:
                mGLView.setIndex(2);
                break;
            case R.id.cube:
                mGLView.setIndex(3);
                break;
            case R.id.cone:
                mGLView.setIndex(4);
                break;
            case R.id.cylinder:
                mGLView.setIndex(5);
                break;
            case R.id.ball:
                mGLView.setIndex(6);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
