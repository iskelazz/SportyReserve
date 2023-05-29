package es.udc.psi.repository.impl;

import es.udc.psi.model.Notification;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import es.udc.psi.model.User;
import es.udc.psi.repository.interfaces.UserRepository;

public class UserRepositoryImpl implements UserRepository {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    public UserRepositoryImpl() {
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public String getCurrentUserId() {
        return mAuth.getCurrentUser().getUid();
    }

    @Override
    public String getAuthId() {
        return mAuth.getCurrentUser().getUid();
    }

    @Override
    public void createUser(User usuario, final OnUserCreatedListener listener) {
        mDatabase.child(usuario.getId()).setValue(usuario)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }

    @Override
    public void addNotification(String userId, Notification notification, final OnNotificationAddedListener listener) {
        mDatabase.child(userId).child("notifications").child(notification.getId()).setValue(notification)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }

    @Override
    public void getNotifications(String userId, final OnNotificationsFetchedListener listener) {
        mDatabase.child(userId).child("notifications").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Notification> notifications = new HashMap<>();
                for (DataSnapshot notificationSnapshot: dataSnapshot.getChildren()) {
                    Notification notification = notificationSnapshot.getValue(Notification.class);
                    notifications.put(notification.getId(), notification);
                }
                listener.onFetched(notifications);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure(databaseError.getMessage());
            }
        });
    }
    @Override
    public void checkUsernameExists(String username, final OnUsernameCheckedListener listener) {
        mDatabase.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listener.onExists();
                } else {
                    listener.onNotExists();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure(databaseError.getMessage());
            }
        });
    }

    @Override
    public void getUser(String uid, final OnUserFetchedListener listener) {
        mDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                listener.onFetched(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure(databaseError.getMessage());
            }
        });
    }

    @Override
    public void signInWithEmailAndPassword(String email, String password, OnCompleteListener<AuthResult> listener) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    @Override
    public void updateUserPassword(User user, String oldPassword, String newPassword, OnPasswordChangedListener listener) {
        // Verificar si la contraseña nueva cumple con los requisitos mínimos
        if (newPassword.length() < 8) {
            listener.onFailure("La contraseña debe tener al menos 8 caracteres");
            return;
        }

        // Verificar la contraseña antigua iniciando sesión con el correo electrónico del usuario y la contraseña antigua
        mAuth.signInWithEmailAndPassword(user.getCorreoElectronico(), oldPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Si el inicio de sesión es exitoso, actualizar la contraseña en FirebaseAuth
                mAuth.getCurrentUser().updatePassword(newPassword).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        // Si la actualización de la contraseña es exitosa en FirebaseAuth, actualizarla en Firebase Realtime Database
                        user.setContraseña(newPassword);
                        mDatabase.child(user.getId()).setValue(user)
                                .addOnSuccessListener(aVoid -> listener.onSuccess())
                                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
                    } else {
                        // Si falla la actualización de la contraseña en FirebaseAuth, enviar el error al listener
                        listener.onFailure(task1.getException().getMessage());
                    }
                });
            } else {
                // Si falla el inicio de sesión, enviar el error al listener
                listener.onFailure(task.getException().getMessage());
            }
        });
    }
}
