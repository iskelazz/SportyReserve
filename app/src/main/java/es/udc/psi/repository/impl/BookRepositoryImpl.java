package es.udc.psi.repository.impl;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import es.udc.psi.model.Reserve;
import es.udc.psi.model.User;
import es.udc.psi.repository.interfaces.BookRepository;

public class BookRepositoryImpl implements BookRepository {
    private DatabaseReference mDatabase;

    public BookRepositoryImpl() {
        mDatabase = FirebaseDatabase.getInstance().getReference("Books");
    }

    @Override
    public void createBook(Reserve book, final OnBookCreatedListener listener) {
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
        mDatabase.orderByChild("pista").equalTo(book.getPista()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
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
}
