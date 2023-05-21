package es.udc.psi.repository.interfaces;

import java.util.ArrayList;

import es.udc.psi.model.Reserve;

public interface BookRepository {

    void createBook(Reserve book,
                    OnBookCreatedListener listener);

    void checkBookCoincidences(Reserve book,
                               OnBookCoincidencesCheckedListener listener);
    void deleteReserve(String bookId, OnBookDeletedListener listener);

    interface OnBookCreatedListener {

        void onSuccess();

        void onFailure(String errorMessage);
    }

    interface OnBookCoincidencesCheckedListener {

        void onExists();

        void onNotExists();

        void onFailure(String errorMessage);
    }

    interface OnReservesFetchedListener {
        void onFetched(ArrayList<Reserve> reserve);
        void onFailure(String error);
    }

    interface OnPlayerReservesFetchedListener {
        void onFetched(ArrayList<Reserve> reserves);
        void onFailure(String error);
    }

    public interface OnBookDeletedListener {
        void onSuccess();
        void onFailure(String errorMessage);
    }
    ArrayList<Reserve> getReservesList();
}
