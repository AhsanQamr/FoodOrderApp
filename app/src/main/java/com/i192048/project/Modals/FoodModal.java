package com.i192048.project.Modals;

public class FoodModal {
    String f_name,f_price,f_description,f_image;

    public FoodModal(String f_name, String f_price, String f_description, String f_image) {
        this.f_name = f_name;
        this.f_price = f_price;
        this.f_description = f_description;
        this.f_image = f_image;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getF_price() {
        return f_price;
    }

    public void setF_price(String f_price) {
        this.f_price = f_price;
    }

    public String getF_description() {
        return f_description;
    }

    public void setF_description(String f_description) {
        this.f_description = f_description;
    }

    public String getF_image() {
        return f_image;
    }

    public void setF_image(String f_image) {
        this.f_image = f_image;
    }
}
