package es.udc.psi.controller.impl;

import android.net.Uri;

import java.util.Map;

import es.udc.psi.controller.interfaces.UserController;
import es.udc.psi.model.Notification;
import es.udc.psi.repository.impl.UserRepositoryImpl;
import es.udc.psi.repository.interfaces.UserRepository;

public class UserControllerImpl implements UserController {
    private UserRepository userRepository;

    public UserControllerImpl() {
        this.userRepository = new UserRepositoryImpl();
    }

    public void getUser(String uid, UserRepository.OnUserFetchedListener listener) {
        userRepository.getUser(uid, listener);
    }

    @Override
    public String getCurrentUserId() {
        return userRepository.getCurrentUserId();
    }


    @Override
    public void getNotifications(String uid, UserRepository.OnNotificationsFetchedListener listener) {
        userRepository.getNotifications(uid, new UserRepository.OnNotificationsFetchedListener() {
            @Override
            public void onFetched(Map<String, Notification> notifications) {
                listener.onFetched(notifications);
            }

            @Override
            public void onFailure(String errorMessage) {
                listener.onFailure(errorMessage);
            }
        });
    }

  
    @Override
    public void uploadAvatarAndSetUrlAvatar(Uri uriAvatarImage) {
        userRepository.uploadAvatarAndSetUrlAvatar(uriAvatarImage);
    }
  
}