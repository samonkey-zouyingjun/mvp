package com.inzone.mvp.login;

/**
 * Created by Administrator on 2017/8/29.
 */

public interface LoginView {

    void showProgress();

    void hideProgress();

    void setUsernameError();

    void setPasswordError();

    void navigateToHome();

}
