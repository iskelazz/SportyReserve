package es.udc.psi.view.activities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import es.udc.psi.controller.impl.LoginControllerImpl;
import es.udc.psi.repository.interfaces.UserRepository;
import es.udc.psi.view.interfaces.LoginView;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerImplTest {

    @Mock
    LoginView mockView;

    @Mock
    UserRepository userRepository;

    private LoginControllerImpl loginController;

    @Before
    public void setup() {
        loginController = new LoginControllerImpl(mockView, userRepository);
    }

    @Test
    public void login_withValidCredentials_callsOnLoginSuccess() {
        // Arrange
        String email = "cantona@mail.com";
        String password = "1234567890";

        AuthResult authResult = mock(AuthResult.class);
        Task<AuthResult> mockTask = mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(true);
        when(mockTask.getResult()).thenReturn(authResult);

        doAnswer(invocation -> {
            OnCompleteListener<AuthResult> listener = invocation.getArgument(2);
            listener.onComplete(mockTask);
            return null;
        }).when(userRepository).signInWithEmailAndPassword(eq(email), eq(password), any());

        // Act
        loginController.login(email, password);

        // Assert
        verify(mockView).onLoginSuccess();
    }

    @Test
    public void login_withInvalidCredentials_callsOnLoginFailed() {
        // Arrange
        String email = "test@test.com";
        String password = "password123";
        String errorMessage = "Login failed";

        doAnswer(invocation -> {
            Task<AuthResult> task = mock(Task.class);
            when(task.isSuccessful()).thenReturn(false);
            when(task.getException()).thenReturn(new Exception(errorMessage));

            OnCompleteListener<AuthResult> listener = invocation.getArgument(2);
            listener.onComplete(task);
            return null;
        }).when(userRepository).signInWithEmailAndPassword(eq(email), eq(password), any());

        // Act
        loginController.login(email, password);

        // Assert
        verify(mockView).onLoginFailed(errorMessage);
    }
}
