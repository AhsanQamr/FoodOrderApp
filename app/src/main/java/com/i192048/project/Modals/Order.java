package com.i192048.project.Modals;

public class Order {

    public Order() {
    }

    public Order( String total, String status, String date, String id) {
        this.total = total;
        this.status = status;
        this.date = date;
        this.id = id;
    }



    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String names;
    private String total;
    private String status;
    private String date;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
