package com.lashchenov.contactsListApp.pojo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class User implements Serializable {

    private int id;
    private String name, email, company, phone;
    private Boolean active;
    private int age;
    private EyeColor eyeColor;
    private FavoriteFruit favoriteFruit;
    private Double latitude, longitude;
    private String about;
    private List<User> friends;
    private String registered;

    public User() {
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public String getAbout() {
        return about;
    }

    public String getCompany() {
        return company;
    }

    public String getPhone() {
        return phone;
    }

    public Boolean getActive() {
        return active;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public EyeColor getEyeColor() {
        return eyeColor;
    }

    public FavoriteFruit getFavoriteFruit() {
        return favoriteFruit;
    }

    public String getRegistered() {
        return registered;
    }

    public List<User> getFriends() {
        return friends;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setEyeColor(EyeColor eyeColor) {
        this.eyeColor = eyeColor;
    }

    public void setFavoriteFruit(FavoriteFruit favoriteFruit) {
        this.favoriteFruit = favoriteFruit;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                age == user.age &&
                Objects.equals(name, user.name) &&
                Objects.equals(email, user.email) &&
                Objects.equals(company, user.company) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(active, user.active) &&
                eyeColor == user.eyeColor &&
                favoriteFruit == user.favoriteFruit &&
                Objects.equals(latitude, user.latitude) &&
                Objects.equals(longitude, user.longitude) &&
                Objects.equals(about, user.about) &&
                Objects.equals(friends, user.friends) &&
                Objects.equals(registered, user.registered);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
