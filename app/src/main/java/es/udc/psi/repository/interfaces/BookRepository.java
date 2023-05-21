package es.udc.psi.repository.interfaces;

import java.util.ArrayList;
import java.util.List;

import es.udc.psi.model.Reserve;
import es.udc.psi.model.User;

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

    interface OnBookDeletedListener {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    interface OnReserveRetrievedListener {
        void onSuccess(Reserve reserve);

        void onFailure(String errorMessage);
    }

    interface OnUserListUpdatedListener {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    void updateReserve(Reserve reserve, final OnReserveUpdatedListener listener);

    interface OnReserveUpdatedListener {
        void onSuccess();
        void onFailure(String errorMessage);
    }
    ArrayList<Reserve> getReservesList();
    public void replaceUserListWithNew(String bookId, List<User> newUserList, OnUserListUpdatedListener listener);
}
