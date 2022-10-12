package com.app.liftlo.Ride.FairComparisson;

public class FareComparisonModel {
    String startLoc;
    String endLoc;

    public FareComparisonModel(String startLoc, String endLoc, String cost) {
        this.startLoc = startLoc;
        this.endLoc = endLoc;
        this.cost = cost;
    }

    public String getStartLoc() {
        return startLoc;
    }

    public void setStartLoc(String startLoc) {
        this.startLoc = startLoc;
    }

    public String getEndLoc() {
        return endLoc;
    }

    public void setEndLoc(String endLoc) {
        this.endLoc = endLoc;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    String cost;
}
