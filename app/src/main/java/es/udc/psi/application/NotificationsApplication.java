package es.udc.psi.application;

import android.app.Application;

import es.udc.psi.utils.NotificationHandler;

public class NotificationsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        NotificationHandler.createNotificationChannel(this);
    }
}
