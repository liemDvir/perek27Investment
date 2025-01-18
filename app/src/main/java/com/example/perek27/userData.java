package com.example.perek27;

import java.util.Date;

public class userData {
    String firstName, lastName,email, password;
    Date date;
    userData(String newFirstName,String newLastName,String newEmail,Date newDate,String newPassword)
    {
        this.firstName = newFirstName;
        this.lastName = newLastName;
        this.email = newEmail;
        this.date = newDate;
        this.password = newPassword;
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
}
