package es.udc.psi.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import es.udc.psi.R;

public class NotificationsFragment extends Fragment {
    private ListView notificationsListView;
    private TextView emptyNotificationsTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        notificationsListView = view.findViewById(R.id.notificationsListView);
        emptyNotificationsTextView = view.findViewById(R.id.emptyNotificationsTextView);

        // Configura aquí tu ListView. Por ejemplo, podrías querer configurar un adapter personalizado,
        // añadir un OnItemClickListener, etc.

        return view;
    }
}

