package es.udc.psi.controller.impl;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;

import es.udc.psi.controller.interfaces.BookController;
import es.udc.psi.model.Reserve;
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


    @Override
    public void validateAndRegister(Reserve book) {
        boolean isValid = validateFields(book.getPassword(), book.getDuracion(), book.getFecha());

        if (isValid) {
            createBook(book);
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

    private boolean validateFields(String password, int duracion, Date fecha) {
        boolean isValid = true;

        if (password.length() < 8) {
            view.showValidationError("password", "La contraseña debe tener al menos 8 caracteres");
            isValid = false;
        } else {
            view.clearValidationError("password");
        }

        if (duracion < 1) {
            view.showValidationError("duracion", "La duración debe tener al menos un valor de 1");
            isValid = false;
        } else {
            view.clearValidationError("duracion");
        }

        return isValid;
    }


    private void createBook(Reserve book) {
        bookRepository.checkBookCoincidences(book, new BookRepository.OnBookCoincidencesCheckedListener() {
            @Override
            public void onExists() {
                view.showValidationError("username", "Username is already taken.");
            }

            @Override
            public void onNotExists() {
                // WARNING - Hay que cambiar todoo este código por algo relacionado con BBDD
                /*
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userId = mAuth.getCurrentUser().getUid();
                            Usuario usuario = new Usuario(userId, firstName, email, password, phone, lastName);

                            userRepository.createUser(usuario, new UserRepository.OnUserCreatedListener() {
                                @Override
                                public void onSuccess() {
                                    view.onRegisterSuccess();
                                }

                                @Override
                                public void onFailure(String errorMessage) {
                                    view.onRegisterFailure("Error al registrar el usuario: " + errorMessage);
                                }
                            });
                        } else {
                            view.onRegisterFailure("Error al crear el usuario: " + task.getException().getMessage());
                        }
                    }
                });
                */
            }

            @Override
            public void onFailure(String errorMessage) {
                view.onBookFailure("Error al comprobar las reservas existentes: " + errorMessage);
            }
        });
    }
}
