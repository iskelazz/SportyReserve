package es.udc.psi.repository.interfaces;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

import es.udc.psi.model.User;

public interface UserRepository {
    String getCurrentUserId();
    void createUser(User usuario, OnUserCreatedListener listener);
    void getUser(String uid, final OnUserFetchedListener listener);
    void checkUsernameExists(String username, OnUsernameCheckedListener listener);

    void signInWithEmailAndPassword(String email, String password, OnCompleteListener<AuthResult> listener);

    interface OnUserCreatedListener {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    interface OnUsernameCheckedListener {
        void onExists();
        void onNotExists();
        void onFailure(String errorMessage);
    }
    interface OnUserFetchedListener {
        void onFetched(User user);
        void onFailure(String error);
    }
}
