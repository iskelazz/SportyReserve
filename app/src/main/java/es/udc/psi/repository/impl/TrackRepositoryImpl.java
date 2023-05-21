package es.udc.psi.repository.impl;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.udc.psi.model.Track;
import es.udc.psi.repository.interfaces.TrackRepository;

public class TrackRepositoryImpl implements TrackRepository {
    private DatabaseReference mDatabase;

    public TrackRepositoryImpl()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference("Locations");
    }

    @Override
    public void retrieveTracks(OnTracksRetrievedListener listener) {
        final Query aux = mDatabase.orderByChild("name");

        aux.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<Track> results = new ArrayList<>();
                    for (DataSnapshot data: dataSnapshot.getChildren())
                    {
                        Log.d("Tracks Retrieval onDataChange", "results: " + data.toString());
                        results.add(data.getValue(Track.class));
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
