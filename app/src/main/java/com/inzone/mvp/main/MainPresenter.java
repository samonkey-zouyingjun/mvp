package com.inzone.mvp.main;

/**
 * Created by Administrator on 2017/8/29.
 */

public interface MainPresenter {

    void onResume();

    void onItemClicked(int position);

    void onDestroy();
}
