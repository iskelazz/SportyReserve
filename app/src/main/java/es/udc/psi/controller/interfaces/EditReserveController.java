package es.udc.psi.controller.interfaces;

import es.udc.psi.model.Reserve;

public interface EditReserveController {

    /**
     * @param book The book to be validated then updated in the DB
     * */
    void validateAndUpdate(Reserve book);
}
