package mvp.UI.Login;

import android.content.SharedPreferences;
import android.widget.EditText;

import Database.CreateDatabase;

public class LoginPresenter implements MVPLoginPresenter {
    private  LoginModel loginmodel;
    private  MVPLoginView PresenterResponseToViewAct;
    public LoginPresenter (MVPLoginView PresenterResponseToViewAct)
    {
        this.PresenterResponseToViewAct=PresenterResponseToViewAct;
    }
    public void receivedHandleLogin(EditText emailTxt, EditText passwordTxt, CreateDatabase db, int idUser, SharedPreferences.Editor editor)
    {
        loginmodel=new LoginModel(this);
       loginmodel.handleLogin(emailTxt,passwordTxt, db, idUser,editor);//thông báo cho model để sử lí logic
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
}
