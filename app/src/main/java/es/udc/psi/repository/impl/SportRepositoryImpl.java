package es.udc.psi.repository.impl;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.udc.psi.model.Reserve;
import es.udc.psi.model.Sport;
import es.udc.psi.repository.interfaces.SportRepository;

public class SportRepositoryImpl implements SportRepository {
    private DatabaseReference mDatabase;

    public SportRepositoryImpl()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference("Sports");
    }
    @Override
    public void retrieveSports(OnSportsRetrievedListener listener) {
        final Query aux = mDatabase.orderByChild("Name");

        aux.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<Sport> results = new ArrayList<>();
                    for (DataSnapshot data: dataSnapshot.getChildren())
                    {
                        Log.d("Sports Retrieval onDataChange", "results: " + data.toString());
                        results.add(data.getValue(Sport.class));
                    }
                    listener.onFetched(results);
                } else {
                    listener.onFailure("Error retrieving sports from Sports database");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure(databaseError.getMessage());
            }
        });
    }
}
