package es.udc.psi.controller.impl;

import android.content.res.Resources;

import com.google.firebase.auth.FirebaseAuth;

import es.udc.psi.R;
import es.udc.psi.controller.interfaces.EditReserveController;
import es.udc.psi.model.Reserve;
import es.udc.psi.repository.impl.BookRepositoryImpl;
import es.udc.psi.repository.interfaces.BookRepository;
import es.udc.psi.utils.ResourceDemocratizator;
import es.udc.psi.view.interfaces.EditReserveView;

public class EditReserveControllerImpl implements EditReserveController {

    private final FirebaseAuth mAuth;
    private final BookRepositoryImpl bookRepository;
    private final EditReserveView view;

    public EditReserveControllerImpl(EditReserveView view) {
        mAuth = FirebaseAuth.getInstance();
        bookRepository = new BookRepositoryImpl();
        this.view = view;
    }


    /**
     * @param reserve The book to be validated then updated in the DB
     */
    @Override
    public void validateAndUpdate(Reserve reserve) {
        boolean isValid = validateFields(reserve.getName(), reserve.isPublic(), reserve.getPassword());

        if (isValid) {
            updateReserve(reserve);
        }
    }

    private boolean validateFields(String name, boolean isPublic, String password)
    {
        boolean isValid = true;

        if(name != null)
        {
            isValid = (name != "");
            view.showValidationError("name", ResourceDemocratizator.getInstance().getStringFromResourceID(R.string.ValError_NameIsEmpty));
        }
        else
        {
            view.clearValidationError("name");
        }
        if (!isPublic && password != null && password.length() < 8) {
            view.showValidationError("password", ResourceDemocratizator.getInstance().getStringFromResourceID(R.string.ValError_InvalidPasswordLength));
            isValid = false;
        } else {
            view.clearValidationError("password");
        }

        return isValid;
    }

    private void updateReserve(Reserve reserve)
    {
        bookRepository.updateReserve(reserve, new BookRepository.OnReserveUpdatedListener() {
            @Override
            public void onSuccess() {
                view.onReserveChanged();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.onEditingReserveFailure(ResourceDemocratizator.getInstance().getStringFromResourceID(R.string.Failure_ReserveCouldNotBeModified) + errorMessage);
            }
        });
    }
}
