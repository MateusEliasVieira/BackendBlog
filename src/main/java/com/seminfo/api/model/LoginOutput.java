package com.seminfo.api.model;

public class LoginOutput {
    private Long idUser;
    private String token;

    public LoginOutput() {
    }

    public LoginOutput(Long idUser, String token) {
        this.idUser = idUser;
        this.token = token;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
