package es.udc.psi.repository.impl;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.udc.psi.R;
import es.udc.psi.model.Track;
import es.udc.psi.repository.interfaces.TrackRepository;
import es.udc.psi.utils.ResourceDemocratizator;

public class TrackRepositoryImpl implements TrackRepository {
    private DatabaseReference mDatabase;

    public TrackRepositoryImpl()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference(ResourceDemocratizator.getInstance().getStringFromResourceID(R.string.name_TracksDB));
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
                    listener.onFailure(ResourceDemocratizator.getInstance().getStringFromResourceID(R.string.Failure_TracksRetrieval));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure(databaseError.getMessage());
            }
        });
    }
}
