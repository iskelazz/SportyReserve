package es.udc.psi.view.interfaces;

import es.udc.psi.model.Reserve;

public interface EditReserveView {

    void onReserveChanged();
    void onEditingReserveFailure(String errorMessage);
    void showValidationError(String fieldName, String errorMessage);
    void clearValidationError(String fieldName);
}
