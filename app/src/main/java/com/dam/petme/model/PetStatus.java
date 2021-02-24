package com.dam.petme.model;

public enum PetStatus {

    LOST,
    FOUND,
    ADOPTED,
    TO_BE_ADOPTED;

    public String toStringPlural(){
        if(this == LOST) return "Perdidas";
        if(this == FOUND) return "Encontradas";
        if(this == ADOPTED) return "Adoptadas";
        return "En adopción";
    }

    public String toString(){
        if(this == LOST) return "Perdida";
        if(this == FOUND) return "Encontrada";
        if(this == ADOPTED) return "Adoptada";
        return "En adopción";
    }

    public String toStringPast(){
        if(this == LOST) return "Perdió";
        if(this == FOUND) return "Encontró";
        return "Encuentra";
    }

    public String toStringEnglish(){
        if(this == LOST) return "LOST";
        if(this == FOUND) return "FOUND";
        if(this == ADOPTED) return "ADOPTED";
        return "TO_BE_ADOPTED";
    }

}