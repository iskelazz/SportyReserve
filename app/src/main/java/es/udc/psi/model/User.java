package es.udc.psi.model;

public class User {

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
}