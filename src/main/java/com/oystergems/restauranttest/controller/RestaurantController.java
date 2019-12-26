package com.oystergems.restauranttest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oystergems.restauranttest.model.RestaurantSchedule;
import com.oystergems.restauranttest.service.RestaurantService;

@Controller
public class RestaurantController {
	
	@Autowired
	RestaurantService restaurantService;
	
	@PostMapping("/getRestaurants")
    public String getRestaurants(@RequestParam("day") String day, 
    		@RequestParam("usr_time") String usr_time,
    		RedirectAttributes redirectAttributes) {

        System.out.println("day: " + day + " usr_time: " + usr_time);

        
        List<RestaurantSchedule> restaurantList =
        		restaurantService.getRestaurantsOpenAt(Long.valueOf(day), usr_time);
       
        //System.out.println( restaurantList );
        String restaurants = "*";
        for (RestaurantSchedule restaurantSchedule : restaurantList) {
			System.out.println( restaurantSchedule.getRestaurantId().getName() );
			restaurants = restaurants + 
					restaurantSchedule.getRestaurantId().getName() + " *";
		}
      
        redirectAttributes.addFlashAttribute("restaurants", restaurants);

        return "redirect:/";
    }
}
