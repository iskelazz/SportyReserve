package es.udc.psi.view.activities;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import es.udc.psi.R;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testFieldsAreEmptyAtLaunch() {
        // Verifica que los campos de email y password están vacíos al inicio
        Espresso.onView(withId(R.id.emailEditText))
                .check(matches(withText("")));
        Espresso.onView(withId(R.id.passwordEditText))
                .check(matches(withText("")));
    }

    @Test
    public void testLoginButtonClickWithEmptyFields() {
        // Presiona el botón de inicio de sesión con los campos vacíos
        Espresso.onView(withId(R.id.loginButton)).perform(ViewActions.click());

        // Verifica que los campos de email y password muestran los mensajes de error adecuados
        Espresso.onView(withId(R.id.emailEditText))
                .check(matches(hasErrorText("Email is required.")));
    }

    @Test
    public void testLoginButtonClickWithFilledFields() {
        // Rellena los campos de email y password
        Espresso.onView(withId(R.id.emailEditText)).perform(ViewActions.typeText("test@example.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.passwordEditText)).perform(ViewActions.typeText("password"), ViewActions.closeSoftKeyboard());

        // Presiona el botón de inicio de sesión con los campos llenos
        Espresso.onView(withId(R.id.loginButton)).perform(ViewActions.click());

    }

}
