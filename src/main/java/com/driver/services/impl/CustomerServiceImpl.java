package com.driver.services.impl;

import com.driver.model.*;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;

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
		/*if(customer!=null) {
			List<TripBooking> bookings = customer.getTripBookingList();
			for (TripBooking trip : bookings) {
				Driver driver = trip.getDriver();
				driver.getCab().setAvailable(true);
				driver.getTripBookingList().remove(trip);
				driverRepository2.save(driver);
			}*/
			customerRepository2.delete(customer);

	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception {
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		TripBooking tripBooking = new TripBooking(fromLocation, toLocation, distanceInKm);

		List<Driver> driverList = driverRepository2.findAll();
		Driver driver = null;
		for (Driver driver1 : driverList) {
			if (driver1.getCab().getAvailable()) {
				if (driver == null || driver.getDriverId() > driver1.getDriverId()) {
					driver = driver1;
				}
			}
		}
		if (driver == null)
			throw new Exception("No cab available!");
		else {
			int rate = driver.getCab().getPerKmRate();
			tripBooking.setBill(rate * distanceInKm);
			driver.getCab().setAvailable(false);
			tripBooking.setStatus(TripStatus.CONFIRMED);
			Customer customer = customerRepository2.findById(customerId).get();
			customer.getTripBookingList().add(tripBooking);
			driver.getTripBookingList().add(tripBooking);
			customerRepository2.save(customer);
			driverRepository2.save(driver);


			//Avoid using SQL query

		}
		return tripBooking;
	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking=tripBookingRepository2.findById(tripId).get();
		tripBooking.setStatus(TripStatus.CANCELED);
		tripBooking.setBill(0);
		tripBooking.getDriver().getCab().setAvailable(true);
		Customer customer=tripBooking.getCustomer();

		Driver driver =tripBooking.getDriver();
		tripBookingRepository2.save(tripBooking);


	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking=tripBookingRepository2.findById(tripId).get();
		tripBooking.setStatus(TripStatus.COMPLETED);
		tripBooking.setBill(0);
		tripBooking.getDriver().getCab().setAvailable(true);
		tripBookingRepository2.save(tripBooking);
		Customer customer=tripBooking.getCustomer();

		Driver driver =tripBooking.getDriver();
	}
}
