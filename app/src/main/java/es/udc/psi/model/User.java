package es.udc.psi.model;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class User implements Parcelable {

    private String id;
    private String nombre;
    private String correoElectronico;
    private String contraseña;
    private String telefono;
    private String apellidos;
    private String username;
    private Map<String, Notification> notifications = new HashMap<>();

    private String uriAvatar;

    public User() {

    }

    // Constructor con parámetros

    public User(String id,
                String nombre,
                String correoElectronico,
                String contraseña,
                String telefono,
                String apellidos,
                String username) {

        this.id = id;
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
        this.contraseña = contraseña;
        this.telefono = telefono;
        this.apellidos = apellidos;
        this.username = username;
    }

    protected User(Parcel in) {
        id = in.readString();
        nombre = in.readString();
        correoElectronico = in.readString();
        contraseña = in.readString();
        telefono = in.readString();
        apellidos = in.readString();
        username = in.readString();
        uriAvatar = in.readString();
        int size = in.readInt();
        for(int i = 0; i < size; i++){
            String key = in.readString();
            Notification value = in.readParcelable(Notification.class.getClassLoader());
            notifications.put(key, value);
        }
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
    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }
    public String getUriAvatar() {

        return uriAvatar;
    }

    public void setUriAvatar(String uriAvatar) {

        this.uriAvatar = uriAvatar;
    }
    public Map<String, Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Map<String, Notification> notifications) {
        this.notifications = notifications;
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
        dest.writeString(username);
        dest.writeString(uriAvatar);
        dest.writeInt(notifications.size());
        for(Map.Entry<String, Notification> entry : notifications.entrySet()){
            dest.writeString(entry.getKey());
            dest.writeParcelable(entry.getValue(), flags);
        }
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