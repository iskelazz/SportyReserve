package es.udc.psi.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.udc.psi.R;
import es.udc.psi.model.Reserva;
import es.udc.psi.view.adapters.ReservesAdapter;

public class MyReservesFragment extends Fragment {
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_reserves, container, false);
        recyclerView = rootView.findViewById(R.id.mis_reservas_recyclerview);

        // Añade aquí la lógica para cargar la lista de reservas en las que el usuario está anotado
        List<Reserva> reservas = new ArrayList<>();

        // Configura el RecyclerView con el adaptador personalizado
        ReservesAdapter adapter = new ReservesAdapter(reservas);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }
}
