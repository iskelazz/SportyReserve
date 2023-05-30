package es.udc.psi.view.activities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import es.udc.psi.controller.impl.RegisterControllerImpl;
import es.udc.psi.repository.interfaces.UserRepository;
import es.udc.psi.view.interfaces.RegisterView;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

@RunWith(MockitoJUnitRunner.class)
public class RegisterControllerImplTest {

    @Mock
    RegisterView mockView;

    @Mock
    FirebaseAuth mockFirebaseAuth;

    @Mock
    UserRepository mockUserRepository;

    private RegisterControllerImpl registerController;

    @Before
    public void setup() {
        registerController = new RegisterControllerImpl(mockView, mockFirebaseAuth, mockUserRepository);
    }

    @Test
    public void testRegisterSuccess() {
        // Organizar
        String email = "test@test.com";
        String password = "password123";
        AuthResult authResult = mock(AuthResult.class);
        Task<AuthResult> mockTask = mock(Task.class);

        when(mockTask.isSuccessful()).thenReturn(true);
        when(mockTask.getResult()).thenReturn(authResult);
        when(mockFirebaseAuth.createUserWithEmailAndPassword(email, password)).thenReturn(mockTask);

        // Actuar
        registerController.validateAndRegister(email, "1234567890", "John", "Doe", password, "johndoe");

        // Afirmar
        verify(mockView).onRegisterSuccess();
    }


}
