package com.wl.opes;

import android.content.res.Resources;

import java.io.InputStream;

/**
 * Created by fly on 2017/9/21.
 */

public class Utils {

    public static String loadFromAssetsFile(String fname, Resources res){
        StringBuilder result=new StringBuilder();
        try{
            InputStream is=res.getAssets().open(fname);
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
