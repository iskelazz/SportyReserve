package es.udc.psi.controller.interfaces;


import es.udc.psi.model.Reserve;

public interface BookController {

    // TODO Se necesita meter el anfitrion para validar?
    void validateAndRegister(Reserve book);

    //void validateAndRegister(String password, String anfitrion, Date fecha, int duracion);
}
