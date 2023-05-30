package es.udc.psi.controller.impl;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import es.udc.psi.controller.interfaces.ReservesListController;
import es.udc.psi.model.Reserve;
import es.udc.psi.model.User;
import es.udc.psi.repository.impl.BookRepositoryImpl;
import es.udc.psi.repository.impl.UserRepositoryImpl;
import es.udc.psi.repository.interfaces.BookRepository;
import es.udc.psi.repository.interfaces.UserRepository;
import es.udc.psi.view.activities.ReservesListActivity;
import es.udc.psi.view.interfaces.ReservesListView;

public class ReservesListControllerImpl implements ReservesListController {

    private final ReservesListView mReserveListView;

    private final BookRepositoryImpl mBookRepository;
    private final UserRepository mUserRepository;

    public ReservesListControllerImpl(ReservesListView reservesListView) {

        mReserveListView = reservesListView;
        mBookRepository = new BookRepositoryImpl();
        mUserRepository = new UserRepositoryImpl();
    }

    @Override
    public void initFlow() {

        Calendar today = new GregorianCalendar();
        Calendar endDate = new GregorianCalendar();
        endDate.add(Calendar.YEAR,1);           //Inicialmente todas las reservas desde hoy en un año

        mBookRepository.getFilteredReserves(ReservesListActivity.ALL_COURTS_AND_SPORTS,
                ReservesListActivity.ALL_COURTS_AND_SPORTS,
                today,
                endDate,
                new BookRepository.OnFilteredReservesFetchedListener(){

                    @Override
                    public void onFetched(ArrayList<Reserve> reservesList) {
                        mReserveListView.showReservesList(reservesList);
                    }

                    @Override
                    public void onFailure(String error) {

                    }
                });
/*
        mBookRepository.getReserves(new BookRepository.OnReservesFetchedListener() {    //TODO:cambiar por reservas filtradas desde hoy hasta 1año, por ej???

            @Override
            public void onFetched(ArrayList<Reserve> reservesList) {
                mReserveListView.showReservesList(reservesList);
            }

            @Override
            public void onFailure(String error) {

            }
        });
*/
    }


    @Override
    public void onClickReserve() {

    }

    @Override
    public void onClickPlayer(Reserve reserve, int position) {
        if (reserve != null && reserve.getPlayerList() != null) {

            //if (position < reserve.getNumPlayers()) {    // Seleccionado un jugador   //TODO:???Comprobar si  BookRepository.replaceUserListWithNew actualiza bien el numPlayers
            if (position < reserve.getPlayerList().size()) {    // Seleccionado un jugador

                if (reserve.getPlayerList().get(position).getId().equals(mUserRepository.getCurrentUserId())) {   //Si el jugador seleccionado es el propio usuario

                    if (!reserve.getAnfitrion().equals(mUserRepository.getCurrentUserId())) {

                        mReserveListView.showDeleteUser(reserve,position);

                    } else {

                        mReserveListView.showImHost();
}
                } else {

                    //TODO: Opción DetailOtrosPlayers()????
                }

            } else if (position ==  reserve.getPlayerList().size()) {    //Seleccionado para añadir al usuario como Nuevo Jugador
                //(position >= reserve.getNumPlayers()

                if (reserve.getPlayerList().stream().noneMatch(player -> player.getId().equals(mUserRepository.getCurrentUserId()))) {

                    mReserveListView.showAddUser(reserve,position);

                } else {

                    mReserveListView.showImInReserve();
                }


            }
        }

    }


    @Override
    public void deleteMeAsPlayer(Reserve reserve, int position){

        mUserRepository.getUser(mUserRepository.getCurrentUserId(), new UserRepository.OnUserFetchedListener() {

            @Override
            public void onFetched(User user) {
                ArrayList<User> newListUsers = reserve.getPlayerList();

                newListUsers.removeIf(user1 -> user1.getId().equals(user.getId()));

                mBookRepository.replaceUserListWithNew(reserve.getId(), newListUsers, new BookRepository.OnUserListUpdatedListener() {

                    @Override
                    public void onSuccess() {
                                //TODO:??? Mostrar Toast (pasar contexto)

                        reserve.setPlayerList(newListUsers);
                        mReserveListView.updateReserveListView(reserve,position);

                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        //TODO:??? Mostrar Toast cuando haya error?? (pasar contexto)

                    }
                });
            }

            @Override
            public void onFailure(String error) {
                //TODO:??? Mostrar Toast cuando haya error?? (pasar contexto)

            }
        });

    }

    @Override
    public void addMeAsPlayer(Reserve reserve, int position){

        mUserRepository.getUser(mUserRepository.getCurrentUserId(), new UserRepository.OnUserFetchedListener() {

            @Override
            public void onFetched(User user) {

                ArrayList<User> newListUsers = reserve.getPlayerList();
                newListUsers.add(user);
                mBookRepository.replaceUserListWithNew(reserve.getId(), newListUsers, new BookRepository.OnUserListUpdatedListener() {

                    @Override
                    public void onSuccess() {

                        reserve.setPlayerList(newListUsers);
                        mReserveListView.updateReserveListView(reserve,position);
                    }

                    @Override
                    public void onFailure(String errorMessage) {

                    }
                });
            }

            @Override
            public void onFailure(String error) {

            }
        });

    }
}
