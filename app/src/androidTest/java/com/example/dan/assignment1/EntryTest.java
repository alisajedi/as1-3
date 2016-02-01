package com.example.dan.assignment1;

import android.test.ActivityInstrumentationTestCase2;

import java.util.regex.Pattern;

/**
 * Created by dan on 2016-01-30.
 */
public class EntryTest extends ActivityInstrumentationTestCase2 {
    public EntryTest(){
        super(MainActivity.class);
    }

    public void testGetters(){
        Entry entry = new Entry("1234/12/12","Station","12.3","Grade","1.234","1.2",0);
        assertEquals("1234/12/12",entry.getDate());
        assertEquals("Station",entry.getStation());
        assertEquals("12.3",entry.getOdometer());
        assertEquals("Grade",entry.getFuelGrade());
        assertEquals("1.234",entry.getFuelAmount());
        assertEquals("1.2",entry.getFuelCost());
        assertEquals("0.01",entry.getTotalCost());
    }

    public void testSetters(){
        Entry entry = new Entry("1111/11/11","s","1.3","G","0.234","1.5",0);
        entry.setDate("1234/12/12");
        entry.setStation("Station");
        entry.setOdometer("12.3");
        entry.setFuelGrade("Grade");
        entry.setFuelAmount("1.000");
        entry.setFuelCost("10.0");
        assertEquals("1234/12/12",entry.getDate());
        assertEquals("Station",entry.getStation());
        assertEquals("12.3",entry.getOdometer());
        assertEquals("Grade",entry.getFuelGrade());
        assertEquals("1.000",entry.getFuelAmount());
        assertEquals("10.0",entry.getFuelCost());
        assertEquals("0.10",entry.getTotalCost());
    }

    public void testToString(){
        Entry entry = new Entry("1234/12/12","Station","12.3","Grade","1.234","1.2",0);
        assertEquals("1.  1234/12/12 Station   12.3       Grade      1.234        1.2       0.01",entry.toString());
    }

    public void testTrimLeadingZeros(){
        String testString1 = "00.1";
        String testString2 = "0.1";
        String testString3 = "01.1";
        String testString4 = "1.1";
        assertEquals("0.1",trimLeadingZeros(testString1));
        assertEquals("0.1",trimLeadingZeros(testString2));
        assertEquals("1.1",trimLeadingZeros(testString3));
        assertEquals("1.1",trimLeadingZeros(testString4));
    }

    public void testPadSpaces(){
        String testString = "1234";
        assertEquals("1234    ",padSpaces(testString,8));
        assertEquals("1234 ",padSpaces(testString,5));
        assertEquals("1234",padSpaces(testString,4));
        assertEquals("1234",padSpaces(testString,3));
    }

    public void testComputeTotalCost(){
        Entry entry = new Entry("1111/11/11","s","1.3","G","10.000","10.0",0);
        String totalCost = computeTotalCost(entry);
        assertEquals("1.00",totalCost);
    }



    //The following private functions are placed here so as to be visible for the purposes of testing
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

    //this function is modified to take an entry as an argument for testing purposes
    //it has been changed to return the value, instead of setting it to the field (field in question
    //has no setter, as its value is computed from two other fields)
    private String computeTotalCost(Entry testEntry){
        //computes cost based on unit cost and amount
        //round to two decimal points
        long forRound;
        forRound = Math.round(Double.parseDouble(testEntry.getFuelCost()) * (Double.parseDouble(testEntry.getFuelAmount())));
        double total = forRound;
        //we can divide by 100 without first multiplying because the initial value was in cents
        //and the result should be in dollars
        total = total / 100;

        String totalCost=String.valueOf(total);

        //add a trailing 0 if it only has one decimal digit
        Pattern twoDecimals = Pattern.compile("^[0-9]+\\.[0-9]{2}$");
        if (!twoDecimals.matcher(totalCost).find() && !totalCost.contains("E")){
            totalCost += "0";
        }

        return totalCost;
    }

}
