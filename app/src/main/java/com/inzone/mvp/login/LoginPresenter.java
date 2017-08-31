package com.inzone.mvp.login;

/**
 * Created by Administrator on 2017/8/29.
 */

public interface LoginPresenter {

    void validateCredentials(String username,String password);

    void onDestroy();
}
