package com.example.perek27;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class UserData {
   private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Date date;
    private float cash;
    private String imageView;
    UserData(String newFirstName, String newLastName, String newEmail, Date newDate, String newPassword, String newImageView)
    {
        this.firstName = newFirstName;
        this.lastName = newLastName;
        this.email = newEmail;
        this.date = newDate;
        this.password = newPassword;
        this.imageView = newImageView;
        cash = 5000; // כמות התחלתית של כסף
    }

    public UserData()
    {

    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public Date getDate() {
        return date;
    }

    public String getImageView() {
        return imageView;
    }

    public void setImageView(String imageView) {
        this.imageView = imageView;
    }
    public void setPicAsString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        this.imageView = Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    public  void addCah(int i)
    {
        cash += i;
    }
    public boolean removeCash(int i) // returns boolean variable
    {
        if(i > cash)
        {
            return false;
        }
        else
        {
           cash =- i;
           return true;
        }
    }

    public float getCash() {
        return cash;
    }
}
