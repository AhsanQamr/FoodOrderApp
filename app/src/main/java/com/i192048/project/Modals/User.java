package com.i192048.project.Modals;

import android.widget.EditText;

public class User {
    String full_name,username,phone_num,address,email,password,u_id;

    private User() {
        this("","","","","","","");
    }

    private static User user = new User();




    public static User getInstance(){
        if(user == null)
            user = new User();
        return user;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public User(String full_name, String username, String phone_num, String address, String email, String password, String u_id) {
        this.full_name = full_name;
        this.username = username;
        this.phone_num = phone_num;
        this.address = address;
        this.email = email;
        this.password = password;
        this.u_id = u_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
