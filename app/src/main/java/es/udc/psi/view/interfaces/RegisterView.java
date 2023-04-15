package es.udc.psi.view.interfaces;

public interface RegisterView {
    void onRegisterSuccess();
    void onRegisterFailure(String errorMessage);
    void showValidationError(String fieldName, String errorMessage);
    void clearValidationError(String fieldName);
}