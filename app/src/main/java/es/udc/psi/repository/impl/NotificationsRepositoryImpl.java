package es.udc.psi.repository.impl;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class NotificationsRepositoryImpl {
    public void subscribeToReservaTopic(String reservaId) {
        FirebaseMessaging.getInstance().subscribeToTopic("reserva_" + reservaId)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Usuario suscrito al tema de la reserva!";
                        if (!task.isSuccessful()) {
                            msg = "Suscripcion al tema fallida";
                        }
                        Log.d(TAG, msg);
                    }
                });
    }
}
