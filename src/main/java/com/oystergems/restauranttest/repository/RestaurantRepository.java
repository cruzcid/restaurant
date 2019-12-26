package com.oystergems.restauranttest.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.oystergems.restauranttest.model.Restaurant;
import com.oystergems.restauranttest.model.RestaurantSchedule;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>{
	Page<Restaurant> findAllRestaurantsByName(String name,Pageable pageable);	
	
}
