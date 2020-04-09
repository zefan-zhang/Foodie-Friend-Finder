package edu.neu.foodiefriendfinder.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YelpRestaurant {
    @SerializedName("rating")
    @Expose
    public double rating;
    @SerializedName("price")
    @Expose
    public String price;
    @SerializedName("phone")
    @Expose
    public String phone;
    @SerializedName("is_closed")
    @Expose
    public Boolean isClosed;
    @SerializedName("categories")
    @Expose
    public List<YelpRestaurantCategory> categories = null;
    @SerializedName("review_count")
    @Expose
    public Integer reviewCount;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("coordinates")
    @Expose
    public YelpRestaurantCoor coordinates;
    @SerializedName("image_url")
    @Expose
    public String imageUrl;
    @SerializedName("location")
    @Expose
    public YelpRestaurantLocation location;
    @SerializedName("distance")
    @Expose
    public Double distanceInMeters;

    public YelpRestaurant() {
    }

    public String meterToMile(){
        double mi = distanceInMeters * 0.00062137;
        return String.format("%.1f", mi) + " Mi";
    }

    public String getRestaurantName() {
        return this.name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getClosed() {
        return isClosed;
    }

    public void setClosed(Boolean closed) {
        isClosed = closed;
    }

    public List<YelpRestaurantCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<YelpRestaurantCategory> categories) {
        this.categories = categories;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public void setRestaurantName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public YelpRestaurantCoor getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(YelpRestaurantCoor coordinates) {
        this.coordinates = coordinates;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public YelpRestaurantLocation getLocation() {
        return location;
    }

    public void setLocation(YelpRestaurantLocation location) {
        this.location = location;
    }

    public Double getDistanceInMeters() {
        return distanceInMeters;
    }

    public void setDistanceInMeters(Double distanceInMeters) {
        this.distanceInMeters = distanceInMeters;
    }
}
