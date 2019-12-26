package com.oystergems.restauranttest.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.oystergems.restauranttest.model.RestaurantSchedule;

public interface RestaurantScheduleRepository extends JpaRepository<RestaurantSchedule, Long> {
	List<RestaurantSchedule> findBydayOfWeekAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual( Long dayOfWeek,Date startTime, Date endTime);
	List<RestaurantSchedule> findByStartTimeLessThanEqualAndEndTimeGreaterThanEqualAndDayOfWeek( Date startTime, Date endTime, Long dayOfWeek);
	List<RestaurantSchedule> findBydayOfWeek(Long dayOfWeek);
	
}
