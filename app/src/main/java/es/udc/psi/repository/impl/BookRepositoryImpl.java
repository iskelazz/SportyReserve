package es.udc.psi.repository.impl;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.udc.psi.model.Reserve;
import es.udc.psi.model.User;
import es.udc.psi.repository.interfaces.BookRepository;

public class BookRepositoryImpl implements BookRepository {
    private DatabaseReference mDatabase;
    private DatabaseReference mSportsDB;
    private DatabaseReference mLocationDB;

    public BookRepositoryImpl() {
        mDatabase = FirebaseDatabase.getInstance().getReference("Books");
        mSportsDB = FirebaseDatabase.getInstance().getReference("Sports");
        mLocationDB = FirebaseDatabase.getInstance().getReference("Location");
    }

    @Override
    public void createReserve(Reserve book, final OnBookCreatedListener listener) {
        mDatabase.child(book.getId()).setValue(book)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }

    /* TODO - Nos interesa una función que nos diga si existe una reserva para una pista concreta
    *   que detecte si una reserva de esa misma pista o no, se entrelaza con la nueva candidata
    *   a reserva. Posiblemente haga falta otra para detectar que el mismo anfitrión no pueda
    *   tener otra reserva durante ese período (fecha_inicio * duración) */

    @Override
    public void checkBookCoincidences(Reserve book, final OnBookCoincidencesCheckedListener listener) {
        final Query aux = mDatabase.orderByChild("id");
        aux.equalTo(book.getPista()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //aux
                    listener.onExists();
                } else {
                    listener.onNotExists();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure(databaseError.getMessage());
            }
        });
    }

    public void getHostReserves(String hostId, final OnReservesFetchedListener listener) {
        mDatabase.orderByChild("anfitrion").equalTo(hostId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Reserve> reserves = new ArrayList<>();
                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                            reserves.add(childSnapshot.getValue(Reserve.class));
                        }
                        listener.onFetched(reserves);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listener.onFailure(databaseError.getMessage());
                    }
                });
    }

    public void getPlayerReserves(String playerId, final OnPlayerReservesFetchedListener listener) {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Reserve> reserves = new ArrayList<>();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    try {
                        Reserve reserve = childSnapshot.getValue(Reserve.class);
                        if (reserve != null && reserve.getPlayerList() != null) {
                            if (reserve.getPlayerList().stream().anyMatch(player -> player.getId().equals(playerId))) {
                                reserves.add(reserve);
                            }
                        }
                    } catch (com.google.firebase.database.DatabaseException e) {
                        // Falló la conversión, ignora esta reserva y continúa con la siguiente
                        continue;
                    }
                }
                listener.onFetched(reserves);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure(databaseError.getMessage());
            }
        });
    }
    @Override
    public void deleteReserve(String bookId, final OnBookDeletedListener listener) {
        mDatabase.child(bookId).removeValue()
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }

    @Override
    public void updateReserve(Reserve reserve, OnReserveUpdatedListener listener) {
        DatabaseReference bookRef = mDatabase.child(reserve.getId());

        bookRef.setValue(reserve)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }


    @Override
    public void replaceUserListWithNew(String bookId, List<User> newUserList, OnUserListUpdatedListener listener) {
        DatabaseReference bookRef = mDatabase.child(bookId);
        DatabaseReference playersRef = bookRef.child("playerList");
        DatabaseReference numPlayersRef = bookRef.child("numPlayers");  // referencia al campo numPlayers

        // Primero, elimina todos los datos existentes en playerList
        playersRef.setValue(null)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Si la eliminación de datos existentes fue exitosa, escribe la nueva lista de usuarios
                        if (task.isSuccessful()) {
                            // Crea un mapa para contener los datos de la lista de usuarios
                            Map<String, Object> userListMap = new HashMap<>();

                            // Rellena el mapa con los datos de los usuarios de newUserList
                            for (int i = 0; i < newUserList.size(); i++) {
                                userListMap.put(Integer.toString(i), newUserList.get(i));
                            }

                            // Escribe la nueva lista de usuarios en Firebase
                            playersRef.updateChildren(userListMap)
                                    .addOnSuccessListener(aVoid -> {
                                        // Actualiza el valor de numPlayers
                                        numPlayersRef.setValue(newUserList.size())
                                                .addOnSuccessListener(aVoid1 -> listener.onSuccess())
                                                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
                                    })
                                    .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
                        } else {
                            // Si hubo un error al eliminar los datos existentes, notifica al oyente
                            listener.onFailure(task.getException().getMessage());
                        }
                    }
                });
    }



    public void getReserve(String reserveId, OnReserveRetrievedListener listener) {
        mDatabase.child(reserveId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Reserve reserve = dataSnapshot.getValue(Reserve.class);
                if (reserve != null) {
                    listener.onSuccess(reserve);
                } else {
                    listener.onFailure("Reserve not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure(databaseError.getMessage());
            }
        });
    }


    @Override
    public ArrayList<Reserve> getReservesList() {

        //TODO: Datos mockeados
        User usuario = new User("1234", "Nombre player 1","mail@mail.com","passwd01","678123456","Perez Perez");
        User usuario2 = new User("1344", "Nombre player 2","mail2@mail.com","passwd01","675345345456","Perez Perez");
        usuario2.setUriAvatar("https://goo.gl/gEgYUd");
        //usuario.setUriAvatar("https://brickmarkt.com/19711-large_default/minifiguras-iron-man-minifigura-lego-marvel-super-heroes-sh065.jpg");
        usuario.setUriAvatar("https://firebasestorage.googleapis.com/v0/b/sportyreserve.appspot.com/o/lego-marvel-super-heroes.jpg?alt=media&token=8ea6a384-da6d-4d81-89f0-f5f7e003169c");

        User usuarioVacio = new User();usuarioVacio.setNombre("+New Player");


        ArrayList<User> listaPlayers2=new ArrayList<>();listaPlayers2.add(usuario);listaPlayers2.add(usuario2);listaPlayers2.add(usuarioVacio);listaPlayers2.add(usuario);
        ArrayList<User> listaPlayers=new ArrayList<>();listaPlayers.add(usuario);listaPlayers.add(usuario2);listaPlayers.add(usuarioVacio);listaPlayers.add(usuario);listaPlayers.add(usuarioVacio);listaPlayers.add(usuario2);listaPlayers.add(usuarioVacio);listaPlayers.add(usuarioVacio);listaPlayers.add(usuarioVacio);listaPlayers.add(usuario);
        ArrayList<User> listaPlayers3 = (ArrayList<User>) listaPlayers.clone();listaPlayers3.add(usuario);listaPlayers3.add(usuario2);listaPlayers3.add(usuarioVacio);listaPlayers3.add(usuario);listaPlayers3.add(usuarioVacio);listaPlayers3.add(usuario2);listaPlayers3.add(usuarioVacio);listaPlayers3.add(usuarioVacio);listaPlayers3.add(usuarioVacio);listaPlayers3.add(usuario);listaPlayers3.add(usuarioVacio);listaPlayers3.add(usuarioVacio);listaPlayers3.add(usuario);

        Reserve reserve1=new Reserve("2234","Nombre Anfitrion","Nombre de la pista",4,"Padel  ",4,new Date(),120,listaPlayers2);
        Reserve reserve2=new Reserve("67534","Nombre Anfitrion","Nombre de la pista2",10,"Basket  ",10,new Date(),120,listaPlayers);
        Reserve reserve3=new Reserve("67534","Nombre Anfitrion","Nombre de la pista2",23,"Basket  ",23,new Date(),120,listaPlayers3);

        ArrayList<Reserve> reservationmockList= new ArrayList<Reserve>();


        reservationmockList.add(reserve1);
        reservationmockList.add(reserve2);
        reservationmockList.add(reserve1);
        reservationmockList.add(reserve2);
        reservationmockList.add(reserve3);
        reservationmockList.add(reserve2);


        return reservationmockList;

        //    return null;
    }

    /**
     *
     */
    @Override
    public void getReserves(OnReservesFetchedListener listener)
    {
        Query aux = mDatabase.orderByChild("name");
        aux.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<Reserve> results = new ArrayList<>();
                    for (DataSnapshot data: dataSnapshot.getChildren())
                    {
                        results.add(data.getValue(Reserve.class));
                    }
                    listener.onFetched(results);
                } else {
                    listener.onFailure("Error retrieving reserves from Reserve database");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure(databaseError.getMessage());
            }
        });
    }
}
