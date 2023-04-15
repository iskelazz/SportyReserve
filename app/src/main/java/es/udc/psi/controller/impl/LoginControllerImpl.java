package es.udc.psi.controller.impl;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.udc.psi.controller.interfaces.LoginController;
import es.udc.psi.view.interfaces.LoginView;

public class LoginControllerImpl implements LoginController {

    private LoginView loginView;
    private FirebaseAuth mAuth;

    public LoginControllerImpl(LoginView loginView) {
        this.loginView = loginView;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginView.onLoginSuccess();
                        } else {
                            loginView.onLoginFailed(task.getException().getMessage());
                        }
                    }
                });
    }
}