package es.udc.psi.controller.impl;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import es.udc.psi.controller.interfaces.ReservesListController;
import es.udc.psi.model.Reserve;
import es.udc.psi.model.User;
import es.udc.psi.repository.impl.BookRepositoryImpl;
import es.udc.psi.repository.impl.UserRepositoryImpl;
import es.udc.psi.repository.interfaces.BookRepository;
import es.udc.psi.repository.interfaces.UserRepository;
import es.udc.psi.view.interfaces.ReservesListView;

public class ReservesListControllerImpl implements ReservesListController {

    private ReservesListView mReserveListView;

    private BookRepositoryImpl mBookRepository;
    private UserRepository mUserRepository;//TODO::???

    public ReservesListControllerImpl(ReservesListView reservesListView) {

        mReserveListView = reservesListView;
        mBookRepository = new BookRepositoryImpl();
        mUserRepository=new UserRepositoryImpl();
    }

    @Override
    public void initFlow() {
        mBookRepository.getReserves(new BookRepository.OnReservesFetchedListener() {    //TODO:cambiar por reservas filtradas desde hoy hasta 1año, por ej???

            @Override
            public void onFetched(ArrayList<Reserve> reservesList) {
                mReserveListView.showReservesList(reservesList);
            }

            @Override
            public void onFailure(String error) {

            }
        });

    }


    @Override
    public void onClickReserve() {

    }

    @Override
    public void onClickPlayer(Reserve reserve, int position) {
        if (reserve != null && reserve.getPlayerList() != null) {
            //if (position < reserve.getPlayerList().size())
            if (position < reserve.getNumPlayers()) {    // Seleccionado un jugador

                if (reserve.getPlayerList().get(position).getId().equals(mUserRepository.getCurrentUserId())) {   //Si el jugador seleccionado es el propio usuario
                    Log.d("TAG_GOL", "player pinchado (yo)??:" + reserve.getPlayerList().get(position).getNombre() + " ,  numplayers:  " + reserve.getNumPlayers());

                    deleteMeAsPlayer(reserve); //TODO:comprobar q no soy anfitrión
//mBookRepository.pruebaBorradolistausuarios(reserve.getId());
                } else {
                    //TODO: Opción DetailOtrosPlayers()????
                    Log.d("TAG_GOL", "player pinchado:" + reserve.getPlayerList().get(position).getNombre() + " ,  numplayers:  " + reserve.getNumPlayers());
                }

            } else if (position == reserve.getNumPlayers()) {    //Seleccionado para añadir al usuario como Nuevo Jugador
                //(position >= reserve.getNumPlayers()
                Log.d("TAG_GOL", "añadirme???:, position:" + position + " ,  numplayers:  " + reserve.getNumPlayers());
                //TODO:AlertDialogs???passwd si es privada???
                if (!reserve.getPlayerList().stream().anyMatch(player -> player.getId().equals(mUserRepository.getCurrentUserId()))) {
                    Log.d("TAG_GOL", "user:" + mUserRepository.getCurrentUserId() + "está en la reserva: " + reserve.getName());

                    addMeAsPlayer(reserve);
                } else {
                    //TODO:Aviso, ya estás en la reserva???
                }


            }
        }

        /*if (reserve != null && reserve.getPlayerList() != null) {
            if (reserve.getPlayerList().stream().anyMatch(player -> player.getId().equals(mUserRepository.getCurrentUserId()))) {
                Log.d("TAG_GOL","user:"+ mUserRepository.getCurrentUserId()+ "está en la reserva: "+reserve.getName());
            }
        }*/

    }

    @Override
    public void onClickAddNewPlayer() {

    }

    private void deleteMeAsPlayer(Reserve reserve){
        //TODO:
        // AlertDialog para Borrarse de la reserva??? (si es anfitrión????) mirar de userAdapter/btnExpulsar

        mUserRepository.getUser(mUserRepository.getCurrentUserId(), new UserRepository.OnUserFetchedListener() {

            @Override
            public void onFetched(User user) {
                ArrayList<User> newListUsers = reserve.getPlayerList();
                Log.d("TAG_DELETE", "newlistPlayers:"+newListUsers.toString());
                Log.d("TAG_DELETE", "user:"+user);
                newListUsers.remove(user);
                Log.d("TAG_DELETE", "newlistPlayers:"+newListUsers.toString());
                newListUsers.removeIf(user1 -> user1.getId().equals(user.getId()));
                Log.d("TAG_DELETE", "newlistPlayers:"+newListUsers.toString());
                mBookRepository.replaceUserListWithNew(reserve.getId(), newListUsers, new BookRepository.OnUserListUpdatedListener() {

                    @Override
                    public void onSuccess() {
                                /*// Muestra un Toast cuando el usuario se haya eliminado con éxito
                                Toast.makeText(context.getApplicationContext(), "Usuario expulsado con éxito.", Toast.LENGTH_SHORT).show();
                                // Actualizar la vista aquí. Por ejemplo, podrías querer actualizar una lista de usuarios.
                                if(listener != null) {
                                    listener.onUserExpelled();
                                }*/

                        //TODO:actualizar vista??                                mReserveListView.showReservesList();
                        initFlow();//TODO: de momento
                    }

                    @Override
                    public void onFailure(String errorMessage) {

                    }
                });
            }

            @Override
            public void onFailure(String error) {
                // Muestra un Toast cuando haya un error
                //TODO: Toast.makeText(context.getApplicationContext(), "Error al expulsar al usuario: " + errorMessage, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void addMeAsPlayer(Reserve reserve){

        mUserRepository.getUser(mUserRepository.getCurrentUserId(), new UserRepository.OnUserFetchedListener() {

            @Override
            public void onFetched(User user) {

                ArrayList<User> newListUsers = reserve.getPlayerList();
                newListUsers.add(user); //TODO:Comprobar q no esté en
                mBookRepository.replaceUserListWithNew(reserve.getId(), newListUsers, new BookRepository.OnUserListUpdatedListener() {

                    @Override
                    public void onSuccess() {
                                /*// Muestra un Toast cuando el usuario se haya añadido con éxito
                                Toast.makeText(context.getApplicationContext(), "Usuario expulsado con éxito.", Toast.LENGTH_SHORT).show();
                                // Actualizar la vista aquí. Por ejemplo, podrías querer actualizar una lista de usuarios.
                                if(listener != null) {
                                    listener.onUserExpelled();
                                }*/

                        //TODO:actualizar vista??                                mReserveListView.showReservesList();
                        initFlow();//TODO: de momento
                    }

                    @Override
                    public void onFailure(String errorMessage) {

                    }
                });
            }

            @Override
            public void onFailure(String error) {
                // Muestra un Toast cuando haya un error
                //TODO: Toast.makeText(context.getApplicationContext(), "Error al expulsar al usuario: " + errorMessage, Toast.LENGTH_SHORT).show();

            }
        });

    }
}
