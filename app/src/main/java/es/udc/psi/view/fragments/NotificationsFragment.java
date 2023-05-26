package es.udc.psi.view.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Map;

import es.udc.psi.R;
import es.udc.psi.model.Notification;
import es.udc.psi.repository.impl.UserRepositoryImpl;
import es.udc.psi.repository.interfaces.UserRepository;
import es.udc.psi.view.adapters.NotificationsAdapter;

public class NotificationsFragment extends Fragment {

    private UserRepository mUserRepository;
    private ListView mNotificationsListView;
    private TextView mEmptyNotificationsTextView;
    private NotificationsAdapter mNotificationsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        mUserRepository = new UserRepositoryImpl();
        mNotificationsListView = view.findViewById(R.id.notificationsListView);
        mEmptyNotificationsTextView = view.findViewById(R.id.emptyNotificationsTextView);

        // Use your actual user id here
        String userId = "actualUserId";

        mUserRepository.getNotifications(userId, new UserRepository.OnNotificationsFetchedListener() {
            @Override
            public void onFetched(Map<String, Notification> notificationsMap) {
                if (notificationsMap.isEmpty()) {
                    mEmptyNotificationsTextView.setVisibility(View.VISIBLE);
                    mNotificationsListView.setVisibility(View.GONE);
                } else {
                    ArrayList<Notification> notificationsList = new ArrayList<>(notificationsMap.values());
                    mNotificationsAdapter = new NotificationsAdapter(getContext(), notificationsList);
                    mNotificationsListView.setAdapter(mNotificationsAdapter);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle error
            }
        });

        return view;
    }
}
