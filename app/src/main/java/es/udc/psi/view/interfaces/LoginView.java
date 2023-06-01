package es.udc.psi.view.interfaces;

public interface LoginView {
    void onLoginSuccess(String email, String password);
    void onLoginFailed(String errorMessage);
}
