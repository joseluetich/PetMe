package com.dam.petme.model;

public enum Gender {

    FEMALE,
    MALE,
    UNKNOWN;

    public String toString(){
        if(this == FEMALE) return "Hembra";
        if(this == MALE) return "Macho";
        return "Desconocido";
    }
}
