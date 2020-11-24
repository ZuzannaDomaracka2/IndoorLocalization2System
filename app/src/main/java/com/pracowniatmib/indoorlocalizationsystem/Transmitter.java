package com.pracowniatmib.indoorlocalizationsystem;

public class Transmitter {

    private String ID;
    private double xCoord;
    private double yCoord;
    private double transmitPower;

    public Transmitter() {
    }

    public Transmitter(String id, double xCoord, double yCoord, double transmitPower) {
        this.ID = id;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.transmitPower = transmitPower;
    }

    String getID(){ return ID; }
    double getxCoord(){ return xCoord; }
    double getyCoord(){ return yCoord; }
    double gettransmitPower(){ return transmitPower; }

    void setID(String ID) { this.ID = ID; }
    void setxCoord(double xCoord) { this.xCoord = xCoord; }
    void setyCoord(double yCoord) { this.yCoord = yCoord; }
    void setTransmitPower(double transmitPower) { this.transmitPower = transmitPower; }
}
