package com.oystergems.restauranttest.service;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.print.attribute.standard.SheetCollate;
import javax.validation.ReportAsSingleViolation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.oystergems.restauranttest.exception.FileStorageException;
import com.oystergems.restauranttest.model.Restaurant;
import com.oystergems.restauranttest.model.RestaurantSchedule;

@Service
public class FileService {

    @Value("${app.upload.dir:${user.home}}")
    public String uploadDir;

    @Autowired
    public RestaurantService restaurantService;
    
    public void uploadFile(MultipartFile file) {

        try {
            Path copyLocation = Paths
                .get(uploadDir + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            InputStream fileStream = file.getInputStream();
            
            // reads till the end of the stream
            int i;
            char c = Character.MIN_VALUE;
            String line = "";
            
            while((i = fileStream.read())!=-1) {
            
               // converts integer to character
            	c = (char)i;
               if(c == '"') continue;
			   if(c =='\n') {
				   // treat csv file
				   System.out.println("Complete: " + line);
				   treatCsvLine(line);
				   
				   line = ""; 
			   }else {
				   line = line + c;				   
			   }
			   
            }            
            Files.copy(fileStream, copyLocation, StandardCopyOption.REPLACE_EXISTING);
            
           
            
            
        
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileStorageException("Could not store file " + file.getOriginalFilename()
                + ". Please try again!");
        }
    }
    
    public void treatCsvLine(String csvLine) {
    	String restaurantAndSchedule[] = csvLine.split(",",2);    	
    	System.out.println(restaurantAndSchedule[0]);
    	
    	// Save Restaurant
    	Restaurant r = new Restaurant();
        r.setName(restaurantAndSchedule[0]);               
        Restaurant savedRestaurant = restaurantService.saveRestaurant(r);
        System.out.println("savedRestaurant: " + savedRestaurant);
        
    	// Separate between Schedules
    	String schedules[] = restaurantAndSchedule[1].split("/");

    	System.out.println( restaurantAndSchedule[1] );
    	
    	// Process each schedule 
    	for(int i=0; i<schedules.length; i++) {
    		System.out.println( "   -sch: " + schedules[i] );
    		saveDaysOfOpenHours( schedules[i], savedRestaurant );
    	}
    }
    
    public void saveDaysOfOpenHours(String openDates, Restaurant restaurant) {
    	
    	// Get Substring starting with the hours.
    	int hoursStartSubstring = indexOfFirstPatterOcurrance(openDates, "[0-9]");    	
    	String hoursStartAndEnd[] = openDates.substring(hoursStartSubstring).split("-");
    	
    	// Get start date
    	String hoursStart = hoursStartAndEnd[0];
    	Date hoursStartDate = hoursTextToDate(hoursStartAndEnd[0]);
    	System.out.println("        hoursStartDate:" + hoursStartDate.toString());
    	// Get end date
    	String hoursEnd = hoursStartAndEnd[1];
    	Date hoursEndDate = hoursTextToDate(hoursStartAndEnd[1]);
    	System.out.println("        hoursEndDate:" + hoursEndDate.toString());
    	
    	// Get the substring of possible open days.
    	String daysTxt = openDates.substring(0, hoursStartSubstring);
    	System.out.println("     Hours Open: Start:*" + hoursStart + "* End: *" + hoursEnd + "*  Days Open: *" + daysTxt +"*");
    	
    	// Get possible days separated by ','
    	String givenDays[] = daysTxt.split(",");
    	for( int d=0; d<givenDays.length; d++ ) {
    		if(indexOfFirstPatterOcurrance(givenDays[d], "-") != -1) {
    			// Eg. givenDays[d] be like Mon-Tue
    			saveForRangeDate(hoursStartDate, hoursEndDate, givenDays[d], restaurant);
    		}else {
    			// Save unique day Eg: Mon
    			saveForUniqueDate(hoursStartDate, hoursEndDate, givenDays[d], restaurant);
    		}
    	}
    }
    
    public void saveForRangeDate(Date startDate, Date endDate, String dateRange, Restaurant restaurantId) {
    	// Eg. Mon-Fri 

    	dateRange = dateRange.trim();
    	String dayStartToEnd[] = dateRange.split("-");
    	int startDay = getNumberFromWeekDay(dayStartToEnd[0]);
    	int endDay = getNumberFromWeekDay(dayStartToEnd[1]);
    	
    	for(int start = startDay; start <= endDay; start++ ) {
    		
    		RestaurantSchedule restaurantSchedule = new RestaurantSchedule();
    		restaurantSchedule.setDayOfWeek(Long.valueOf(start));
    		restaurantSchedule.setStartTime(startDate);
    		restaurantSchedule.setEndTime(endDate);
    		restaurantSchedule.setRestaurantId(restaurantId);
    		System.out.println("                             saveForRangeDate: " + restaurantSchedule.toString());
    		RestaurantSchedule savedRestaurantSchedule = restaurantService.saveRestaurantSchedule(restaurantSchedule); 
    		System.out.println("                                savedRestaurantSchedule: " + restaurantSchedule.toString());
    	}
    	
    }
    
    public void saveForUniqueDate(Date startDate, Date endDate, String givenDay, Restaurant restaurantId) {
    	// Eg. Mon || Fri 

    	givenDay = givenDay.trim();
    	
    	int uniqueDay = getNumberFromWeekDay( givenDay );
    	    	    	
		RestaurantSchedule restaurantSchedule = new RestaurantSchedule();
		restaurantSchedule.setDayOfWeek(Long.valueOf(uniqueDay));
		restaurantSchedule.setStartTime(startDate);
		restaurantSchedule.setEndTime(endDate);
		restaurantSchedule.setRestaurantId(restaurantId);
		System.out.println("                             saveForUniqueDate: " + restaurantSchedule.toString());
		
		RestaurantSchedule savedRestaurantSchedule = restaurantService.saveRestaurantSchedule(restaurantSchedule); 
		System.out.println("                               savedRestaurantSchedule: " + restaurantSchedule.toString());
		
    }
    
    public int getNumberFromWeekDay(String day) {
		// Eg: Mon = 1, Tue = 2, ... , Sun = 7
    	int gDay = 0;
		
    	switch(day) {
    		case "Mon":    		
    			gDay = 1;
    	    	break;
    		case "Tue":
    			gDay = 2;
    			break;
    		case "Wed":
    			gDay = 3;
    			break;
    		case "Thu":
    			gDay = 4;
    			break;
    		case "Fri":
    			gDay = 5;
    			break;
    		case "Sat":
    			gDay = 6;
    			break;
    		case "Sun":
    			gDay = 7;
    			break;
    	  default:
    		  gDay = 1;
    	}
    	
    	return gDay;
    }
    public int indexOfFirstPatterOcurrance(String text, String patternString) {
    	
    	int ocurrance = -1;
    	// Find number with regex
    	Pattern pattern = Pattern.compile(patternString);
    	Matcher matcher = pattern.matcher(text);
    	
    	if( matcher.find() ) {
    		ocurrance = matcher.start();
    	}
    	
    	return ocurrance;
    	
    }
    
    public Date hoursTextToDate(String hoursText) {
    	// Eg: 11:40 am || 2:30 pm
    	hoursText = hoursText.trim();
    	Calendar cal = Calendar.getInstance();
    	
    	int hours = 0;
    	int minutes = 0;
    	
    	boolean isPm = indexOfFirstPatterOcurrance(hoursText, "pm") != -1;
    	
    	String hoursMinutesAndAmpm[] = hoursText.split(" ");
    	// Eg: hoursMinutesAndAmpm[0] be like 11:40 am || 2:30 pm
    	
    	hours = getHoursFromHoursText(hoursMinutesAndAmpm[0]);
    	minutes = getMinutesFromHoursText(hoursMinutesAndAmpm[0]);    
    	
    	if( isPm ) {    		
    		
    		// It`s pm.  Eg: 12 pm + 1 = 13hrs, 10pm + 12 = 22hrs
    		int midDay = 12;
    		hours = midDay + hours;    	
    	
    	} 
    	
    	// Set Hours and Minutes
    	cal.set(Calendar.HOUR_OF_DAY, hours);
    	cal.set(Calendar.MINUTE, minutes );
    	cal.set(Calendar.SECOND, 0);
    	cal.set(Calendar.MILLISECOND, 0);
    	Date date = cal.getTime();
    	
    	return date;
    }
    
    public int getMinutesFromHoursText(String hoursText) {
    	
    	String hoursMinutes[] = hoursText.split(":");
    	int minutes = 0;
    	
    	if( hoursMinutes.length > 1 ) {
    		// It has minutes  11:34
    		minutes = Integer.parseInt(hoursMinutes[1]);
    	}
    	return minutes;
    }
    
    public int getHoursFromHoursText(String hoursText) {
    	
    	String hoursMinutes[] = hoursText.split(":");
    	int hours = Integer.parseInt(hoursMinutes[0]);
    	    	
    	return hours;
    }
}
