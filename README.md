# mvp
安卓中的Mvp设计模式

>本项目github地址：https://github.com/samonkey-zouyingjun/mvp
转载请注明出处：http://www.jianshu.com/p/9cee97587006

##反思源于工作，却高于工作
---


MVC和MVP的区别大家都懂，简单的用一副图片就可以概括，这种层次的背书应付面试还行，但是不求甚解恐怕永远领会不到精髓，怎没有金刚钻怎揽瓷器活？接下来我通过一些工作中所遇到的问题来谈谈mvp设计模式的前世今生，在文章末尾加上对mvp的案例以及分析，希望对大家有所帮助，本文案例比较适合mvp初学者，附录给出了一些进阶学习的建议。

![](http://upload-images.jianshu.io/upload_images/7365804-2e88574cae17a93d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

###问题一
>随着界面和业务逻辑复杂度不断提升，Activity的代码就会越来越用臃肿，一个复杂一点的Activity常常是几千行代码，维护起来特别乱，这又是为何？

**1.首先我们来分析一下传统的mvc模式：**

 - Modle层：适合做一些业务逻辑处理，比如数据库存取操作，网络操作，复杂的算法等耗时的任务。
 - View层：应用层中处理数据显示的部分，XML布局可以视为V层，显示Model层的数据结果。
 - Controller层：在Android中，Activity处理用户交互问题，因此可以认为Activity是控制器，Activity读取V视图层的数据（eg.读取当前EditText控件的数据），控制用户输入（eg.EditText控件数据的输入），并向Model发送数据请求（eg.点击Button发起网络请求等）。

从上面可以知道MVC在安卓中，Activity并不是一个标准的Controller（处理用户的交互请求和响应），也需要做View层的工作（加载布局和初始化用户界面），这就导致了V层和Controler层的偶和度较高。

**2.再来看看mvp是如何改进这一问题的：**

 - Modle层：和原来一样，适合做一些耗时的业务逻辑处理。
 - View层：明确定义为Activity，负责UI元素的初始化，建立UI元素与Presenter的关联（Listener之类），同时自己也会处理一些简单的逻辑（复杂的逻辑交由 Presenter处理）.
 - Presenter层：负责复杂的逻辑处理，对应各种实现类和回调方法

从MVP模式中我们也可以看到一些**明显**的改变，弱化了Activity的职责，让其变得和轻薄，只负责显示数据、提供友好界面和交互就行；其次是在原来Activity和Modle层中又剥离出了各种接口的实现类，通过回调来传递数据。

**3.所以MVP相比MVC的好处：**业务结构清晰，而且将来更换实现类不用修改业务结构，原因就是presenter就是实现类，把model和View完全解耦。坏处就是：分层多了，逻辑会更绕。



###问题二
>Android应用做单元测试，一般都是部署到虚拟机或者真机上再模拟操作进行测试，而这将耗费大量不必要的时间，如何节省了不必要的部署和测试时间？

从问题一的分析我们知道，传统的mvc模式下Controller层和View层耦合都较高难以分离，所以一般都是通过部署来测试。但是再MVP模式中，Presenter和Activity中是**通过接口来进行交互**，我们只需要去自定义类实现来这个接口，再这个类中来模拟Activity调用就可以进行单元测试，开发效率大大提高。

##从mvp到设计模式
---
**1.一句话简单概括mvp**
>mvp是安卓中面向接口编程的典型，presenter通过view和modle接口的引用，来调用具体实现类的方法

**2.三层架构**
对于各种架构思想，三层架构和MVP等模式有异曲同工之妙。三层架构是从整个程序架构的角度来分为WEB（界面层）、DAL（数据访问层）和BLL（业务逻辑层）各司其职，分工明确。对于程序员来说也是为了在不同阶段更加注重某阶段业务逻辑处理。

**2.万变不离其宗**，不管哪种设计模式，其优化目的都是：

 - 易于维护
 - 易于测试
 - 松耦合度
 - 复用性高
 - 健壮稳定


##案例与分析
---


![项目结构](http://upload-images.jianshu.io/upload_images/7365804-35c1be4206f58e18.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

此项目分包是根据功能模块来分的（登陆和主页数据显示两个模块）。
###login中的mvp分块
Modle层：对应于longinInteractorImple实现了longinInteractor
View层：对应于LoginActivity实现了LoginView接口
Presenter层：对应于LoginPreseenterImpl实现了LoginPresenter接口

###login模块中的mvp实现

**1.先来看看View层都干了那些事情**

```java
public interface LoginView {

    void showProgress();

    void hideProgress();

    void setUsernameError();

    void setPasswordError();

    void navigateToHome();

}

```


```java
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginView {

    private ProgressBar progressBar;
    private EditText username;
    private EditText password;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); //初始化UI

        progressBar = findViewById(R.id.progress);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        findViewById(R.id.button).setOnClickListener(this); //绑定监听

        presenter = new LoginPresenterImpl(this); //建立UI元素与Presenter的关联
    }


    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }


    @Override
    public void onClick(View view) {
        presenter.validateCredentials(username.getText().toString(),password.getText().toString());
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setUsernameError() {
        username.setError(getString(R.string.username_error));
    }

    @Override
    public void setPasswordError() {
        password.setError(getString(R.string.password_error));
    }

    @Override
    public void navigateToHome() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
```
可以看到现在Activity主要负责的就是以下三件事：
 - 初始化UI
 - 绑定监听
 - 建立UI元素与Presenter的关联

**2.再来看看Presenter层干了那些事情**

```java
public interface LoginPresenter {

    void validateCredentials(String username,String password);

    void onDestroy();
}
```

```java

public class LoginPresenterImpl implements LoginPresenter, LoginInteractor.OnloginFinishedListener {

    private LoginView loginView;
    private LoginInteractor interactor;

    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        this.interactor = new LoginInteractorImpl();
    }

    @Override
    public void validateCredentials(String username, String password) {
        if(loginView != null){
            loginView.showProgress();
        }

        interactor.login(username,password,this); //关联Modle层
    }

    @Override
    public void onDestroy() {
        loginView = null;
    }

    @Override
    public void onUsernameError() {
        if(loginView != null){
            loginView.setUsernameError();
            loginView.hideProgress();
        }
    }

    @Override
    public void onPasswordError() {
        if(loginView != null){
            loginView.setPasswordError();
            loginView.hideProgress();
        }
    }

    @Override
    public void onSuccess() {
        if(loginView != null){
            loginView.navigateToHome();
        }
    }
}

```
可以看到P层主要是处理loginActivity传给LoginPresenterImpl 逻辑业务，并在需要访问数据的时候关联了M层。

**3.最后来看看Modle层干了那些事情**

```java
public interface LoginInteractor {

    interface OnloginFinishedListener{
        void onUsernameError();
        void onPasswordError();
        void onSuccess();
    }

    void login(String username,String password,OnloginFinishedListener listener);

}
```

```java
public class LoginInteractorImpl implements LoginInteractor {
    @Override
    public void login(final String username, final String password, final OnloginFinishedListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean error = false;
                if(TextUtils.isEmpty(username)){
                    listener.onUsernameError(); //处理P层传入的逻辑
                    error = true;
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    listener.onPasswordError();
                    error = true;
                    return;
                }
                if (!error){
                    listener.onSuccess();
                }

            }
        },2000);
    }
}
```
这登陆模块的modle层未涉及到数据访问，只是做了模拟操作。

**4.总结**
总体来说mvp给人的感觉是很爽快的，特别是activity中的书写更是简明清爽，在activity中只是看到Ui监听的代码和P层绑定代码，而具体逻辑则在P层中实现，P层中涉及到数据访问则绑定M层，然后在M层中处理相应数据的封装，再把结果给P层，P处理业务后在给V显示。现在再看文章头的关系图是不是更亲切了呢？(●'◡'●)

总得来说mvp用的不是很多，也没有说非要遵从这个模式，模式始终都是为程序员服务的，每种模式都是各有弊利。对于初学者来说不建议对大项目用mvp，可以先从小项目上尝试使用，熟能生巧。以下附录提供进阶建议。

附录：
[Introduction-to-Model-View-Presenter-on-Android 英文翻译版](http://konmik.com/post/introduction_to_model_view_presenter_on_android/)（MVP经典必读）
[Introduction-to-Model-View-Presenter-on-Android 中文翻译版](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0425/2782.html)
[ZhiHuMVP](https://github.com/CameloeAnthony/ZhiHuMVP)（MVP配合RxJava 响应式编程）
[ActivityFragmentMVP github地址](https://github.com/spengilley/ActivityFragmentMVP)（MVP处理Activity和Fragment，Dagger 注入）
[Material-Movies github地址](https://github.com/saulmm/Material-Movies)（ 使用material design +MVP实现的Material-Movies）
[androidmvp](https://github.com/antoniolg/androidmvp)（star2000+的MVP实例）
[MVP for Android: how to organize the presentation layer](http://antonioleiva.com/mvp-android/)（star2000+MVP的讲解）
