package es.udc.psi.view.fragments;

// ... (importaciones necesarias)

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import es.udc.psi.R;
import es.udc.psi.model.Notification;
import es.udc.psi.repository.interfaces.UserRepository;
import es.udc.psi.view.adapters.NotificationsAdapter;
import es.udc.psi.controller.impl.UserControllerImpl;
import es.udc.psi.controller.interfaces.UserController;
import es.udc.psi.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {
    private UserController userController;
    private NotificationsAdapter adapter;
    private FragmentNotificationsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        userController = new UserControllerImpl();
        String userId = userController.getCurrentUserId(); // Obtén el ID del usuario actual
        userController.getNotifications(userId, new UserRepository.OnNotificationsFetchedListener() {
            @Override
            public void onFetched(Map<String, Notification> notifications) {
                // Cuando tengamos las notificaciones, las mostramos en el ListView.
                List<Notification> notificationList = new ArrayList<>(notifications.values());

                // Ordena la lista en orden inverso.
                Collections.reverse(notificationList);

                adapter = new NotificationsAdapter(getContext(), notificationList);
                binding.notificationsListView.setAdapter(adapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                // Gestionar el error aquí.
                Toast.makeText(getContext(), getContext().getString(R.string.Toast_ErrorRetrievingNotifications) + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
