package es.udc.psi.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Date;
import java.util.UUID;

import es.udc.psi.model.Notification;
import es.udc.psi.repository.impl.UserRepositoryImpl;
import es.udc.psi.repository.interfaces.UserRepository;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        String userId = intent.getStringExtra("userId");  // Recupera el userId del intent

        NotificationHandler.sendReservationNotification(context, title, message);

        UserRepository userRepository = new UserRepositoryImpl();

        Notification firebaseNotification = new Notification();
        firebaseNotification.setId(UUID.randomUUID().toString());
        firebaseNotification.setTitle(title);
        firebaseNotification.setMessage(message);
        firebaseNotification.setDate(new Date());

        // Llama al método addNotification para guardar la notificación en la base de datos
        userRepository.addNotification(userId, firebaseNotification, new UserRepository.OnNotificationAddedListener() {
            @Override
            public void onSuccess() {
                // Código a ejecutar en caso de éxito. Puede estar vacío.
            }

            @Override
            public void onFailure(String errorMessage) {
                // Código a ejecutar en caso de error. Aquí puedes manejar el error.
            }
        });
    }
}
