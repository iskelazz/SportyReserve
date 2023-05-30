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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.udc.psi.model.Reserve;
import es.udc.psi.model.User;
import es.udc.psi.repository.interfaces.BookRepository;
import es.udc.psi.view.activities.ReservesListActivity;

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
        public void getFilteredReserves(String nameCourt, String nameSport, Calendar startDate, Calendar endDate, final OnFilteredReservesFetchedListener listener) {

        mDatabase.orderByChild("fecha/time").startAt(startDate.getTimeInMillis()).endAt(endDate.getTimeInMillis())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //TODO: if (dataSnapshot.exists()) { ??????Mirar/Comprobar????
                ArrayList<Reserve> reservesList = new ArrayList<>();

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    try {
                        Reserve reserve = childSnapshot.getValue(Reserve.class);

                        if (reserve != null){
                            if (nameSport.equals(ReservesListActivity.ALL_COURTS_AND_SPORTS)){
                                if (nameCourt.equals(ReservesListActivity.ALL_COURTS_AND_SPORTS)){
                                    reservesList.add(reserve);
                                } else if (reserve.getPista().equals(nameCourt)){
                                    reservesList.add(reserve);
                                }
                            } else if (reserve.getDeporte().equals(nameSport)){
                                if (nameCourt.equals(ReservesListActivity.ALL_COURTS_AND_SPORTS)){
                                    reservesList.add(reserve);
                                } else if (reserve.getPista().equals(nameCourt)){
                                    reservesList.add(reserve);
                                }
                            }
                        }

                    } catch (com.google.firebase.database.DatabaseException e) {
                            // Ignora esta reserva y continúa con la siguiente
                            continue;
                    }
                }
                listener.onFetched(reservesList);
/*
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
*/
//TODO:            } else {listener.onFailure("Error retrieving reserves from Reserve database");}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                listener.onFailure(databaseError.getMessage());
            }
        });

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
