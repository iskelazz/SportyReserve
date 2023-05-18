package es.udc.psi.model;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class User implements Parcelable {

    private String id;
    private String nombre;
    private String correoElectronico;
    private String contraseña;
    private String telefono;
    private String apellidos;

    private String uriAvatar;

    public User() {

    }

    // Constructor con parámetros

    public User(String id,
                String nombre,
                String correoElectronico,
                String contraseña,
                String telefono,
                String apellidos) {

        this.id = id;
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
        this.contraseña = contraseña;
        this.telefono = telefono;
        this.apellidos = apellidos;
    }

    protected User(Parcel in) {
        id = in.readString();
        nombre = in.readString();
        correoElectronico = in.readString();
        contraseña = in.readString();
        telefono = in.readString();
        apellidos = in.readString();
        uriAvatar = in.readString();
    }
    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }

    public String getCorreoElectronico() {

        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {

        this.correoElectronico = correoElectronico;
    }

    public String getContraseña() {

        return contraseña;
    }

    public void setContraseña(String contraseña) {

        this.contraseña = contraseña;
    }

    public String getTelefono() {

        return telefono;
    }

    public void setTelefono(String telefono) {

        this.telefono = telefono;
    }

    public String getApellidos() {

        return apellidos;
    }

    public void setApellidos(String apellidos) {

        this.apellidos = apellidos;
    }

    public String getUriAvatar() {

        return uriAvatar;
    }

    public void setUriAvatar(String uriAvatar) {

        this.uriAvatar = uriAvatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nombre);
        dest.writeString(correoElectronico);
        dest.writeString(contraseña);
        dest.writeString(telefono);
        dest.writeString(apellidos);
        dest.writeString(uriAvatar);
    }

    // Este es el CREATOR requerido para Parcelable
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}