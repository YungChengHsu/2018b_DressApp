package com.technion.rbd.dressapp.FrontEnd;

import java.util.Locale;

public class Measurement {
    private String category;
    private double dress_chest;
    private double dress_waist;
    private double dress_hips;
    private String top_size;
    private double pants_size;
    private double shoes_size;

    /*
    Tops: XS,S,M,L,XL
    Pants: inches
    Dresses: Waist,bust,hips
    Shoes: cm
     */

    public Measurement() {
        this.category = "none";
        this.dress_chest = -1;
        this.dress_waist = -1;
        this.dress_hips = -1;
        this.top_size = "none";
        this.pants_size = -1;
        this.shoes_size = -1;
    }

    public Measurement(String category, String top_size) {
        this.category = category;
        this.top_size = top_size;
    }

    public Measurement(String category, double size) {
        this.category = category;
        if (category.equals("Shoes")) {
            this.shoes_size = size;
        } else {
            this.pants_size = size;
        }
    }

    public Measurement(String category, double dress_chest, double dress_waist, double dress_hips) {
        this.category = category;
        this.dress_chest = dress_chest;
        this.dress_waist = dress_waist;
        this.dress_hips = dress_hips;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getDress_chest() {
        return dress_chest;
    }

    public void setDress_chest(double dress_chest) {
        this.dress_chest = dress_chest;
    }

    public double getDress_waist() {
        return dress_waist;
    }

    public void setDress_waist(double dress_waist) {
        this.dress_waist = dress_waist;
    }

    public double getDress_hips() {
        return dress_hips;
    }

    public void setDress_hips(double dress_hips) {
        this.dress_hips = dress_hips;
    }

    public String getTop_size() {
        return top_size;
    }

    public void setTop_size(String top_size) {
        this.top_size = top_size;
    }

    public double getPants_size() {
        return pants_size;
    }

    public void setPants_size(double pants_size) {
        this.pants_size = pants_size;
    }

    public double getShoes_size() {
        return shoes_size;
    }

    public void setShoes_size(double shoes_size) {
        this.shoes_size = shoes_size;
    }
}

