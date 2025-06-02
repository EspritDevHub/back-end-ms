package com.example.projectmanagement.Entities.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EtatTacheEnum {

    NON_COMMENCEE("Non commencée"),
    EN_COURS("En cours"),
    TERMINEE("Terminée");

    private final String label;

    EtatTacheEnum(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static EtatTacheEnum fromValue(String value) {
        for (EtatTacheEnum etat : EtatTacheEnum.values()) {
            if (etat.label.equalsIgnoreCase(value)) {
                return etat;
            }
        }
        throw new IllegalArgumentException("Unknown label: " + value);
    }
}
