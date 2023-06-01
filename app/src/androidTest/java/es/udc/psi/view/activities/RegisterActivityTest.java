package es.udc.psi.view.activities;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import android.view.View;
import android.widget.Button;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.udc.psi.R;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {

    @Rule
    public ActivityScenarioRule<RegisterActivity> activityRule = new ActivityScenarioRule<>(RegisterActivity.class);

    @Test
    public void testRegisterButtonIsDisabledWhenFieldsAreEmpty() {
        // Verifica que el botón de registro está deshabilitado cuando los campos están vacíos
        Espresso.onView(withId(R.id.registerButton))
                .check(isEnabled(false));
    }

    @Test
    public void testRegisterButtonIsEnabledWhenFieldsAreNotEmpty() {
        // Rellena todos los campos
        Espresso.onView(withId(R.id.emailEditText)).perform(ViewActions.typeText("test@example.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.phoneEditText)).perform(ViewActions.typeText("1234567890"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.firstNameEditText)).perform(ViewActions.typeText("John"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.lastNameEditText)).perform(ViewActions.typeText("Doe"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.passwordEditText)).perform(ViewActions.typeText("password"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.nicknameEditText)).perform(ViewActions.typeText("johndoe"), ViewActions.closeSoftKeyboard());

        // Verifica que el botón de registro está habilitado cuando todos los campos están llenos
        Espresso.onView(withId(R.id.registerButton))
                .check(isEnabled(true));
    }

    // Aquí puedes agregar más pruebas de UI para cubrir los diferentes casos de uso y escenarios de la actividad de registro

    // Método auxiliar para verificar si una vista está habilitada o deshabilitada
    private ViewAssertion isEnabled(final boolean enabled) {
        return (view, noViewFoundException) -> {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            assertTrue("View is not a Button: " + view, view instanceof Button);
            assertTrue("View is not enabled: " + view, enabled == view.isEnabled());
        };
    }
}
