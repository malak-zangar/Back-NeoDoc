package com.example.backneodoc.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ResetRequest {
    @NotBlank
    @Size(min = 8, max = 40)
    private String password;


    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
