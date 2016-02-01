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
            //truncate if too long
            retString += station.substring(0,8)+"… ";
        }
        else{
            retString += station;
        }
        retString = padSpaces(retString,21);//pad to fill up screen space

        if (odometer.length()>10){
            //display this if overflow
            retString += "99999999.9 ";
        }
        else{
            retString += odometer;
        }
        retString = padSpaces(retString,32);

        if (fuelGrade.length()>10){
            //truncate if too long
            retString += fuelGrade.substring(0,9)+"… ";
        }
        else{
            retString += fuelGrade;
        }
        retString = padSpaces(retString,43);

        if (fuelAmount.length()>12){
            //display this if overflow
            retString += "99999999.999 ";
        }
        else{
            retString += fuelAmount;
        }
        retString = padSpaces(retString,56);

        if (fuelCost.length()>9){
            //display this if overflow
            retString += "9999999.9 ";
        }
        else{
            retString += fuelCost;
        }
        retString = padSpaces(retString, 66);

        if (totalCost.length()>12){
            //display this if overflow
            retString += "999999999.99 ";
        }
        else{
            retString += totalCost;
        }

        //prefix string with index number
        if (number+1<10) {
            retString = String.valueOf(number+1) + ".  " + retString;
        }
        else{
            retString = String.valueOf(number+1) + ". " + retString;
        }
        return retString;
    }

    private String trimLeadingZeros(String string){
        //removes leading zeros (except for before decimal)
        while (string.indexOf("0")==0 && string.indexOf(".")!=1){
            string = string.substring(1);
        }
        return string;
    }

    private String padSpaces(String string, int length){
        //will add spaces onto the end of a string until string is of desired length
        if (string.length()>=length){
            return string;
        }
        while (string.length()!=length){
            string += " ";
        }
        return string;
    }

    private void computeTotalCost(){
        //computes cost based on unit cost and amount
        //round to two decimal points
        long forRound;
        forRound = Math.round(Double.parseDouble(this.fuelCost) * (Double.parseDouble(this.fuelAmount)));
        double total = forRound;
        //we can divide by 100 without first multiplying because the initial value was in cents
        //and the result should be in dollars
        total = total / 100;

        this.totalCost=String.valueOf(total);

        //add a trailing 0 if it only has one decimal digit
        Pattern twoDecimals = Pattern.compile("^[0-9]+\\.[0-9]{2}$");
        if (!twoDecimals.matcher(totalCost).find() && !totalCost.contains("E")){
            totalCost += "0";
        }

    }
}
