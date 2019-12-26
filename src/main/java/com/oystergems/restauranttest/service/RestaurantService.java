package com.oystergems.restauranttest.service;

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
}
