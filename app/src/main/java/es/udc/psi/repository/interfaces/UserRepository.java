package es.udc.psi.repository.interfaces;

import android.net.Uri;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

import java.util.Map;

import es.udc.psi.model.Notification;
import es.udc.psi.model.User;

public interface UserRepository {
    String getCurrentUserId();
    String getAuthId();
    void createUser(User usuario, OnUserCreatedListener listener);
    void getUser(String uid, final OnUserFetchedListener listener);
    void checkUsernameExists(String username, OnUsernameCheckedListener listener);
    public void addNotification(String userId, Notification notification, final OnNotificationAddedListener listener);
    public void getNotifications(String userId, final OnNotificationsFetchedListener listener);

    void signInWithEmailAndPassword(String email, String password, OnCompleteListener<AuthResult> listener);
    public void updateUserPassword(User user, String oldPassword, String newPassword, OnPasswordChangedListener listener);

    interface OnUserCreatedListener {
        void onSuccess();
        void onFailure(String errorMessage);
    }
    public interface OnPasswordChangedListener {
        void onSuccess();
        void onFailure(String error);
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

    interface OnNotificationsFetchedListener {
        void onFetched(Map<String, Notification> notifications);
        void onFailure(String error);
    }

    interface OnNotificationAddedListener {
        void onSuccess();
        void onFailure(String errorMessage);
    }

      void uploadAvatarAndSetUrlAvatar(Uri uriAvatarImage);
}
