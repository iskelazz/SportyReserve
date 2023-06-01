package es.udc.psi.controller.impl;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import es.udc.psi.R;
import es.udc.psi.controller.interfaces.BookController;
import es.udc.psi.model.Reserve;

import es.udc.psi.repository.impl.BookRepositoryImpl;
import es.udc.psi.repository.interfaces.BookRepository;
import es.udc.psi.utils.ResourceDemocratizator;
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
            view.showValidationError("name", ResourceDemocratizator.getInstance().getStringFromResourceID(R.string.ValError_NameIsEmpty));
        }
        else
        {
            view.clearValidationError("name");
        }
        if (fecha.compareTo(Calendar.getInstance().getTime()) < 0)
        {
            System.out.println(String.format("fecha before: %s vs %s", fecha.toString(), Calendar.getInstance().getTime().toString()));
            view.showValidationError("date", ResourceDemocratizator.getInstance().getStringFromResourceID(R.string.ValError_ReserveDateIsPast));
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
            view.showValidationError("time", ResourceDemocratizator.getInstance().getStringFromResourceID(R.string.ValError_InvalidTimetable));
            isValid = false;
        }
        else
        {
            view.clearValidationError("time");
        }
        if (!isPublic && password != null && password.length() < 8) {
            view.showValidationError("password", ResourceDemocratizator.getInstance().getStringFromResourceID(R.string.ValError_InvalidPasswordLength));
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
                view.showValidationError("reserve", ResourceDemocratizator.getInstance().getStringFromResourceID(R.string.ValError_CollisionOnTimeWithPreviousReserve));
            }

            @Override
            public void onNotExists() {
                // WARNING - Hay que cambiar todoo este código por algo relacionado con BBDD
                bookRepository.createReserve(book, new BookRepository.OnBookCreatedListener() {
                    @Override
                    public void onSuccess(Reserve reserve) {
                        view.onBookSuccess(reserve);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        view.onBookFailure(ResourceDemocratizator.getInstance().getStringFromResourceID(R.string.Failure_AddingReserve) + errorMessage);
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                view.onBookFailure(ResourceDemocratizator.getInstance().getStringFromResourceID(R.string.Failure_ErrorCheckingPreviousReserves) + errorMessage);
            }
        });
    }


    @Override
    public void fetchFilteredReserves(String nameCourt, String nameSport, Calendar startDate, Calendar endDate , BookRepository.OnFilteredReservesFetchedListener listener) {

            bookRepository.getFilteredReserves(nameCourt, nameSport, startDate, endDate, new BookRepository.OnFilteredReservesFetchedListener() {
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

}
