package com.example.projectmanagement.Entities.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;



public enum EtapeProjetEnum {
    PLANIFICATION,
    REALISATION,
    CLOTURE,
    ETUDE;

    @JsonCreator
    public static EtapeProjetEnum fromString(String value) {
        try {
            return EtapeProjetEnum.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid EtapeProjetEnum value: " + value);
        }
    }
}
