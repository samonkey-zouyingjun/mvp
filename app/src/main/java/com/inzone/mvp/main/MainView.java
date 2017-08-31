package com.inzone.mvp.main;

import java.util.List;

/**
 * Created by Administrator on 2017/8/29.
 */

public interface MainView {

    void showProgress();

    void hideProgress();

    void setItems(List<String> items);

    void showMessage(String message);

}
