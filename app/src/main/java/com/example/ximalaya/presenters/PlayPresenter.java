package com.example.ximalaya.presenters;

import com.example.ximalaya.base.BaseApplication;
import com.example.ximalaya.interfaces.IPlayerCallback;
import com.example.ximalaya.interfaces.IPlayerPresenter;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.List;

public class PlayPresenter implements IPlayerPresenter, IXmPlayerStatusListener {

    private List<IPlayerCallback> mIPlayerCallbacks = new ArrayList<>();


    private final XmPlayerManager mPlayerManager;
    private Track mCurrentTrack;
    private int mCurrentIndex = 0;

    private  PlayPresenter(){
        mPlayerManager = XmPlayerManager.getInstance(BaseApplication.getAppContext());

        //注册播放器状态相关的接口
        mPlayerManager.addPlayerStatusListener(this);

    }

    private static PlayPresenter sPlayPresenter;

    public static PlayPresenter getPlayPresenter() {
        if (sPlayPresenter == null) {
            synchronized (PlayPresenter.class) {
                if (sPlayPresenter == null) {
                    sPlayPresenter = new PlayPresenter();
                }
            }
        }
        return sPlayPresenter;
    }

    private boolean isPlayListSet = false;

    public void setPlayList(List<Track> list, int playIndex) {
        if (mPlayerManager != null) {
            mPlayerManager.setPlayList(list,playIndex);
            isPlayListSet = true;
            mCurrentTrack = list.get(playIndex);
            mCurrentIndex = playIndex;
        } else  {

        }

    }

    @Override
    public void play() {
        if (isPlayListSet) {
            mPlayerManager.play();
        }
    }

    @Override
    public void pause() {
        if (mPlayerManager != null) {
            mPlayerManager.pause();
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void playPre() {
        if (mPlayerManager != null) {
            mPlayerManager.playPre();
        }
    }

    @Override
    public void playNext() {
        if (mPlayerManager != null) {
            mPlayerManager.playNext();
        }
    }

    @Override
    public void switchPlayMode(XmPlayListControl.PlayMode mode) {

    }

    @Override
    public void getPlayList() {
        if (mPlayerManager != null) {
            List<Track> playList = mPlayerManager.getPlayList();
            for(IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
                iPlayerCallback.onListLoaded(playList);
            }
        }

    }

    @Override
    public void playByIndex(int index) {
        //切换播放器到第index的位置进行播放
        if (mPlayerManager != null) {
            mPlayerManager.play(index);
        }
    }

    @Override
    public void seekTo(int progress) {
        //更新播放器进度
        mPlayerManager.seekTo(progress);
    }

    @Override
    public boolean isPlay() {
        //返回是否在播放
        return mPlayerManager.isPlaying();

    }

    @Override
    public void registerViewCallback(IPlayerCallback iPlayerCallback) {
        iPlayerCallback.onTrackUpdate(mCurrentTrack,mCurrentIndex);
        if (!mIPlayerCallbacks.contains((iPlayerCallback))) {
            mIPlayerCallbacks.add(iPlayerCallback);
        }
    }

    @Override
    public void unRegisterViewCallback(IPlayerCallback iPlayerCallback) {
        mIPlayerCallbacks.remove(iPlayerCallback);
    }


    //===============================播放器相关的回调方法======================================//
    @Override
    public void onPlayStart() {
        for(IPlayerCallback iPlayerCallback :mIPlayerCallbacks) {
            iPlayerCallback.onPlayStart();
        }
    }

    @Override
    public void onPlayPause() {
        for(IPlayerCallback iPlayerCallback :mIPlayerCallbacks) {
            iPlayerCallback.onPlayPause();
        }
    }

    @Override
    public void onPlayStop() {
        for(IPlayerCallback iPlayerCallback :mIPlayerCallbacks) {
            iPlayerCallback.onPlayStop();
        }
    }

    @Override
    public void onSoundPlayComplete() {

    }

    @Override
    public void onSoundPrepared() {

    }

    @Override
    public void onSoundSwitch(PlayableModel lastModel, PlayableModel curModel) {
        mCurrentIndex = mPlayerManager.getCurrentIndex();

        if(curModel instanceof Track) {
            Track currentTrack = (Track) curModel;
            mCurrentTrack = currentTrack;
            for(IPlayerCallback iPlayerCallback : mIPlayerCallbacks){
                iPlayerCallback.onTrackUpdate(mCurrentTrack,mCurrentIndex);
            }
        }
        //当前节目改变后，要修改页面的图片

    }

    @Override
    public void onBufferingStart() {

    }

    @Override
    public void onBufferingStop() {

    }

    @Override
    public void onBufferProgress(int progress) {

    }

    @Override
    public void onPlayProgress(int currPos, int duration) {
        for(IPlayerCallback iPlayerCallback:mIPlayerCallbacks) {
            iPlayerCallback.onProgressChange(currPos,duration);
        }
    }

    @Override
    public boolean onError(XmPlayerException e) {
        return false;
    }
    //===============================播放器相关的回调方法======================================//
}
