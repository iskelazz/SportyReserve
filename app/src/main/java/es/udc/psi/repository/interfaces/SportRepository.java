package es.udc.psi.repository.interfaces;

import java.util.ArrayList;

import es.udc.psi.model.Sport;

public interface SportRepository {
    public void retrieveSports(OnSportsRetrievedListener listener);

    interface OnSportsRetrievedListener
    {
        void onFetched(ArrayList<Sport> result);
        void onFailure(String errorMessage);
    };

}
