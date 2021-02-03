package com.dam.petme.model;

import java.util.Date;

public class User {
    private String lastName;
    private String name;
    private String email;
    private String password;
    private Date birthdate;
    private String province;
    private String city;
    //TODO Ver si ponemos ubicacion en mapa o ciudad y provincia


    public User() {
    }

    public User(String lastName, String name, String email, String password, Date birthdate, String province, String city) {
        this.lastName = lastName;
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthdate = birthdate;
        this.province = province;
        this.city = city;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
