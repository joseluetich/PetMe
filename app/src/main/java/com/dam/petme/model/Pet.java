package com.dam.petme.model;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.Map;

public class Pet {

    private String id;
    private String name;
    private String race;
    private Float age;
    private Float weight;
    private String description;
    private Gender gender;
    private String province;
    private String city;
    private PetStatus status;
    private PetType type;
    private String profileImage;

    public Pet() {
    }

    public Pet(String name, String race, Float age, Float weight, String description, Gender gender, String province, String city, PetStatus status, PetType type) {
        this.name = name;
        this.race = race;
        this.age = age;
        this.weight = weight;
        this.description = description;
        this.gender = gender;
        this.province = province;
        this.city = city;
        this.status = status;
        this.type = type;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("race", race);
        result.put("type", type);
        result.put("gender", gender);
        result.put("province", province);
        result.put("weight", weight);
        result.put("status", status);
        result.put("city", city);
        result.put("description", description);
        result.put("profile_image", profileImage);

        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public Float getAge() {
        return age;
    }

    public void setAge(Float age) {
        this.age = age;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public PetStatus getStatus() {
        return status;
    }

    public void setStatus(PetStatus status) {
        this.status = status;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    @PropertyName("profile_image")
    public String getProfileImage() {
        return profileImage;
    }

    @PropertyName("profile_image")
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

}