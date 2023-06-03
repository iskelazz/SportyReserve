package es.udc.psi.controller.impl;

import android.util.Patterns;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.udc.psi.R;
import es.udc.psi.controller.interfaces.RegisterController;
import es.udc.psi.model.User;
import es.udc.psi.repository.impl.UserRepositoryImpl;
import es.udc.psi.repository.interfaces.UserRepository;
import es.udc.psi.utils.ResourceDemocratizator;
import es.udc.psi.view.interfaces.RegisterView;

public class RegisterControllerImpl implements RegisterController {

    private RegisterView view;
    private FirebaseAuth mAuth;
    private UserRepository userRepository;

    public RegisterControllerImpl(RegisterView view) {
        this.view = view;
        mAuth = FirebaseAuth.getInstance();
        userRepository = new UserRepositoryImpl();
    }
    public RegisterControllerImpl(RegisterView view, FirebaseAuth mAuth, UserRepository userRepository) {
        this.view = view;
        this.mAuth = mAuth;
        this.userRepository = userRepository;
    }

    @Override
    public void validateAndRegister(String email, String phone, String firstName, String lastName, String password, String username) {
        boolean isValid = validateFields(email, phone, firstName, lastName, password, username);

        if (isValid) {
            registerUser(email, phone, firstName, lastName, password, username);
        }
    }

    private boolean validateFields(String email, String phone, String firstName, String lastName, String password, String username) {
        boolean isValid = true;

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.showValidationError("email", ResourceDemocratizator.getInstance().getStringFromResourceID(R.string.ValError_InvalidEmail));
            isValid = false;
        } else {
            view.clearValidationError("email");
        }

        if (!Patterns.PHONE.matcher(phone).matches()) {
            view.showValidationError("phone", ResourceDemocratizator.getInstance().getStringFromResourceID(R.string.ValError_InvalidPhoneNumber));
            isValid = false;
        } else {
            view.clearValidationError("phone");
        }

        if (firstName.isEmpty()) {
            view.showValidationError("firstName", ResourceDemocratizator.getInstance().getStringFromResourceID(R.string.ValError_EmptyField));
            isValid = false;
        } else {
            view.clearValidationError("firstName");
        }

        if (lastName.isEmpty()) {
            view.showValidationError("lastName", ResourceDemocratizator.getInstance().getStringFromResourceID(R.string.ValError_EmptyField));
            isValid = false;
        } else {
            view.clearValidationError("lastName");
        }

        if (password.length() < 8) {
            view.showValidationError("password", ResourceDemocratizator.getInstance().getStringFromResourceID(R.string.ValError_InvalidPasswordLength));
            isValid = false;
        } else {
            view.clearValidationError("password");
        }

        if (username.length() < 3) {
            view.showValidationError("username", ResourceDemocratizator.getInstance().getStringFromResourceID(R.string.ValError_InvalidUsernameLength));
            isValid = false;
        } else {
            view.clearValidationError("username");
        }

        return isValid;
    }

    private void registerUser(final String email, final String phone, final String firstName, final String lastName, final String password, final String username) {
        userRepository.checkUsernameExists(username, new UserRepository.OnUsernameCheckedListener() {
            @Override
            public void onExists() {
                view.showValidationError("username", ResourceDemocratizator.getInstance().getStringFromResourceID(R.string.ValError_UsernameAlreadyTaken));
            }

            @Override
            public void onNotExists() {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userId = userRepository.getAuthId();
                            User usuario = new User(userId, firstName, email, password, phone, lastName, username);

                            userRepository.createUser(usuario, new UserRepository.OnUserCreatedListener() {
                                @Override
                                public void onSuccess() {
                                    view.onRegisterSuccess();
                                }

                                @Override
                                public void onFailure(String errorMessage) {
                                    view.onRegisterFailure(ResourceDemocratizator.getInstance().getStringFromResourceID(R.string.Failure_UserRegistration) + errorMessage);
                                }
                            });
                        } else {
                            view.onRegisterFailure(ResourceDemocratizator.getInstance().getStringFromResourceID(R.string.Failure_UserCreation) + task.getException().getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                view.onRegisterFailure(ResourceDemocratizator.getInstance().getStringFromResourceID(R.string.Failure_UsernameVerification) + errorMessage);
            }
        });
    }
}