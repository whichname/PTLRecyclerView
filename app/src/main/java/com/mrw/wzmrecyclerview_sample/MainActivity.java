package com.mrw.wzmrecyclerview_sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mrw.wzmrecyclerview.AutoLoad.AutoLoadRecyclerView;
import com.mrw.wzmrecyclerview.Divider.BaseItemDecoration;
import com.mrw.wzmrecyclerview.PullToLoad.OnLoadListener;
import com.mrw.wzmrecyclerview.PullToLoad.PullToLoadRecyclerView;
import com.mrw.wzmrecyclerview.PullToRefresh.OnRefreshListener;
import com.mrw.wzmrecyclerview_sample.R;
import com.mrw.wzmrecyclerview.SimpleAdapter.SimpleAdapter;
import com.mrw.wzmrecyclerview.SimpleAdapter.ViewHolder;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_linear_manager:
                LinearManagerActivity.actionStart(this);
                break;
            case R.id.btn_grid_manager:
                GridManagerActivity.actionStart(this);
                break;
            case R.id.btn_staggred_grid_manager:
                StaggredGridManagerActivity.actionStart(this);
                break;
            case R.id.btn_auto_load:
                AutoLoadActivity.actionStart(this);
                break;
        }
    }




}
