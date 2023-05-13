package es.udc.psi.repository.interfaces;

import java.util.ArrayList;

import es.udc.psi.model.Reserve;

public interface BookRepository {

    void createBook(Reserve book,
                    OnBookCreatedListener listener);

    void checkBookCoincidences(Reserve book,
                               OnBookCoincidencesCheckedListener listener);

    interface OnBookCreatedListener {

        void onSuccess();

        void onFailure(String errorMessage);
    }

    interface OnBookCoincidencesCheckedListener {

        void onExists();

        void onNotExists();

        void onFailure(String errorMessage);
    }

    ArrayList<Reserve> getReservesList();
}
