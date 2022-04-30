package com.app.liftlo.Ride.AllDrivers;

public class DriverFilter_model {
    String driver_name
            , driver_number, car_name, car_color, rating, driver_image;

    public DriverFilter_model(String driver_name, String driver_number, String car_name, String car_color, String rating, String driver_image) {
        this.driver_name = driver_name;
        this.driver_number = driver_number;
        this.car_name = car_name;
        this.car_color = car_color;
        this.rating = rating;
        this.driver_image = driver_image;
    }


    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_number() {
        return driver_number;
    }

    public void setDriver_number(String driver_number) {
        this.driver_number = driver_number;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getCar_color() {
        return car_color;
    }

    public void setCar_color(String car_color) {
        this.car_color = car_color;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDriver_image() {
        return driver_image;
    }

    public void setDriver_image(String driver_image) {
        this.driver_image = driver_image;
    }
}
