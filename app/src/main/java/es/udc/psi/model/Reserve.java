package es.udc.psi.model;

import java.util.ArrayList;
import java.util.Date;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Reserve implements Parcelable {

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

    private ArrayList<User> playerList;

    public Reserve(String id,
                   String anfitrion,
                   String password,
                   String pista,
                   int capacidadMax,
                   String deporte,
                   int numPlayers,
                   Date fecha,
                   int duracion,
                   ArrayList<User> playerList) {

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
                   ArrayList<User> playerList) {

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

    protected Reserve(Parcel in) {
        id = in.readString();
        anfitrion = in.readString();
        password = in.readString();
        pista = in.readString();
        capacidadMax = in.readInt();
        deporte = in.readString();
        numPlayers = in.readInt();
        long tmpFecha = in.readLong();
        fecha = tmpFecha != -1 ? new Date(tmpFecha) : null;
        duracion = in.readInt();
        if (in.readByte() == 0x01) {
            playerList = new ArrayList<User>();
            in.readList(playerList, User.class.getClassLoader());
        } else {
            playerList = null;
        }
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

    public ArrayList<User> getPlayerList() {

        return playerList;
    }

    public void setPlayerList(ArrayList<User> playerList) {

        this.playerList = playerList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(anfitrion);
        dest.writeString(password);
        dest.writeString(pista);
        dest.writeInt(capacidadMax);
        dest.writeString(deporte);
        dest.writeInt(numPlayers);
        dest.writeLong(fecha != null ? fecha.getTime() : -1L);
        dest.writeInt(duracion);
        if (playerList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(playerList);
        }
    }

    public static final Creator<Reserve> CREATOR = new Creator<Reserve>() {
        @Override
        public Reserve createFromParcel(Parcel in) {
            return new Reserve(in);
        }

        @Override
        public Reserve[] newArray(int size) {
            return new Reserve[size];
        }
    };
}
