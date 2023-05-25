package es.udc.psi.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

public class ReservationReminderManager {
    public static void scheduleReservationReminder(Context context, Calendar startTime) {
        Calendar reminderTime = (Calendar) startTime.clone();
        reminderTime.add(Calendar.MINUTE, 2); // Ajusta este valor a 2 minutos en el futuro

        Intent intent = new Intent(context, ReminderBroadcastReceiver.class);
        intent.putExtra("title", "Reminder"); // Personaliza el título y el mensaje según necesites
        intent.putExtra("message", "Your reservation is about to start in 1 hour!");

        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getBroadcast(
                    context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(
                    context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminderTime.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, reminderTime.getTimeInMillis(), pendingIntent);
        }
    }
}
