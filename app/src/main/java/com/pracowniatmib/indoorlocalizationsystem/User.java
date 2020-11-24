package com.pracowniatmib.indoorlocalizationsystem;

public class User {

    private String id;
    private double x;
    private double y;

    public User(String id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }

    public String toString(){
        String out = "ID: " + id + ", x: " + x + ", y: " + y;
        return out;
    }
}
