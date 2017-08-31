package com.inzone.mvp.main;

import java.util.List;

/**
 * Created by Administrator on 2017/8/29.
 */

public interface FindItemsInteractor {
    interface OnFinishedListener{
        void onFinished(List<String> items);
    }
    void findItems(OnFinishedListener onFinishedListener);

}
