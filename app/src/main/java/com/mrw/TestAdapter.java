package com.mrw;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mrw.wzmrecyclerview.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/19.
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestHolder> {

    private ArrayList<String> imgs;
    private Context mContext;

    public TestAdapter(ArrayList<String> imgs, Context mContext) {
        this.imgs = imgs;
        this.mContext = mContext;
    }

    @Override
    public TestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TestHolder testHolder = new TestHolder(LayoutInflater.from(mContext).inflate(R.layout.item_test,parent,false));
        return testHolder;
    }

    @Override
    public void onBindViewHolder(TestHolder holder, int position) {
        Glide.with(mContext).load(imgs.get(position)).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.color.colorPrimary).into(holder.imageView);
//        if (position % 2 == 0 ) {
//            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.imageView.getLayoutParams();
//            layoutParams.height = 100;
//            holder.imageView.setLayoutParams(layoutParams);
//        }
    }

    @Override
    public int getItemCount() {
        return imgs.size();
    }

    static class TestHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public TestHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView;
//            imageView = (ImageView) itemView.findViewById(R.id.iv);
        }
    }

}
