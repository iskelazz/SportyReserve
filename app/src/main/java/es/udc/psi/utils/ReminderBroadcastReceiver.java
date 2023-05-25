package es.udc.psi.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import es.udc.psi.utils.NotificationHandler;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");

        NotificationHandler.sendReservationNotification(context, title, message);
    }
}
