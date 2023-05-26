package es.udc.psi.view.interfaces;

import es.udc.psi.model.Reserve;

public interface BookView {
    void onBookSuccess(Reserve reserve);
    void onBookFailure(String errorMessage);
    void showValidationError(String fieldName, String errorMessage);
    void clearValidationError(String fieldName);
}