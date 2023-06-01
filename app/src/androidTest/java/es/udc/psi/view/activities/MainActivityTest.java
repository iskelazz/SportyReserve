package es.udc.psi.view.activities;


import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.udc.psi.R;
import androidx.test.espresso.contrib.DrawerActions;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void checkTabFragmentDisplay() {
        // Comprobar que el TabLayout se muestra
        onView(ViewMatchers.withId(R.id.tabs))
                .check(matches(ViewMatchers.isDisplayed()));

        // Comprobar que el ViewPager se muestra
        onView(ViewMatchers.withId(R.id.view_pager))
                .check(matches(ViewMatchers.isDisplayed()));

        // Comprobar que el FloatingActionButton se muestra
        onView(ViewMatchers.withId(R.id.fab_add_reservation))
                .check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void checkFabClickAndIntent() {
        // Hacer clic en el FloatingActionButton
        onView(ViewMatchers.withId(R.id.fab_add_reservation))
                .perform(ViewActions.click());

        // Comprobar si se ha lanzado la actividad BookActivity
        onView(ViewMatchers.withId(R.id.datePickerInput)) // Reemplazar con el id de alguna vista en BookActivity
                .check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void checkTabSelection() {
        // Seleccionar la segunda pestaña (Mis Reservas)
        onView(ViewMatchers.withId(R.id.view_pager))
                .perform(ViewActions.swipeLeft());

        // Comprobar si se muestra el RecyclerView de Mis Reservas
        onView(ViewMatchers.withId(R.id.mis_reservas_recyclerview))
                .check(matches(ViewMatchers.isDisplayed()));
    }
}

