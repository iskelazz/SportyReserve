package es.udc.psi.controller.interfaces;

public interface BookController {
    void validateAndRegister(String email, String phone, String firstName, String lastName, String password, String username);
}
