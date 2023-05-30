package es.udc.psi.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import es.udc.psi.controller.impl.BookControllerImpl;
import es.udc.psi.controller.impl.UserControllerImpl;
import es.udc.psi.controller.interfaces.BookController;
import es.udc.psi.controller.interfaces.UserController;
import es.udc.psi.model.Reserve;
import es.udc.psi.repository.interfaces.BookRepository;
import es.udc.psi.view.adapters.ReservesAdapter;
import es.udc.psi.databinding.FragmentHostBinding;

public class HostFragment extends Fragment {
    private ReservesAdapter adapter;
    private BookController bookController;
    private FragmentHostBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHostBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Añade aquí la lógica para cargar la lista de reservas creadas por el usuario
        List<Reserve> reservas = new ArrayList<>();

        // Configura el RecyclerView con el adaptador personalizado
        adapter = new ReservesAdapter(reservas);
        binding.anfitrionRecyclerview.setAdapter(adapter);
        binding.anfitrionRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bookController = new BookControllerImpl();
        fetchReserves();
    }

    public void updateReserves(List<Reserve> newReserves) {
        adapter.updateData(newReserves);
    }

    private void fetchReserves() {
        UserController userController = new UserControllerImpl();
        String currentUserId = userController.getCurrentUserId();
        bookController.fetchHostReserves(currentUserId, new BookRepository.OnReservesFetchedListener() {
            @Override
            public void onFetched(ArrayList<Reserve> reserves) {
                updateReserves(reserves);
            }

            @Override
            public void onFailure(String errorMsg) {
                // Maneja el error aquí
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
