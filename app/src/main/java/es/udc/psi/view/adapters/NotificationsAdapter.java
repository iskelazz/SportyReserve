package es.udc.psi.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import es.udc.psi.model.Notification;
import es.udc.psi.databinding.NotificationItemBinding;

public class NotificationsAdapter extends ArrayAdapter<Notification> {

    private final SimpleDateFormat mDateFormat;

    public NotificationsAdapter(Context context, List<Notification> notifications) {
        super(context, 0, notifications);
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NotificationItemBinding binding;

        if (convertView == null) {
            binding = NotificationItemBinding.inflate(LayoutInflater.from(getContext()), parent, false);
            convertView = binding.getRoot();
        } else {
            binding = (NotificationItemBinding) convertView.getTag();
        }

        Notification notification = getItem(position);
        if (notification != null) {
            binding.notificationTitleTextView.setText(notification.getTitle());
            binding.notificationMessageTextView.setText(notification.getMessage());
            binding.notificationDateTextView.setText(mDateFormat.format(notification.getDate()));
        }

        convertView.setTag(binding);
        return convertView;
    }
}
