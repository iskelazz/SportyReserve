package es.udc.psi.repository.interfaces;

import java.util.ArrayList;

import es.udc.psi.model.Reserva;
import es.udc.psi.model.Usuario;

public interface BookRepository {

    void createBook(Reserva book,
                    OnBookCreatedListener listener);

    void checkBookCoincidences(Reserva book,
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
