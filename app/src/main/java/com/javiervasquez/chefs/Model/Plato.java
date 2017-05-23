package com.javiervasquez.chefs.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by javiervasquez on 19/05/17.
 */

@IgnoreExtraProperties
public class Plato implements Serializable {
    private String name;
    private String description;
    private String ingredients;
    private String dateAndHour;
    private String url_img;
    private String quantity;
    private String chef_id;
    private int state;
    private String id;
    private int price;
    private String buyer_id;
    private double score;
    private Score specificScore;
    private String address;
    private String user_name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setDateAndHour(String dateAndHour) {
        this.dateAndHour = dateAndHour;
    }

    public String getDateAndHour() {
        return dateAndHour;
    }

    public String getUrl_img() {
        return url_img;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setChef_id(String chef_id) {
        this.chef_id = chef_id;
    }

    public String getChef_id() {
        return chef_id;
    }


    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public void setSpecificScore(Score specificScore) {
        this.specificScore = specificScore;
    }

    public Score getSpecificScore() {
        return specificScore;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name() {
        return user_name;
    }
}
