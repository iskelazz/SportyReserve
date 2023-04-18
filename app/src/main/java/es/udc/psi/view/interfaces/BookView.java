package es.udc.psi.view.interfaces;

public interface BookView {
    void onBookSuccess();
    void onBookFailure(String errorMessage);
    void showValidationError(String fieldName, String errorMessage);
    void clearValidationError(String fieldName);
}