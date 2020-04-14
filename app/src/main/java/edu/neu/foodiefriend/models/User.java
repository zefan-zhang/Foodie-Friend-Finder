package edu.neu.foodiefriend.models;

import android.os.Build;

import androidx.annotation.Nullable;

import com.google.firebase.database.IgnoreExtraProperties;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@IgnoreExtraProperties
public class User {

    private String userId;
    private String firstName;
    private String lastName;
    private String phone;
    private List<String> cuisine;
    private String dob;
    private List<String> languages;
    private String gender;
    private String email;
    private boolean isOnline;
    private List<String> interestedRestaurants;
    private String interestedFoodie;

    public User() {
    }

    public User(String userId, String firstName, String lastName, String email, String phone,
                List<String> cuisine, String dob, List<String> languages, String gender) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.cuisine = cuisine;
        this.dob = dob;
        this.languages = languages;
        this.gender = gender;
        this.isOnline = false;
        this.interestedRestaurants = new ArrayList<String>();
        this.interestedFoodie = "";
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof User) {
            User user = (User) obj;
            return user.userId.equals(this.userId);
        }
        return false;
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

    public boolean getOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
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

    public String getEmail() {
        return email;
    }

    public void setEmaill(String email) {
        this.email = email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getInterestedRestaurants() {
        return interestedRestaurants;
    }

    public void setInterestedRestaurants(List<String> interestedRestaurants) {
        this.interestedRestaurants = interestedRestaurants;
    }

    public void setInterestedFoodie(String interestedFoodie) {
        this.interestedFoodie = interestedFoodie;
    }

    public String getAge() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy", Locale.US);
            LocalDate parsedBirthday = LocalDate.parse(getDob(), formatter);
            LocalDate currentDate = LocalDate.now();
            int ageInt = Period.between(parsedBirthday, currentDate).getYears();
            return Integer.toString(ageInt);
        } else {
            SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
            int age = 0;
            try {
                Date parsedDob = format.parse(getDob());
                // https://stackoverflow.com/questions/21242110/convert-java-util-date-to-java-time-localdate/27378709#27378709

                // https://stackoverflow.com/questions/31482267/get-age-of-person-with-java-threeten-bp
                Date currentDate = new Date();
                org.threeten.bp.LocalDate dateThreeTenFormat = Instant.ofEpochMilli(currentDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                org.threeten.bp.LocalDate convertedDob = Instant.ofEpochMilli(parsedDob.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                age = dateThreeTenFormat.getYear() - convertedDob.getYear();

                if (convertedDob.plusYears(age).isAfter(dateThreeTenFormat)) {
                    age--;
                }

            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            return Integer.toString(age);
        }
    }
}
