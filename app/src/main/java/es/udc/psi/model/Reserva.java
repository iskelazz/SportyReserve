package es.udc.psi.model;

import java.util.Date;

public class Reserva {

    private String id;

    private String anfitrion;

    private String password;

    private String pista;

    private int capacidadMax;

    private String deporte;

    private Date fecha;

    // Cantidad de "lapsos" de tiempo que se reserva la pista
    // 0 <= duracion <= X
    private int duracion;

    public Reserva() {}

    public Reserva(String id, String anfitrion, String password, String pista, String deporte, int capacidadMax, Date fecha, int duracion)
    {
        this.id = id;
        this.anfitrion = anfitrion;
        this.password = password;
        this.pista = pista;
        this.capacidadMax = capacidadMax;
        this.deporte = deporte;
        this.fecha = fecha;
        this.duracion = duracion;
    }

    public Reserva(String id, String anfitrion, String pista, String deporte, int capacidadMax, int duracion)
    {
        this.id = id;
        this.anfitrion = anfitrion;
        this.pista = pista;
        this.deporte = deporte;
        this.capacidadMax = capacidadMax;
        this.duracion = duracion;
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
}

