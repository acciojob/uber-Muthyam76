package com.driver.services.impl;

import com.driver.model.TripBooking;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.model.Customer;
import com.driver.model.Driver;
import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;
import com.driver.model.TripStatus;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		Customer customer=customerRepository2.findById(customerId).get();
		List<TripBooking>bookings=customer.getTripBookingList();
		for(TripBooking trip : bookings){
			Driver driver=trip.getDriver();
			driver.getCab().setAvailable(true);
			driver.getTripBookingList().remove(trip);
			driverRepository2.save(driver);
		}
		customerRepository2.delete(customer);

	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		TripBooking tripBooking=new TripBooking(fromLocation,toLocation,distanceInKm);

		List<Driver> driverList =driverRepository2.findAll();
		for(Driver d :driverList){
			if(d.getCab().isAvailable()==true)
			{
				Customer customer=customerRepository2.findById(customerId).get();
				tripBooking.setCustomer(customer);
				tripBooking.setDriver(d);
				d.getCab().setAvailable(false);
				List<TripBooking> bookingList=customer.getTripBookingList();
				bookingList.add(tripBooking);
				List<TripBooking>bookingList1=d.getTripBookingList();
				bookingList1.add(tripBooking);
				customerRepository2.save(customer);
				driverRepository2.save(d);
				break;
			}

		}
    return tripBooking;
		//Avoid using SQL query

	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking=tripBookingRepository2.findById(tripId).get();
		int id=tripBooking.getCustomer().getCustomerId();
		Customer customer=customerRepository2.findById(id).get();
		id=tripBooking.getDriver().getDriverId();
		customer.getTripBookingList().remove(tripBooking);
		Driver driver=driverRepository2.findById(id).get();
		driver.getCab().setAvailable(true);
		driver.getTripBookingList().remove(tripBooking);
		customerRepository2.save(customer);
		driverRepository2.save(driver);
		tripBookingRepository2.delete(tripBooking);

	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly

	}
}
