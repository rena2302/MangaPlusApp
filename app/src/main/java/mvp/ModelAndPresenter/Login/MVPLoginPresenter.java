package mvp.ModelAndPresenter.Login;

public interface MVPLoginPresenter {
    void LoginSuccess();
    void LoginFailed();
    void LoginDefalt();
    void ResActionPage();
    void ForgotAction();
    void Emailnoitvalid();
    void Passwordnotvalid();
}
