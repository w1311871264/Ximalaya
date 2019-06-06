package com.example.ximalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public interface IAlbumDetailViewCallback {

    /**
     * 加载详细界面
     * @param tracks
     */
    void onDetailListLoaded(List<Track> tracks);

    /**
     * 网络错误
     *
     */

    void onNetWorkError(int errorCode, String errorMsg );
    /**
     * 把Album传给ui
     * @param album
     */

    void onAlbumLoaded(Album album);
}
