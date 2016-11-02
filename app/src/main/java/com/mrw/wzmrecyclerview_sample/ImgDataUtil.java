package com.mrw.wzmrecyclerview_sample;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/30.
 */
public class ImgDataUtil {

    public static ArrayList<String> getImgDatas() {
        ArrayList<String> imgDatas = new ArrayList<>();
        imgDatas.add("http://seopic.699pic.com/photo/50004/2199.jpg_wh1200.jpg");
        imgDatas.add("http://seopic.699pic.com/photo/50000/2811.jpg_wh1200.jpg");
        imgDatas.add("http://seopic.699pic.com/photo/50007/7034.jpg_wh1200.jpg");
        imgDatas.add("http://seopic.699pic.com/photo/50006/0945.jpg_wh1200.jpg");
        imgDatas.add("http://seopic.699pic.com/photo/00040/2066.jpg_wh1200.jpg");
        imgDatas.add("http://seopic.699pic.com/photo/00010/8940.jpg_wh1200.jpg");
        imgDatas.add("http://seopic.699pic.com/photo/00041/5575.jpg_wh1200.jpg");
        imgDatas.add("http://seopic.699pic.com/photo/50007/1912.jpg_wh1200.jpg");
        imgDatas.add("http://seopic.699pic.com/photo/50004/2199.jpg_wh1200.jpg");
        imgDatas.add("http://seopic.699pic.com/photo/50000/2811.jpg_wh1200.jpg");
        return imgDatas;
    }

    public static void loadImage(Context context,String url, final ImageView imageView) {
        Glide.with(context).load(url).placeholder(R.color.gray).dontAnimate().dontTransform().into(imageView);
    }

}
