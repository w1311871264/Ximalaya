package com.example.ximalaya.interfaces;

import com.example.ximalaya.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

public interface IPlayerPresenter extends IBasePresenter<IPlayerCallback> {
    /**
     * 播放
     */
    void play();

    /**
     * 暂停
     */
    void pause();

    /**
     * 停止
     */

    void stop();

    /**
     * 上一首
     */

    void playPre();

    /**
     * 下一首
     */

    void playNext();

    /**
     * 播放模式
     * @param mode
     */

    void switchPlayMode(XmPlayListControl.PlayMode mode);

    /**
     * 获取播放列表
     */
    void getPlayList();

    /**
     * 根据节目位置进行播放
     */
    void playByIndex(int index);

    /**
     * 切换播放进度
     * @param progress
     */
    void seekTo(int progress);

    /**
     * 判断是否在播放
     * @return
     */
    boolean isPlay();



}
