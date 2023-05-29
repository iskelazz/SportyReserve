package es.udc.psi.controller.interfaces;

import es.udc.psi.model.Reserve;

public interface ReservesListController {

    void initFlow();

    void onClickReserve();

    void onClickPlayer(Reserve reserve, int position);

    void onClickAddNewPlayer();

}
