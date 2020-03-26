package edu.neu.foodiefriendfinder.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class User {

    public String userId;
    public String firstName;
    public String lastName;
    public String phone;
    public List<String> cuisine;
    public String dob;
    public List<String> languages;
    public String gender;

    public User() {
    }

    public User(String userId, String firstName, String lastName, String phone,
                String dob, String gender) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.cuisine = cuisine;
        this.dob = dob;
        this.languages = languages;
        this.gender = gender;
    }

    public User(String userId, String firstName, String lastName, String phone,
                List<String> cuisine, String dob, List<String> languages, String gender) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.cuisine = cuisine;
        this.dob = dob;
        this.languages = languages;
        this.gender = gender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getCuisine() {
        return cuisine;
    }

    public void setCuisine(List<String> cuisine) {
        this.cuisine = cuisine;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void addLanguage(String language) {
        this.languages.add(language);
    }

    public void addCuisine(String cuisine) {
        this.cuisine.add(cuisine);
    }

    public void removeLanguage(String language) {
        this.languages.remove(language);
    }

    public void removeCuisine(String cuisine) {
        this.cuisine.remove(cuisine);
    }

}
