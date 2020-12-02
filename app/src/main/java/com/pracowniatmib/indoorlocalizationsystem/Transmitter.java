package com.pracowniatmib.indoorlocalizationsystem;

public class Transmitter {

    private String id;
    private double xCoord;
    private double yCoord;
    private double transmitPower;

    public Transmitter() {
    }

    public Transmitter(String id, double xCoord, double yCoord, double transmitPower) {
        this.id = id;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.transmitPower = transmitPower;
    }

    String getId(){ return id; }
    double getxCoord(){ return xCoord; }
    double getyCoord(){ return yCoord; }
    double gettransmitPower(){ return transmitPower; }

    void setId(String id) { this.id = id; }
    void setxCoord(double xCoord) { this.xCoord = xCoord; }
    void setyCoord(double yCoord) { this.yCoord = yCoord; }
    void setTransmitPower(double transmitPower) { this.transmitPower = transmitPower; }
}
