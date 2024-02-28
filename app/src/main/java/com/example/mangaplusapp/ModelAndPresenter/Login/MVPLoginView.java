package com.example.mangaplusapp.ModelAndPresenter.Login;

public interface MVPLoginView {
    void LoginSuccess();
    void LoginFailed();
    void LoginDefalt();
    void ResActionPage();
    void ForgotAction();
    void Emailnoitvalid();
    void Passwordnotvalid();
}
