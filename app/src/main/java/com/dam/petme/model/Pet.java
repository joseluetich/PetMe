package com.dam.petme.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
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
    private String latitude;
    private String longitude;
    private String responsableId;

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
        result.put("id", id);
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
        result.put("latitude",latitude);
        result.put("longitude",longitude);
        result.put("responsable",responsableId);
        return result;
    }

    //@PropertyName("key")
    public String getId() {
        return id;
    }

    //@PropertyName("key")
    public void setId(String id) {
        System.out.println("id   "+id);
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
            this.province = province;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        if(latitude.equals("null"))
            latitude = null;
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        if(longitude.equals("null"))
            longitude = null;
        this.longitude = longitude;
    }

    @PropertyName("responsable")
    public String getResponsableId() {
        return responsableId;
    }

    @PropertyName("responsable")
    public void setResponsableId(String responsableId) {
        this.responsableId = responsableId;
    }
}