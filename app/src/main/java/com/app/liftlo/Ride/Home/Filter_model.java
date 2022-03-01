package com.app.liftlo.Ride.Home;

public class Filter_model {
    String booked_seats, status, date, time, driver_name
            , driver_number, start_name, dest_name
            , seat_no, seat_cost, car_name, car_color, ac, music
            , smoking, rating, driver_image;


    public Filter_model(String booked_seats, String status, String date, String time, String driver_name, String driver_number, String start_name, String dest_name, String seat_no, String seat_cost, String car_name, String car_color, String ac, String music, String smoking, String rating, String driver_image) {
        this.booked_seats = booked_seats;
        this.status = status;
        this.date = date;
        this.time = time;
        this.driver_name = driver_name;
        this.driver_number = driver_number;
        this.start_name = start_name;
        this.dest_name = dest_name;
        this.seat_no = seat_no;
        this.seat_cost = seat_cost;
        this.car_name = car_name;
        this.car_color = car_color;
        this.ac = ac;
        this.music = music;
        this.smoking = smoking;
        this.rating = rating;
        this.driver_image = driver_image;
    }

    public String getBooked_seats() {
        return booked_seats;
    }

    public void setBooked_seats(String booked_seats) {
        this.booked_seats = booked_seats;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getStart_name() {
        return start_name;
    }

    public void setStart_name(String start_name) {
        this.start_name = start_name;
    }

    public String getDest_name() {
        return dest_name;
    }

    public void setDest_name(String dest_name) {
        this.dest_name = dest_name;
    }

    public String getSeat_no() {
        return seat_no;
    }

    public void setSeat_no(String seat_no) {
        this.seat_no = seat_no;
    }

    public String getSeat_cost() {
        return seat_cost;
    }

    public void setSeat_cost(String seat_cost) {
        this.seat_cost = seat_cost;
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

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getSmoking() {
        return smoking;
    }

    public void setSmoking(String smoking) {
        this.smoking = smoking;
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
