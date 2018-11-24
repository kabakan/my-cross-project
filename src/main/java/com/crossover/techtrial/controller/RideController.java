/**
 * 
 */
package com.crossover.techtrial.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.crossover.techtrial.model.Person;
import com.crossover.techtrial.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.crossover.techtrial.dto.TopDriverDTO;
import com.crossover.techtrial.model.Ride;
import com.crossover.techtrial.service.RideService;

/**
 * RideController for Ride related APIs.
 * @author crossover
 *
 */
@RestController
@RequestMapping("/api/ride")
public class RideController {
  
  @Autowired
  RideService rideService;

  @Autowired
  PersonService personService;

  @PostMapping(path ="/createNewRide")
  public ResponseEntity<Ride> createNewRide(@RequestBody Ride ride) {
    return ResponseEntity.ok(rideService.save(ride));
  }
  
  @GetMapping(path = "/getRide?rideId={rideId}")
  public ResponseEntity<Ride> getRideById(@PathVariable(name="rideId",required=true)Long rideId){
    Ride ride = rideService.findById(rideId);
    if (ride!=null)
      return ResponseEntity.ok(ride);
    return ResponseEntity.notFound().build();
  }
  
  /**
   * This API returns the top 5 drivers with their email,name, total minutes, maximum ride duration in minutes.
   * Only rides that starts and ends within the mentioned durations should be counted.
   * Any rides where either start or endtime is outside the search, should not be considered.
   * 
   * DONT CHANGE METHOD SIGNATURE AND RETURN TYPES
   * @return
   */
  @GetMapping(path = "/getTopRides")
  public ResponseEntity<List<TopDriverDTO>> getTopDriver(
      @RequestParam(value="max", defaultValue="5") Long count,
      @RequestParam(value="startTime", required=true) @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startTime,
      @RequestParam(value="endTime", required=true) @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endTime){
    List<TopDriverDTO> topDrivers = new ArrayList<TopDriverDTO>();
    Integer next = 0;

    for (TopDriverDTO topDriverDTO : rideService.getTopDriver(startTime,endTime)) {
      next =+1;
      if (next.equals(count)) {break;}
      topDrivers.add(topDriverDTO);
    }

    return ResponseEntity.ok(topDrivers);
    
  }
  
}
