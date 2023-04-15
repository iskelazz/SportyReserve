package es.udc.psi.repository.interfaces;

import es.udc.psi.model.Usuario;

public interface UserRepository {
    void createUser(Usuario usuario, OnUserCreatedListener listener);

    void checkUsernameExists(String username, OnUsernameCheckedListener listener);

    interface OnUserCreatedListener {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    interface OnUsernameCheckedListener {
        void onExists();
        void onNotExists();
        void onFailure(String errorMessage);
    }
}
