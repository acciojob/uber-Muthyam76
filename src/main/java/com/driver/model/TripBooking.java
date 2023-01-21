package com.driver.model;

import javax.persistence.*;

@Entity
public class TripBooking{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tripBookingId;
    private String fromLocation;
    private String toLocation;
    private int distanceInKm;
    private int bill;
   private  TripStatus status;

    public TripBooking(String fromLocation, String toLocation, int distanceInKm) {
        this.distanceInKm=distanceInKm;
        this.fromLocation=fromLocation;
        this.toLocation=toLocation;
    }

    public int getTripBookingId() {
        return tripBookingId;
    }

    public void setTripBookingId(int tripBookingId) {
        this.tripBookingId = tripBookingId;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public int getDistanceInKm() {
        return distanceInKm;
    }

    public void setDistanceInKm(int distanceInKm) {
        this.distanceInKm = distanceInKm;
    }

    public int getBill() {
        return bill;
    }

    public void setBill(int bill) {
        this.bill = bill;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    public TripBooking() {
    }

    public TripBooking(int tripBookingId,String fromLocation,String toLocation,int distanceInKm,int bill,TripStatus Status) {
        this.tripBookingId = tripBookingId;
        this.bill=bill;
        this.distanceInKm=distanceInKm;
        this.fromLocation=fromLocation;
        this.toLocation=toLocation;
        this.status = Status;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @ManyToOne
    private Driver driver;
    @ManyToOne
    private Customer customer;

}