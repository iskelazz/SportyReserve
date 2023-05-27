package es.udc.psi.controller.interfaces;

import android.net.Uri;

import es.udc.psi.repository.interfaces.UserRepository;

public interface UserController {
    public void getUser(String uid, UserRepository.OnUserFetchedListener listener);
    String getCurrentUserId();

    void uploadAvatarAndSetUrlAvatar(Uri uriAvatarImage);
}
