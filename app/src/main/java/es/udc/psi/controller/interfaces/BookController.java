package es.udc.psi.controller.interfaces;

import java.util.Date;

import es.udc.psi.model.Reserva;

public interface BookController {

    // TODO Se necesita meter el anfitrion para validar?
    void validateAndRegister(Reserva book);

    //void validateAndRegister(String password, String anfitrion, Date fecha, int duracion);
}
