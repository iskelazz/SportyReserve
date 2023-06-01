package es.udc.psi.controller.interfaces;


import android.net.Uri;

import java.util.List;
import java.util.Map;

import es.udc.psi.model.Notification;
import es.udc.psi.repository.interfaces.UserRepository;

public interface UserController {
    public void getUser(String uid, UserRepository.OnUserFetchedListener listener);
    String getCurrentUserId();

    public void getNotifications(String uid, UserRepository.OnNotificationsFetchedListener listener);

    interface OnNotificationsFetchedListener {
        void onFetched(Map<String, Notification> notifications);
        void onFailure(String errorMessage);
    }

      void uploadAvatarAndSetUrlAvatar(Uri uriAvatarImage);
}
