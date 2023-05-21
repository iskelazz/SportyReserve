package es.udc.psi.repository.interfaces;

import java.util.ArrayList;

import es.udc.psi.model.Track;

public interface TrackRepository {

    public void retrieveTracks(OnTracksRetrievedListener listener);

    interface OnTracksRetrievedListener
    {
        void onFetched(ArrayList<Track> result);
        void onFailure(String errorMessage);
    };

}
