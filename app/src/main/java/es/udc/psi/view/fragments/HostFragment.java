package es.udc.psi.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.udc.psi.R;
import es.udc.psi.controller.impl.BookControllerImpl;
import es.udc.psi.controller.impl.UserControllerImpl;
import es.udc.psi.controller.interfaces.BookController;
import es.udc.psi.controller.interfaces.UserController;
import es.udc.psi.model.Reserve;
import es.udc.psi.repository.interfaces.BookRepository;
import es.udc.psi.view.adapters.ReservesAdapter;

public class HostFragment extends Fragment {
    private RecyclerView recyclerView;
    private ReservesAdapter adapter;
    private BookController bookController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_host, container, false);

        recyclerView = rootView.findViewById(R.id.anfitrion_recyclerview);

        // Añade aquí la lógica para cargar la lista de reservas creadas por el usuario
        List<Reserve> reservas = new ArrayList<>();

        // Configura el RecyclerView con el adaptador personalizado
        adapter = new ReservesAdapter(reservas);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
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
}
