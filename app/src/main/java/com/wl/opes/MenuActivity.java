package com.wl.opes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wl.opes.obj.ObjLoadActivity;
import com.wl.opes.shape.FGLViewActivity;
import com.wl.opes.texture.TextureActivity;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {


    private RecyclerView recyclerView;
    private ArrayList<MenuBean> beanArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        beanArrayList = new ArrayList<>();
        add("绘制形体",FGLViewActivity.class);
        add("图片处理",TextureActivity.class);
//        add("图形变换",FGLViewActivity.class);
//        add("相机",FGLViewActivity.class);
//        add("相机2 动画",FGLViewActivity.class);
//        add("相机3 美颜",FGLViewActivity.class);
//        add("压缩纹理动画",FGLViewActivity.class);
//        add("FBO使用",FGLViewActivity.class);
//        add("EGL后台处理",FGLViewActivity.class);
        add("3D obj模型",ObjLoadActivity.class);
//        add("obj+mtl模型",FGLViewActivity.class);
//        add("VR效果",FGLViewActivity.class);
//        add("颜色混合",FGLViewActivity.class);
        recyclerView.setAdapter(new MenuAdapter());
    }

    private class MenuBean{

        String name;
        Class<?> clazz;

    }

    private void add(String name,Class<?> clazz){
        MenuBean bean=new MenuBean();
        bean.name=name;
        bean.clazz=clazz;
        beanArrayList.add(bean);
    }

    private class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder>{


        @Override
        public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MenuHolder(getLayoutInflater().inflate(R.layout.item_button,parent,false));
        }

        @Override
        public void onBindViewHolder(MenuHolder holder, int position) {
            holder.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return beanArrayList.size();
        }

        class MenuHolder extends RecyclerView.ViewHolder{

            private Button mBtn;

            MenuHolder(View itemView) {
                super(itemView);
                mBtn= (Button)itemView.findViewById(R.id.mBtn);
                mBtn.setOnClickListener(MenuActivity.this);
            }

            public void setPosition(int position){
                MenuBean bean= beanArrayList.get(position);
                mBtn.setText(bean.name);
                mBtn.setTag(position);
            }
        }

    }

    @Override
    public void onClick(View view){
        int position= (int)view.getTag();
        MenuBean bean= beanArrayList.get(position);
        startActivity(new Intent(this,bean.clazz));
    }

}
