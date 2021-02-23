package com.dam.petme.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.Map;

public class Pet implements Parcelable {

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
        result.put("latitude",latitude);
        result.put("longitude",longitude);
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
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    private Pet(Parcel in) {
        name = in.readString();
        race = in.readString();
        age = in.readFloat();
        weight = in.readFloat();
        description = in.readString();
        gender = Gender.valueOf(in.readString());
        province = in.readString();
        city = in.readString();
        status = PetStatus.valueOf(in.readString());
        profileImage = in.readString();
        type = PetType.valueOf(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(race);
        parcel.writeFloat(age);
        parcel.writeFloat(weight);
        parcel.writeString(description);
        parcel.writeString(gender.toString());
        parcel.writeString(province);
        parcel.writeString(city);
        parcel.writeString(status.toString());
        parcel.writeString(profileImage);
        parcel.writeString(type.toString());
    }

    public static final Creator<Pet> CREATOR = new Creator<Pet>() {
        @Override
        public Pet createFromParcel(Parcel in) {
            return new Pet(in);
        }

        @Override
        public Pet[] newArray(int size) {
            return new Pet[size];
        }
    };
}