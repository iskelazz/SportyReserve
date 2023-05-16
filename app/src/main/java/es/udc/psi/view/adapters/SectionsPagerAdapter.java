package es.udc.psi.view.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import es.udc.psi.view.fragments.HostFragment;
import es.udc.psi.view.fragments.MyReservesFragment;

public class SectionsPagerAdapter extends FragmentStateAdapter {
    private HostFragment hostFragment;
    public SectionsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                if (hostFragment == null) {
                    hostFragment = new HostFragment();
                }
                return hostFragment;
            case 1:
                return new MyReservesFragment();
            default:
                throw new IllegalStateException("Posición inválida: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public HostFragment getHostFragment() {
        return hostFragment;
    }
}

