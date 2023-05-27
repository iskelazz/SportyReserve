package es.udc.psi.repository.impl;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import androidx.annotation.NonNull;
import es.udc.psi.model.User;
import es.udc.psi.repository.interfaces.UserRepository;

public class UserRepositoryImpl implements UserRepository {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;

    public UserRepositoryImpl() {
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
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


    public void uploadAvatarAndSetUrlAvatar(Uri fileAvatarImage){


        //StorageReference storageReferenceUID = mStorage.getReference().child(mAuth.getCurrentUser().getUid()+"/avatar.jpg");
        StorageReference storageReferenceUID = mStorage.getReference().child(getCurrentUserId()+"/avatar.jpg");
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")   //TODO: ¿qué pasa si no upload un jpg????
                .build();
        UploadTask uploadTask = storageReferenceUID.putFile(fileAvatarImage, metadata);
//TODO: controlar observers to listen for when the download is done or if it fails


/*
        storageReferenceUID.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                Log.d("TAG_LOG","urlAvatarImagen??:"+downloadUrl.toString());


            }
        });
*/

        //Log.d("TAG_LOG","urlAvatarImagen??:"+storageReferenceUID.getDownloadUrl().getResult().toString());
        //return storageReferenceUID.getDownloadUrl().getResult().toString();

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
Log.d("TAG_LOG","urlAvatarImagenblablalba1??:"+ storageReferenceUID.getDownloadUrl().toString());
Log.d("TAG_LOG","urlAvatarImagenblablalba3??:"+ storageReferenceUID.getDownloadUrl().getResult().toString());

                return storageReferenceUID.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
Log.d("TAG_LOG","urlAvatarImagenblablalba2??:"+downloadUri.toString());
                    setUrlAvatar(downloadUri.toString());
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }


    private void setUrlAvatar(String url){
        mDatabase.child(getCurrentUserId()).child("uriAvatar").setValue(url);


    }
}
