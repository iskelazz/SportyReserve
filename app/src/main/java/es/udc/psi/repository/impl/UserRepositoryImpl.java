package es.udc.psi.repository.impl;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
}
