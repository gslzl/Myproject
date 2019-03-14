package com.example.myproject.bean;

import java.io.Serializable;
import java.util.List;

public class FavoritesBean implements Serializable {
    public String code;
    public String message;
    private List<favoritesBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<favoritesBean> getData() {
        return data;
    }

    public void setData(List<favoritesBean> data) {
        this.data = data;
    }
    public static class favoritesBean implements Serializable{

        public String product_name;
        public String current_price;
        public String picture;
        public String information;
        public String state;
        public String price;

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getCurrent_price() {
            return current_price;
        }

        public void setCurrent_price(String current_price) {
            this.current_price = current_price;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getInformation() {
            return information;
        }

        public void setInformation(String information) {
            this.information = information;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }


}
