package es.udc.psi.view.fragments;

// ... (importaciones necesarias)

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
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
import es.udc.psi.view.adapters.NotificationsAdapter;
import es.udc.psi.repository.impl.UserRepositoryImpl;
import es.udc.psi.repository.interfaces.UserRepository;

public class NotificationsFragment extends Fragment {
    private ListView notificationsListView;
    private TextView emptyNotificationsTextView;
    private UserRepository userRepository;
    private NotificationsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        notificationsListView = view.findViewById(R.id.notificationsListView);
        emptyNotificationsTextView = view.findViewById(R.id.emptyNotificationsTextView);

        userRepository = new UserRepositoryImpl();
        String userId = userRepository.getCurrentUserId(); // Obtén el ID del usuario actual

        userRepository.getNotifications(userId, new UserRepository.OnNotificationsFetchedListener() {
            @Override
            public void onFetched(Map<String, Notification> notifications) {
                // Cuando tengamos las notificaciones, las mostramos en el ListView.
                List<Notification> notificationList = new ArrayList<>(notifications.values());

                // Ordena la lista en orden inverso.
                Collections.reverse(notificationList);

                adapter = new NotificationsAdapter(getContext(), notificationList);
                notificationsListView.setAdapter(adapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                // Gestionar el error aquí.
                Toast.makeText(getContext(), "Error fetching notifications: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
