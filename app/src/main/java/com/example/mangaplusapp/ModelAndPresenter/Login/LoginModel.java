package com.example.mangaplusapp.ModelAndPresenter.Login;

import android.content.SharedPreferences;
import android.widget.EditText;

import com.example.mangaplusapp.Helper.DBHelper.UserDBHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginModel {

    //tao element để khi xử lí logic xong sẽ trả về thông báo cho presenter thông qua interface MVPLoginPresenter
MVPLoginPresenter ModelResponseToPresenter;

public LoginModel(MVPLoginPresenter ModelResponseToPresenter)
{
    this.ModelResponseToPresenter=ModelResponseToPresenter;
}

//END CREATE
    //Xử lí logic cho presenter
    public void handleLogin(EditText emailTxt, EditText passwordTxt, UserDBHelper db, SharedPreferences.Editor editor){
        String userEmail = emailTxt.getText().toString();
        String userPassword = passwordTxt.getText().toString();
        // if user do nothing or Missing input
        if(userEmail.equals("")||userPassword.equals("")){
            ModelResponseToPresenter.LoginDefalt();
        }
        else{
            if(db.validEmail(userEmail)){
                if(db.validPassword(userPassword)){
                    // Xác thực người dùng bằng Firebase Authentication
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Đăng nhập thành công
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        // Lấy id người dùng hiện tại
                                        String userId = user.getUid();

                                        // Lưu thông tin người dùng vào SharedPreferences
                                        editor.putString("user_id", userId);
                                        editor.putString("user_email", userEmail);
                                        editor.apply();

                                        // Gửi thông báo đăng nhập thành công tới Presenter
                                        ModelResponseToPresenter.LoginSuccess();
                                    } else {
                                        // Đăng nhập thất bại do không tìm thấy người dùng
                                        ModelResponseToPresenter.LoginFailed();
                                    }
                                } else {
                                    // Đăng nhập thất bại do email hoặc mật khẩu không chính xác
                                    ModelResponseToPresenter.LoginFailed();
                                }
                            });
                }
                else{
                    ModelResponseToPresenter.Passwordnotvalid();
                }
            }
            else{
                ModelResponseToPresenter.Emailnoitvalid();
            }
        }
    }

    public void ResAction()
    {
       ModelResponseToPresenter.ResActionPage();
    }

    public void ForgotAction()
    {
        ModelResponseToPresenter.ForgotAction();
    }
}
