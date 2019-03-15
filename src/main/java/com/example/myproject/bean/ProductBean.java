package com.example.myproject.bean;

import java.io.Serializable;

public class ProductBean implements Serializable {
    public String code;
    private productBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public productBean getData() {
        return data;
    }

    public void setData(productBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String message;
    public static class productBean implements Serializable{
        public String ID;
        public String latitude;
        public String longitude;

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

        public String getStart_price() {
            return start_price;
        }

        public void setStart_price(String start_price) {
            this.start_price = start_price;
        }

        public String name;
        public String user_ID;//发布的
        public String picture;
        public String information;
        public String start_price;
        public String current_price;
        public String time;
        public String state;//商品状态
        public String person_number;//参与竞拍人数
        public String price;
        public String end_time;
        public String buy_user;//现出价最高的
        public String catalog;
        public String scope;//竞价幅度

        public String getPerson_number() {
            return person_number;
        }

        public void setPerson_number(String person_number) {
            person_number = person_number;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getBuy_user() {
            return buy_user;
        }

        public void setBuy_user(String buy_user) {
            this.buy_user = buy_user;
        }

        public String getCatalog() {
            return catalog;
        }

        public void setCatalog(String catalog) {
            this.catalog = catalog;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUser_ID() {
            return user_ID;
        }

        public void setUser_ID(String user_ID) {
            this.user_ID = user_ID;
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

        public String getStar_price() {
            return start_price;
        }

        public void setStar_price(String star_price) {
            this.start_price = star_price;
        }

        public String getCurrent_price() {
            return current_price;
        }

        public void setCurrent_price(String current_price) {
            this.current_price = current_price;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
}
