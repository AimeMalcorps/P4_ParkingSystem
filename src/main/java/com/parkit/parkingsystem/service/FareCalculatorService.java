package com.parkit.parkingsystem.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
	
	TicketDAO ticketDAO = new TicketDAO();
	
    public void calculateFare(Ticket ticket) {
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ ticket.getOutTime().toString() + " - IN TIME : " + ticket.getInTime().toString());
        }
        
        Date inHour = ticket.getInTime();
        Date outHour = ticket.getOutTime();
             
        /* Convert milliseconds to hours */
        float minutes = TimeUnit.MILLISECONDS.convert(outHour.getTime() - inHour.getTime(), TimeUnit.MILLISECONDS);
        float duration = minutes / 60;
        
        /* Free less than 30 minutes */
        if (duration >= 0.5) {
        	switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                if (ticketDAO.getRecurrentTicket(ticket.getVehicleRegNumber()) > 1) {
                	ticket.setPrice(ticket.getPrice() / 1.05);
                }
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                if (ticketDAO.getRecurrentTicket(ticket.getVehicleRegNumber()) > 1)
                	ticket.setPrice(ticket.getPrice() / 1.05);
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