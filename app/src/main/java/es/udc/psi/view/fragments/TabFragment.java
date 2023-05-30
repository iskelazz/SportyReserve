package es.udc.psi.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayoutMediator;

import es.udc.psi.view.activities.BookActivity;
import es.udc.psi.view.adapters.SectionsPagerAdapter;
import es.udc.psi.databinding.FragmentTabsBinding;

public class TabFragment extends Fragment {

    private SectionsPagerAdapter pagerAdapter;
    private FragmentTabsBinding binding;

    public TabFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el layout de este fragmento
        binding = FragmentTabsBinding.inflate(inflater, container, false);

        pagerAdapter = new SectionsPagerAdapter(getActivity());
        binding.viewPager.setAdapter(pagerAdapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                binding.tabs, binding.viewPager, (tab, position) -> {
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

        setupFloatingActionButton();

        return binding.getRoot();
    }
    private void setupFloatingActionButton() {
        binding.fabAddReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BookActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
