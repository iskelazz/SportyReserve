package es.udc.psi.controller.impl;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.udc.psi.R;
import es.udc.psi.controller.interfaces.LoginController;
import es.udc.psi.repository.interfaces.UserRepository;
import es.udc.psi.utils.ResourceDemocratizator;
import es.udc.psi.view.interfaces.LoginView;

public class LoginControllerImpl implements LoginController {

    private LoginView loginView;
    private UserRepository userRepository;

    public LoginControllerImpl(LoginView loginView, UserRepository userRepository) {
        this.loginView = loginView;
        this.userRepository = userRepository;
    }

    @Override
    public void login(String email, String password) {
        userRepository.signInWithEmailAndPassword(email, password, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    loginView.onLoginSuccess(email, password);
                } else {
                    Exception exception = task.getException();
                    String errorMessage = exception != null ? exception.getMessage() : ResourceDemocratizator.getInstance().getStringFromResourceID(R.string.Error_Unkown);
                    loginView.onLoginFailed(errorMessage);
                }
            }
        });
    }
}