package es.udc.psi.controller.impl;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import es.udc.psi.controller.interfaces.BookController;
import es.udc.psi.model.Reserve;
import es.udc.psi.model.TimeShot;
import es.udc.psi.repository.impl.BookRepositoryImpl;
import es.udc.psi.repository.interfaces.BookRepository;
import es.udc.psi.view.interfaces.BookView;

public class BookControllerImpl implements BookController {

    private BookView view;
    private final FirebaseAuth mAuth;
    private final BookRepositoryImpl bookRepository;


    public BookControllerImpl() {
        mAuth = FirebaseAuth.getInstance();
        bookRepository = new BookRepositoryImpl();
    }

    public BookControllerImpl(BookView view) {
        this.view = view;
        mAuth = FirebaseAuth.getInstance();
        bookRepository = new BookRepositoryImpl();
    }

    @Override
    public void validateAndRegister(Reserve book) {
        boolean isValid = validateFields(book.isPublic(), book.getName(), book.getPassword(), book.getDuracion(), book.getFecha());

        if (isValid) {
            createReserve(book);
        }
    }

    @Override
    public void fetchHostReserves(String hostId, BookRepository.OnReservesFetchedListener listener) {
        bookRepository.getHostReserves(hostId, new BookRepository.OnReservesFetchedListener() {
            @Override
            public void onFetched(ArrayList<Reserve> reserves) {
                listener.onFetched(reserves);
            }

            @Override
            public void onFailure(String errorMsg) {
                listener.onFailure(errorMsg);
            }
        });
    }

    @Override
    public void fetchPlayerReserves(String playerId, BookRepository.OnPlayerReservesFetchedListener listener) {
        bookRepository.getPlayerReserves(playerId, new BookRepository.OnPlayerReservesFetchedListener() {
            @Override
            public void onFetched(ArrayList<Reserve> reserves) {
                listener.onFetched(reserves);
            }

            @Override
            public void onFailure(String errorMsg) {
                listener.onFailure(errorMsg);
            }
        });
    }

    @Override
    public void deleteReserve(String reserveId, BookRepository.OnBookDeletedListener listener) {
        bookRepository.deleteReserve(reserveId, new BookRepository.OnBookDeletedListener() {
            @Override
            public void onSuccess() {
                listener.onSuccess();
            }

            @Override
            public void onFailure(String errorMessage) {
                listener.onFailure(errorMessage);
            }
        });
    }

    private boolean validateFields(Boolean isPublic, String name, String password, int duracion, Date fecha) {
        boolean isValid = true;

        if(name != null)
        {
            isValid = (name != "");
            view.showValidationError("name", "La reserva debe tener un nombre");
        }
        else
        {
            view.clearValidationError("name");
        }
        if (fecha.compareTo(Calendar.getInstance().getTime()) < 0)
        {
            System.out.println(String.format("fecha before: %s vs %s", fecha.toString(), Calendar.getInstance().getTime().toString()));
            view.showValidationError("date", "La fecha de la reserva debe ser posterior a la actual");
            isValid = false;
        }
        else
        {
            view.clearValidationError("date");
        }
        int aux = fecha.getHours();
        if (aux < 8 || aux > 20)
        {
            System.out.println("wrong time");
            view.showValidationError("time", "La hora no está dentro del horario apto para reservar");
            isValid = false;
        }
        else
        {
            view.clearValidationError("time");
        }
        if (!isPublic && password != null && password.length() < 8) {
            view.showValidationError("password", "La contraseña debe tener al menos 8 caracteres");
            isValid = false;
        } else {
            view.clearValidationError("password");
        }



        return isValid;
    }


    private void createReserve(Reserve book) {
        bookRepository.checkBookCoincidences(book, new BookRepository.OnBookCoincidencesCheckedListener() {
            @Override
            public void onExists() {
                view.showValidationError("reserve", "La reserva coincide en horario con alguna ya existente.");
            }

            @Override
            public void onNotExists() {
                // WARNING - Hay que cambiar todoo este código por algo relacionado con BBDD
                bookRepository.createReserve(book, new BookRepository.OnBookCreatedListener() {
                    @Override
                    public void onSuccess() {
                        view.onBookSuccess();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        view.onBookFailure("Error al añadir la reserva: " + errorMessage);
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                view.onBookFailure("Error al comprobar las reservas existentes: " + errorMessage);
            }
        });
    }
}
