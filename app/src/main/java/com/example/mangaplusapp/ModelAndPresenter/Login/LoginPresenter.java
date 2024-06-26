package com.example.mangaplusapp.ModelAndPresenter.Login;

import android.content.SharedPreferences;
import android.widget.EditText;

import com.example.mangaplusapp.Helper.DBHelper.UserDBHelper;

public class LoginPresenter implements MVPLoginPresenter {

    private  LoginModel loginmodel;

    private  MVPLoginView PresenterResponseToViewAct;

    public LoginPresenter (MVPLoginView PresenterResponseToViewAct)
    {
        this.PresenterResponseToViewAct=PresenterResponseToViewAct;
    }

    public void receivedHandleLogin(EditText emailTxt, EditText passwordTxt, UserDBHelper db, SharedPreferences.Editor editor)
    {
        loginmodel=new LoginModel(this);
       loginmodel.handleLogin(emailTxt,passwordTxt, db,editor);//thông báo cho model để sử lí logic
    }

    public void RegisterAction()
    {
        loginmodel=new LoginModel(this);
        loginmodel.ResAction();
    }

    public void ForAction()
    {
        loginmodel=new LoginModel(this);
        loginmodel.ForgotAction();
    }

    @Override
    public void LoginSuccess() {
        PresenterResponseToViewAct.LoginSuccess();
    }

    @Override
    public void LoginFailed() {
        PresenterResponseToViewAct.LoginFailed();
    }

    @Override
    public void LoginDefalt() {
        PresenterResponseToViewAct.LoginDefalt();
    }

    @Override
    public void ResActionPage() {
        PresenterResponseToViewAct.ResActionPage();
    }

    @Override
    public void ForgotAction() {
        PresenterResponseToViewAct.ForgotAction();
    }

    @Override
    public void Emailnoitvalid() {
        PresenterResponseToViewAct.Emailnoitvalid();
    }

    @Override
    public void Passwordnotvalid() {
     PresenterResponseToViewAct.Passwordnotvalid();
    }
}
