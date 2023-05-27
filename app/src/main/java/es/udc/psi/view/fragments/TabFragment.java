package es.udc.psi.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import es.udc.psi.R;
import es.udc.psi.view.activities.BookActivity;
import es.udc.psi.view.adapters.SectionsPagerAdapter;

public class TabFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private SectionsPagerAdapter pagerAdapter;
    private FloatingActionButton fab;

    public TabFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el layout de este fragmento
        View view = inflater.inflate(R.layout.fragment_tabs, container, false);

        tabLayout = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.view_pager);
        pagerAdapter = new SectionsPagerAdapter(getActivity());
        viewPager.setAdapter(pagerAdapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Anfitrión");
                    break;
                case 1:
                    tab.setText("Mis Reservas");
                    break;
            }
        });
        tabLayoutMediator.attach();

        setupFloatingActionButton(view);

        return view;
    }
    private void setupFloatingActionButton(View view) {
        fab = view.findViewById(R.id.fab_add_reservation);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BookActivity.class);
                startActivity(intent);
            }
        });
    }
}
