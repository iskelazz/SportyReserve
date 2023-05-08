package es.udc.psi.view.interfaces;

import java.util.ArrayList;

import es.udc.psi.model.Reserve;

public interface ReservesListView {

    void showReservesList(ArrayList<Reserve> reservesList);

    void showError();

    void showEmptyView();

    void updateReserve(Reserve reserve,
                       int position);

}