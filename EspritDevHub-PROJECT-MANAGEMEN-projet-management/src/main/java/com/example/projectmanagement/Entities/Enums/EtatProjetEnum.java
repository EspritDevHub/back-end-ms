package com.example.projectmanagement.Entities.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public enum EtatProjetEnum {
    EN_ATTENTE,
    EN_COURS,
    TERMINE,
    NON_COMMENCE;

    @JsonCreator
    public static EtatProjetEnum fromString(String value) {
        try {
            return EtatProjetEnum.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid EtatProjetEnum value: " + value);
        }
    }
}