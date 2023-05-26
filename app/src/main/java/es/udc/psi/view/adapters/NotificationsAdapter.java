package es.udc.psi.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import es.udc.psi.R;
import es.udc.psi.model.Notification;

public class NotificationsAdapter extends ArrayAdapter<Notification> {

    private final SimpleDateFormat mDateFormat;

    public NotificationsAdapter(Context context, List<Notification> notifications) {
        super(context, 0, notifications);
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notification_item, parent, false);
        }

        TextView titleTextView = convertView.findViewById(R.id.notificationTitleTextView);
        TextView messageTextView = convertView.findViewById(R.id.notificationMessageTextView);
        TextView dateTextView = convertView.findViewById(R.id.notificationDateTextView);

        Notification notification = getItem(position);
        if (notification != null) {
            titleTextView.setText(notification.getTitle());
            messageTextView.setText(notification.getMessage());
            dateTextView.setText(mDateFormat.format(notification.getDate()));
        }

        return convertView;
    }
}

