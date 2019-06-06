package com.example.ximalaya;

import android.accounts.NetworkErrorException;
import android.content.Intent;
import android.graphics.Color;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.ximalaya.adapters.DetailListAdapter;
import com.example.ximalaya.base.BaseActivity;
import com.example.ximalaya.interfaces.IAlbumDetailViewCallback;
import com.example.ximalaya.presenters.AlbumDetailPresenter;
import com.example.ximalaya.presenters.PlayPresenter;
import com.example.ximalaya.utils.LogUtil;
import com.example.ximalaya.views.RoundRectImageView;
import com.example.ximalaya.views.UILoader;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

public class DetailActivity  extends BaseActivity  implements IAlbumDetailViewCallback, UILoader.OnRetryClickListener, DetailListAdapter.ItemClickListener {

    private ImageView mMLargeCover;
    private RoundRectImageView mMSmallCover;
    private TextView mMAlbumTitle;
    private TextView mMAlbumAuthor;
    private AlbumDetailPresenter mAlbumDetailPresenter;
    private int mCurrentPage = 1 ;
    private RecyclerView mDetailList;
    private DetailListAdapter mDetailListAdapter;
    private String TAG = "DetailActivity";
    private FrameLayout mDetailListContainer;
    private UILoader mUiLoader;
    private  long mCurrentId = -1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        initView();
        mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
        mAlbumDetailPresenter.registerViewCallback(this);

    }

    private void initView() {
        mDetailListContainer = this.findViewById(R.id.detail_list_container);
        //
        if (mUiLoader == null) {
            mUiLoader = new UILoader(this) {

                @Override
                public View getSuccessView(ViewGroup container) {
                    return createSuccessView(container);
                }
            };
            mDetailListContainer.removeAllViews();
            mDetailListContainer.addView(mUiLoader);
            mUiLoader.setOnRetryClickListener(DetailActivity.this);
        }

        mMLargeCover = this.findViewById(R.id.iv_large_cover);
        mMSmallCover = this.findViewById(R.id.viv_small_cover);
        mMAlbumTitle = this.findViewById(R.id.tv_album_title);
        mMAlbumAuthor = this.findViewById(R.id.tv_album_author);



    }

    private View createSuccessView(ViewGroup container) {
        View detailListView = LayoutInflater.from(this).inflate(R.layout.item_detail_list, container, false);
        mDetailList = detailListView.findViewById(R.id.album_detail_list);
        //使用RecycleView
        //1.设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mDetailList.setLayoutManager(layoutManager);
        //2.设置适配器
        mDetailListAdapter = new DetailListAdapter();
        mDetailList.setAdapter(mDetailListAdapter);
        //设置上下边距
        mDetailList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(),2);
                outRect.bottom = UIUtil.dip2px(view.getContext(),2);
                outRect.left = UIUtil.dip2px(view.getContext(),2);
                outRect.right = UIUtil.dip2px(view.getContext(),2);
            }
        });
        mDetailListAdapter.setItemClickListener(this);
        return detailListView;
    }


    @Override
    public void onDetailListLoaded(List<Track> tracks) {
        //判断数据结果，根据结果控制UI显示
        if(tracks == null || tracks.size()==0){
            if (mUiLoader != null) {
                mUiLoader.updateStatus(UILoader.UIStatus.EMPTY);
            }
        }


        if (mUiLoader != null) {
            mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);
        }

        //更新UI
        mDetailListAdapter.setData(tracks);
    }

    @Override
    public void onNetWorkError(int errorCode, String errorMsg) {
        mUiLoader.updateStatus(UILoader.UIStatus.NETWORK_ERROE);
    }

    @Override
    public void onAlbumLoaded(Album album) {

        //获取专辑的内容的列表

        long id = album.getId();
        mCurrentId = id;
        if (mAlbumDetailPresenter != null) {
            mAlbumDetailPresenter.getAlbumDetail((int)id, mCurrentPage);
        }
        //加载标题
        //拿到数据 显示Loading
        if (mUiLoader != null) {
            mUiLoader.updateStatus(UILoader.UIStatus.LOADING);
        }
        if (mMAlbumTitle != null) {
            mMAlbumTitle.setText(album.getAlbumTitle());
        }
        //加载作者
        if (mMAlbumAuthor != null) {
            mMAlbumAuthor.setText(album.getAnnouncer().getNickname());
        }
        if (mMLargeCover != null) {
            Picasso.with(this).load(album.getCoverUrlLarge()).into(mMLargeCover);
        }

        if (mMSmallCover != null) {
            Picasso.with(this).load(album.getCoverUrlSmall()).into(mMSmallCover);
        }
    }

    @Override
    public void OnRetryClick() {
        //用户网络不佳时重新加载
        if (mAlbumDetailPresenter != null) {
            mAlbumDetailPresenter.getAlbumDetail((int)mCurrentId, mCurrentPage);
        }
    }


    @Override
    public void onItemClick(List<Track> mdetailData, int position) {
        //设置播放器的数据
        PlayPresenter playPresenter = PlayPresenter.getPlayPresenter();
        playPresenter.setPlayList(mdetailData,position);

        //跳转到播放器界面
        Intent intent = new Intent(this,PlayerActivity.class);
        startActivity(intent);
    }
}
