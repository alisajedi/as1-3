package com.example.dan.assignment1;

import java.util.regex.Pattern;

/**
 * Created by dan on 2016-01-25.
 */
public class Entry {
    private String date;
    private String station;
    private String odometer;
    private String fuelGrade;
    private String fuelAmount;
    private String fuelCost;
    private String totalCost;
    private final int number;

    public Entry(String date, String station, String odometer, String grade, String amount, String cost, int number){
        this.date=date;
        this.station=station;
        this.odometer=trimLeadingZeros(odometer);
        this.fuelGrade=grade;
        this.fuelAmount=trimLeadingZeros(amount);
        this.fuelCost=trimLeadingZeros(cost);
        this.number=number;
        computeTotalCost();
    }

    public String getDate() {
        return date;
    }

    public String getStation() {
        return station;
    }

    public String getOdometer() {
        return odometer;
    }

    public String getFuelGrade() {
        return fuelGrade;
    }

    public String getFuelAmount() {
        return fuelAmount;
    }

    public String getFuelCost() {
        return fuelCost;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setFuelCost(String fuelCost) {
        this.fuelCost = fuelCost;
        computeTotalCost();
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public void setOdometer(String odometer) {
        this.odometer = odometer;
    }

    public void setFuelGrade(String fuelGrade) {
        this.fuelGrade = fuelGrade;
    }

    public void setFuelAmount(String fuelAmount) {
        this.fuelAmount = fuelAmount;
        computeTotalCost();
    }

    @Override
    public String toString(){
        String retString = date + " ";

        if (station.length()>9){
            retString += station.substring(0,8)+"… ";
        }
        else{
            retString += station;
        }
        retString = padSpaces(retString,21);

        if (odometer.length()>10){
            retString += "99999999.9 ";
        }
        else{
            retString += odometer;
        }
        retString = padSpaces(retString,32);

        if (fuelGrade.length()>10){
            retString += fuelGrade.substring(0,9)+"… ";
        }
        else{
            retString += fuelGrade;
        }
        retString = padSpaces(retString,43);

        if (fuelAmount.length()>12){
            retString += "99999999.999 ";
        }
        else{
            retString += fuelAmount;
        }
        retString = padSpaces(retString,56);

        if (fuelCost.length()>9){
            retString += "9999999.9 ";
        }
        else{
            retString += fuelCost;
        }
        retString = padSpaces(retString, 66);

        if (totalCost.length()>12){
            retString += "999999999.99 ";
        }
        else{
            retString += totalCost;
        }

        if (number<10) {
            retString = String.valueOf(number) + ".  " + retString;
        }
        else{
            retString = String.valueOf(number) + ". " + retString;
        }
        //return date + " " + station + " " + odometer + " " + fuelGrade + " " + fuelAmount + " " + fuelCost + " " + totalCost;
        return retString;
    }

    private String trimLeadingZeros(String string){
        while (string.substring(0,1)=="0"){
            string = string.substring(1);
        }
        return string;
    }

    private String padSpaces(String string, int length){
        if (string.length()>=length){
            return string;
        }
        while (string.length()!=length){
            string += " ";
        }
        return string;
    }

    private void computeTotalCost(){
        long forRound;
        forRound = Math.round(Double.parseDouble(this.fuelCost) * (Double.parseDouble(this.fuelAmount)));
        double total = forRound;
        total = total / 100;

        this.totalCost=String.valueOf(total);

        Pattern twoDecimals = Pattern.compile("^[0-9]+\\.[0-9]{2}$");
        if (!twoDecimals.matcher(totalCost).find() && !totalCost.contains("E")){
            totalCost += "0";
        }

    }
}
