package es.udc.psi.model;

import java.util.ArrayList;
import java.util.Date;

public class Reserve {

    private String id;

    private String anfitrion;

    private String password;

    private String pista;

    private int capacidadMax;

    private String deporte;

    private int numPlayers;

    private Date fecha;

    // Cantidad de "lapsos" de tiempo que se reserva la pista
    // 0 <= duracion <= X
    private int duracion;

    private ArrayList<Usuario> playerList;

    public Reserve(String id,
                   String anfitrion,
                   String password,
                   String pista,
                   int capacidadMax,
                   String deporte,
                   int numPlayers,
                   Date fecha,
                   int duracion,
                   ArrayList<Usuario> playerList) {

        this.id = id;
        this.anfitrion = anfitrion;
        this.password = password;
        this.pista = pista;
        this.capacidadMax = capacidadMax;
        this.deporte = deporte;
        this.numPlayers = numPlayers;
        this.fecha = fecha;
        this.duracion = duracion;
        this.playerList = playerList;
    }

    public Reserve() {

    }

    public Reserve(String id,
                   String anfitrion,
                   String pista,
                   int capacidadMax,
                   String deporte,
                   int numPlayers,
                   Date fecha,
                   int duracion,
                   ArrayList<Usuario> playerList) {

        this.id = id;
        this.anfitrion = anfitrion;
        this.pista = pista;
        this.capacidadMax = capacidadMax;
        this.deporte = deporte;
        this.numPlayers = numPlayers;
        this.fecha = fecha;
        this.duracion = duracion;
        this.playerList = playerList;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getAnfitrion() {

        return anfitrion;
    }

    public void setAnfitrion(String anfitrion) {

        this.anfitrion = anfitrion;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public String getPista() {

        return pista;
    }

    public void setPista(String pista) {

        this.pista = pista;
    }

    public int getCapacidadMax() {

        return capacidadMax;
    }

    public void setCapacidadMax(int capacidadMax) {

        this.capacidadMax = capacidadMax;
    }

    public String getDeporte() {

        return deporte;
    }

    public void setDeporte(String deporte) {

        this.deporte = deporte;
    }

    public int getNumPlayers() {

        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {

        this.numPlayers = numPlayers;
    }

    public Date getFecha() {

        return fecha;
    }

    public void setFecha(Date fecha) {

        this.fecha = fecha;
    }

    public int getDuracion() {

        return duracion;
    }

    public void setDuracion(int duracion) {

        this.duracion = duracion;
    }

    public ArrayList<Usuario> getPlayerList() {

        return playerList;
    }

    public void setPlayerList(ArrayList<Usuario> playerList) {

        this.playerList = playerList;
    }
}
