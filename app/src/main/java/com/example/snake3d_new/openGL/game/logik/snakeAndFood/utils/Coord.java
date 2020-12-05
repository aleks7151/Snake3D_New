package com.example.snake3d_new.openGL.game.logik.snakeAndFood.utils;

public class Coord {
    private double x;
    private double y;
    private double z;

    public Coord(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void plusX(double plus){
        x += plus;
    }

    public void plusY(double plus){
        y += plus;
    }

    public void plusZ(double plus){
        z += plus;
    }

    public Coord clone(){
        return new Coord(x, y, z);
    }

    public boolean equals(Coord coord){
        return coord.getX() == x && coord.getY() == y && coord.getZ() == z;
    }
}
