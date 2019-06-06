package com.example.ximalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

public interface IPlayerCallback {
    /**
     * 开始播放
     */
    void onPlayStart();

    /**
     * 暂停播放
     */
    void onPlayPause();

    /**
     * 播放停止
     */
    void onPlayStop();

    /**
     * 播放错误
     */
    void onPlayError();

    /**
     * 下一首
     */
    void nextPlay(Track track);

    /**
     * 上一首
     */
    void prePlay(Track track);

    /**
     * 播放数据列表加载完成
     * @param list 获取播放列表
     */
    void onListLoaded(List<Track> list);

    /**
     * 播放器播放模式改变
     * @param playMode
     */
    void onPlayModeChange(XmPlayListControl.PlayMode playMode);

    /**
     * 进度条改变
     * @param currentProgress
     * @param total
     */
    void onProgressChange(int currentProgress, int total);

    /**
     * 更新节目
     * @param track
     */
    void onTrackUpdate(Track track, int playIndex);
}
