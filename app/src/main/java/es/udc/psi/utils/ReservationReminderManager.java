package es.udc.psi.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

import es.udc.psi.repository.impl.BookRepositoryImpl;

public class ReservationReminderManager {

    public static void scheduleReservationReminder(Context context, Calendar startTime, int reservationId, String title, String userId){
        Calendar reminderTime = (Calendar) startTime.clone();
        reminderTime.add(Calendar.MINUTE, -60); // Ajusta este valor para que sea 1 hora antes

        Intent intent = new Intent(context, ReminderBroadcastReceiver.class);
        intent.putExtra("title", "Reminder");
        String message = String.format("Your reservation, %s, is about to begin!", title); //TODO Igual necesita convertirse en resource
        intent.putExtra("message", message);
        intent.putExtra("userId", userId);  // Añade el userId al intent

        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getBroadcast(
                    context, reservationId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(
                    context, reservationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminderTime.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, reminderTime.getTimeInMillis(), pendingIntent);
        }
    }

    public static void cancelReservationReminder(Context context, int notificationId) {
        Intent intent = new Intent(context, ReminderBroadcastReceiver.class);
        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getBroadcast(
                    context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(
                    context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
