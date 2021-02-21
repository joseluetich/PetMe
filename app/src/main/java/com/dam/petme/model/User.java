package com.dam.petme.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User {

    private String lastName;
    private String name;
    private String email;
    private String password;
    private Date birthday;
    private String province;
    private String city;
    private String photo;


    public User() {
    }

    public User(String lastName, String name, String email, String password, Date birthday, String province, String city) {
        this.lastName = lastName;
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.province = province;
        this.city = city;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("last_name", lastName);
        result.put("name", name);
        result.put("email", email);
        result.put("password", password);
        result.put("birthday", birthday.getTime());
        result.put("province", province);
        result.put("city", city);

        return result;
    }

    @PropertyName("last_name")
    public String getLastName() {
        return lastName;
    }

    @PropertyName("last_name")
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

    public Date getBirthday() {
        return birthday;
    }

    @Exclude
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = new Date(birthday);
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
