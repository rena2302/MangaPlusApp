package mvp.UI.Login;

import android.content.SharedPreferences;
import android.widget.EditText;

import Database.CreateDatabase;

public class LoginModel {
    //tao element để khi xử lí logic xong sẽ trả về thông báo cho presenter thông qua interface MVPLoginPresenter
MVPLoginPresenter ModelResponseToPresenter;
public LoginModel(MVPLoginPresenter ModelResponseToPresenter)
{
    this.ModelResponseToPresenter=ModelResponseToPresenter;
}
//END CREATE
    //Xử lí logic cho presenter
    public void handleLogin(EditText emailTxt,EditText passwordTxt, CreateDatabase db,int idUser, SharedPreferences.Editor editor){
        String userEmail = emailTxt.getText().toString();
        String userPassword = passwordTxt.getText().toString();
        // if user do nothing or Missing input
        if(userEmail.equals("")||userPassword.equals("")){
            ModelResponseToPresenter.LoginDefalt();
        }
        else{
            Boolean checkEmailPass= db.CheckEmailPassword(userEmail,userPassword);
            // if email and password valid -> nav to home activity
            if(checkEmailPass){
                ////////===========================Begin Login Successful=========================//////////
                ModelResponseToPresenter.LoginSuccess();
                ////////===========================END Login Successful=========================////////////
            }
            // show message input error
            else{
                ModelResponseToPresenter.LoginFailed();
            }
        }
    }
}
