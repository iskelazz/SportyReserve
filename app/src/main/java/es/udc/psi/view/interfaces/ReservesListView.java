package es.udc.psi.view.interfaces;

import java.util.ArrayList;

import es.udc.psi.model.Reserve;

public interface ReservesListView {

    void showReservesList(ArrayList<Reserve> reservesList);

    void showError(String error);

    void showEmptyView();

    void updateReserveListView(Reserve reserve,
                       int position);

    void showAddUser(Reserve reserve, int position);

    void showDeleteUser(Reserve reserve, int position);

    void showImHost();

    void showImInReserve();
}