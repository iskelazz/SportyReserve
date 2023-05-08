package es.udc.psi.view.interfaces;

import java.util.ArrayList;

import es.udc.psi.pruebattreservasmvc.model.Reserve;

public interface ReservesListView {

    void showReservesList(ArrayList<Reserve> reservationList);

    void showError();

    void showEmptyView();

    void updateReserve(Reserve reserve,
                       int position);

}