package com.example.ximalaya;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.ximalaya.adapters.PlayTrackPageAdapter;
import com.example.ximalaya.base.BaseActivity;
import com.example.ximalaya.interfaces.IPlayerCallback;
import com.example.ximalaya.presenters.PlayPresenter;
import com.example.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.text.SimpleDateFormat;
import java.util.List;

public class PlayerActivity extends BaseActivity implements IPlayerCallback, ViewPager.OnPageChangeListener {

    private ImageView mControlBtn;
    private PlayPresenter mPlayPresenter;
    private SimpleDateFormat minFormat = new SimpleDateFormat("mm:ss");
    private SimpleDateFormat mHourFormat = new SimpleDateFormat("hh:mm:ss");
    private TextView mTotalDuration;
    private TextView mCurrentPosition;
    private SeekBar mDurationBar;
    private int mCurrentProgress = 0;
    private boolean isUserTouch = false;
    private ImageView mPlayPreBtn;
    private ImageView mPlayNextBtn;
    private TextView mTrackTitleTv;
    private String mTrackTitleText;
    private PlayTrackPageAdapter mPlayTrackPageAdapter;
    private ViewPager mTrackPageView;
    private boolean mIsUserSlidePager = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_sounds);

        mPlayPresenter = PlayPresenter.getPlayPresenter();
        mPlayPresenter.registerViewCallback(this);
        mPlayPresenter.getPlayList();
        initView();
        //界面初始化后，采取获取数据
        mPlayPresenter.getPlayList();
        initEvent();
        startPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        if (mPlayPresenter != null) {
            mPlayPresenter.unRegisterViewCallback(this);
            mPlayPresenter = null;
        }
    }

    /**
     * 开始播放
     */
    private void startPlay() {
        if (mPlayPresenter != null) {
            mPlayPresenter.play();
        }
    }

    /**
     * 给控件设置相关的事件
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initEvent() {
        mControlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果是播放状态就暂停
                if (mPlayPresenter.isPlay()) {
                    mPlayPresenter.pause();
                } else {
                    //如果是非播放状态 就播放
                    mPlayPresenter.play();
                }


            }
        });

        mDurationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mCurrentProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isUserTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //手离开拖动进度条更新进度
                isUserTouch = false;
                mPlayPresenter.seekTo(mCurrentProgress);
            }
        });
        //播放下一個節目
        mPlayNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayPresenter != null) {
                    mPlayPresenter.playNext();
                }
            }
        });

        //播放上一个节目
        mPlayPreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayPresenter != null) {
                    mPlayPresenter.playPre();
                }
            }
        });

        mTrackPageView.addOnPageChangeListener(this);

        mTrackPageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mIsUserSlidePager = true;
                    break;
                }
                return false;
            }
        });
    }

    /**
     * 找到各个控件
     */
    private void initView() {
        mControlBtn = this.findViewById(R.id.btn_play_or_stop);

        mTotalDuration = this.findViewById(R.id.track_duration);

        mCurrentPosition = this.findViewById(R.id.current_position);

        mDurationBar = this.findViewById(R.id.track_seek_bar);

        mPlayNextBtn = this.findViewById(R.id.btn_next);

        mPlayPreBtn = this.findViewById(R.id.btn_pre);

        mTrackTitleTv = this.findViewById(R.id.track_title);
        if (!TextUtils.isEmpty(mTrackTitleText)) {
            mTrackTitleTv.setText(mTrackTitleText);
        }

        //每个播放界面的图片
        mTrackPageView = this.findViewById(R.id.track_page_view);
        //创建图片的适配器
        mPlayTrackPageAdapter = new PlayTrackPageAdapter();
        //设置适配器
        mTrackPageView.setAdapter(mPlayTrackPageAdapter);
    }

    @Override
    public void onPlayStart() {
        //开始播放 修改UI成暂停
        if (mControlBtn != null) {
            mControlBtn.setImageResource(R.mipmap.stop);
        }

    }

    @Override
    public void onPlayPause() {
        if (mControlBtn != null) {
            mControlBtn.setImageResource(R.mipmap.play);
        }
    }

    @Override
    public void onPlayStop() {
        if (mControlBtn != null) {
            mControlBtn.setImageResource(R.mipmap.play);
        }
    }

    @Override
    public void onPlayError() {

    }

    @Override
    public void nextPlay(Track track) {

    }

    @Override
    public void prePlay(Track track) {

    }

    @Override
    public void onListLoaded(List<Track> list) {
        LogUtil.d("PlayerActivity","list ----->" + list);
        if (mPlayTrackPageAdapter != null) {
            mPlayTrackPageAdapter.setData(list);

        }
    }

    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode playMode) {

    }

    @Override
    public void onProgressChange(int currentProgress, int total) {
        mDurationBar.setMax(total);
        String totalDuration;
        String currentPosition;
        //更新进度条
        if (total > 3600000) {
            totalDuration = mHourFormat.format(total);
            currentPosition = mHourFormat.format(currentProgress);

        } else {
            totalDuration = minFormat.format(total);
            currentPosition = minFormat.format(currentProgress);
        }
        if (mTotalDuration != null) {
            mTotalDuration.setText(totalDuration);
        }
        //更新当前时间
        if (mCurrentPosition != null) {
            mCurrentPosition.setText(currentPosition);
        }
        if (!isUserTouch) {
            //更新进度
            mDurationBar.setProgress(currentProgress);
        }


    }

    @Override
    public void onTrackUpdate(Track track, int playIndex) {
        this.mTrackTitleText = track.getTrackTitle();
        if (mTrackTitleTv != null) {
            //设置标题
            mTrackTitleTv.setText(mTrackTitleText);
        }
        //当节目改变获取到播放位置
        if (mTrackPageView != null) {
            mTrackPageView.setCurrentItem(playIndex,true);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels ) {

    }

    @Override
    public void onPageSelected(int position) {
        //当页面选中就去切换播放内容
        if (mPlayPresenter != null && mIsUserSlidePager ) {
            mPlayPresenter.playByIndex(position);
        }
        mIsUserSlidePager = false;
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
