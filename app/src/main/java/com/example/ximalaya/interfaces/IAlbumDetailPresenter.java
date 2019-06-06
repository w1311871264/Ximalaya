package com.example.ximalaya.interfaces;

import com.example.ximalaya.base.IBasePresenter;

public interface IAlbumDetailPresenter extends IBasePresenter<IAlbumDetailViewCallback> {

    /**
     * 下拉刷新内容
     */

    void pullRefreshMore();

    /**
     * 上接加载更多
     */

    void loadMore();

    /**
     * 获取专辑详情
     * @param albumId
     * @param page
     */
    void getAlbumDetail(int albumId, int page);



}
