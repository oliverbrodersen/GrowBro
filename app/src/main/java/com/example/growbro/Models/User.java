package com.example.growbro.Models;

public class User {
    private int Id;
    private String Username;
    private String Password;

    public User() {
    }

    public User(int Id, String Username, String Password) {
        this.Id = Id;
        this.Username = Username;
        this.Password = Password;
    }

    public int getId() {
        return Id;
    }

    public String getUsername() {
        return Username;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getPassword() {
        return Password;
    }

    @Override
    public String toString() {
        return  "Id=" + Id +
                ", Username='" + Username + '\'' +
                ", Password='" + Password + '\'';
    }
}
