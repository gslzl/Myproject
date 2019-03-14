package com.example.myproject.bean;

import java.io.Serializable;
import java.util.List;

public class IndentBean implements Serializable {
    public String code;
    public String message;
    private List<indentBean> data;

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

    public List<indentBean> getData() {
        return data;
    }

    public void setData(List<indentBean> data) {
        this.data = data;
    }
    public static class indentBean implements Serializable{
        public String name;
        public String product_ID;
        public String user_ID;
        public String time;//订单产生的时间
        public String price;//原价
        public String my_price;//我的出价
        public String number;//购买数量
        public String current_price;
        public String picture;
        public String information;
        public String product_state;//商品状态 0竞拍中 1拍卖结束
        public String state;//订单状态 0竞拍中 1待发货 2已完成

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String product_name) {
            this.name = product_name;
        }

        public String getProduct_ID() {
            return product_ID;
        }

        public void setProduct_ID(String product_ID) {
            this.product_ID = product_ID;
        }

        public String getUser_ID() {
            return user_ID;
        }

        public void setUser_ID(String user_ID) {
            this.user_ID = user_ID;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getMy_price() {
            return my_price;
        }

        public void setMy_price(String my_price) {
            this.my_price = my_price;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
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

        public String getProduct_state() {
            return product_state;
        }

        public void setProduct_state(String product_state) {
            this.product_state = product_state;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
}
