package es.udc.psi.controller.interfaces;


import es.udc.psi.model.Reserve;
import es.udc.psi.repository.interfaces.BookRepository;

public interface BookController {

    // TODO Se necesita meter el anfitrion para validar?
    void validateAndRegister(Reserve book);
    void fetchHostReserves(String hostId, final BookRepository.OnReservesFetchedListener listener);
    void fetchPlayerReserves(String playerId, final BookRepository.OnPlayerReservesFetchedListener listener);
    void deleteReserve(String reserveId, BookRepository.OnBookDeletedListener listener);
    //void validateAndRegister(String password, String anfitrion, Date fecha, int duracion);
}
