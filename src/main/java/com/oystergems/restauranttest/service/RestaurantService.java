package com.oystergems.restauranttest.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oystergems.restauranttest.model.Restaurant;
import com.oystergems.restauranttest.model.RestaurantSchedule;
import com.oystergems.restauranttest.repository.RestaurantRepository;
import com.oystergems.restauranttest.repository.RestaurantScheduleRepository;

@Service
public class RestaurantService {
		
	@Autowired
	RestaurantRepository restaurantRepository; 
	
	@Autowired
	RestaurantScheduleRepository restaurantScheduleRepository;
	
	public Restaurant saveRestaurant(Restaurant restaurant) {
		return restaurantRepository.save(restaurant);
	}
	
	public RestaurantSchedule saveRestaurantSchedule(RestaurantSchedule restaurantSchedule) {
		return restaurantScheduleRepository.save(restaurantSchedule);
	}
	
	public List<RestaurantSchedule> getRestaurantsOpenAt(Long numberOfDay, String hourOfDay){
		Date startTime = hoursTextAndDayToDate(hourOfDay, numberOfDay);
		Date endTime = new Date();
		//return restaurantScheduleRepository.findBydayOfWeekAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual( numberOfDay, startTime, startTime);
		//return restaurantScheduleRepository.findBydayOfWeek( numberOfDay );
		return restaurantScheduleRepository.findByStartTimeLessThanEqualAndEndTimeGreaterThanEqualAndDayOfWeek( startTime, startTime, numberOfDay);
	}
	
	public Date hoursTextAndDayToDate(String hoursText, Long numberOfDay) {
    	// Eg: 11:40 am || 2:30 pm
    	hoursText = hoursText.trim();
    	Calendar cal = Calendar.getInstance();
    	
    	int hours = 0;
    	int minutes = 0;
    	
    	String hoursMinutes[] = hoursText.split(":");
    	hours = Integer.valueOf(hoursMinutes[0]);
    	minutes = Integer.valueOf(hoursMinutes[1]);    
    	    	
    	
    	// Set Hours and Minutes
    	cal.set(Calendar.HOUR_OF_DAY, hours);
    	cal.set(Calendar.MINUTE, minutes );
    	cal.set(Calendar.SECOND, 0);
    	cal.set(Calendar.MILLISECOND, 0);
    	cal.set(Calendar.DAY_OF_WEEK, Math.toIntExact(numberOfDay));
    	Date date = cal.getTime();
    	System.out.println(date);
    	return date;
    }
}
