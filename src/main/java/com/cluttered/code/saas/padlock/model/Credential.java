package com.cluttered.code.saas.padlock.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author cluttered.code@gmail.com
 */
public class Credential {

    private final String email;
    private final String password;

    @JsonCreator
    public Credential(@JsonProperty("email") final String email,
                      @JsonProperty("password") final String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
