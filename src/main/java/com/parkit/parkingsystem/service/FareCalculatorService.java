package com.parkit.parkingsystem.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
	
    public void calculateFare(Ticket ticket) {
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ ticket.getOutTime().toString() + " - IN TIME : " + ticket.getInTime().toString());
        }
        
        Date inHour = ticket.getInTime();
        Date outHour = ticket.getOutTime();
       
        //TODO: Some tests are failing here. Need to check if this logic is correct
             
        /* Convert milliseconds to hours */
        float minutes = TimeUnit.MINUTES.convert(outHour.getTime() - inHour.getTime(), TimeUnit.MILLISECONDS);
        float duration = minutes / 60;
        
        /* Free less than 30 minutes */
        if (duration >= 0.5) {
        	switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        	}	
        } else {
        	System.out.println("Less Than 30min - Free");
        	ticket.setPrice(0);
        }
        
    }
}