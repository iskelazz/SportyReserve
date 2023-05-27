package es.udc.psi.controller.impl;

import android.net.Uri;

import es.udc.psi.controller.interfaces.UserController;
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
    public void uploadAvatarAndSetUrlAvatar(Uri uriAvatarImage) {
        userRepository.uploadAvatarAndSetUrlAvatar(uriAvatarImage);
    }

}