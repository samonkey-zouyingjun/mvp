package com.inzone.mvp.login;

/**
 * Created by Administrator on 2017/8/29.
 */

public interface LoginInteractor {

    interface OnloginFinishedListener{
        void onUsernameError();
        void onPasswordError();
        void onSuccess();
    }

    void login(String username,String password,OnloginFinishedListener listener);

}
