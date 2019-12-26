package com.oystergems.restauranttest.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.oystergems.restauranttest.model.RestaurantSchedule;

public interface RestaurantScheduleRepository extends JpaRepository<RestaurantSchedule, Long> {
	List<RestaurantSchedule> findByStartTimeGreaterThanEqualAndEndTimeLessThanEqual( Date startTime, Date endTime);	
	
}
