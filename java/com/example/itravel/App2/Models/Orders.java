package com.example.itravel.App2.Models;

public class Orders {
        private String paytmOrderId,orderId,orderTime, orderDate,userName, address, phone, city,totalAmount,state,orderThrough;

        public Orders(){}

    public Orders(String paytmOrderId, String orderId, String orderTime, String orderDate, String userName, String address, String phone, String city, String totalAmount, String state, String orderThrough) {
        this.paytmOrderId = paytmOrderId;
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.orderDate = orderDate;
        this.userName = userName;
        this.address = address;
        this.phone = phone;
        this.city = city;
        this.totalAmount = totalAmount;
        this.state = state;
        this.orderThrough = orderThrough;
    }

    public String getOrderThrough() {
        return orderThrough;
    }

    public void setOrderThrough(String orderThrough) {
        this.orderThrough = orderThrough;
    }

    public String getPaytmOrderId() {
        return paytmOrderId;
    }

    public void setPaytmOrderId(String paytmOrderId) {
        this.paytmOrderId = paytmOrderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    //        public Orders(String orderDate, String userName, String address, String phone, String city, String totalAmount, String state) {
//            this.orderDate = orderDate;
//            this.userName = userName;
//            this.address = address;
//            this.phone = phone;
//            this.city = city;
//            this.totalAmount = totalAmount;
//            this.state = state;
//        }
//
//        public String getOrderTime() {
//            return orderTime;
//        }
//
//        public void setOrderTime(String orderTime) {
//            this.orderTime = orderTime;
//        }
//
//        public String getOrderDate() {
//            return orderDate;
//        }
//
//        public void setOrderDate(String orderDate) {
//            this.orderDate = orderDate;
//        }
//
//        public String getUserName() {
//            return userName;
//        }
//
//        public void setUserName(String userName) {
//            this.userName = userName;
//        }
//
//        public String getAddress() {
//            return address;
//        }
//
//        public void setAddress(String address) {
//            this.address = address;
//        }
//
//        public String getPhone() {
//            return phone;
//        }
//
//        public void setPhone(String phone) {
//            this.phone = phone;
//        }
//
//        public String getCity() {
//            return city;
//        }
//
//        public void setCity(String city) {
//            this.city = city;
//        }
//
//        public String getTotalAmount() {
//            return totalAmount;
//        }
//
//        public void setTotalAmount(String totalAmount) {
//            this.totalAmount = totalAmount;
//        }
//
//        public String getState() {
//            return state;
//        }
//
//        public void setState(String state) {
//            this.state = state;
//        }
    }

