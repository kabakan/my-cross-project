/**
 *
 */
package com.crossover.techtrial.service;

import java.security.Key;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

import com.crossover.techtrial.dto.TopDriverDTO;
import com.crossover.techtrial.model.Person;
import com.crossover.techtrial.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.crossover.techtrial.model.Ride;
import com.crossover.techtrial.repositories.RideRepository;

/**
 * @author crossover
 */
@Service
public class RideServiceImpl implements RideService {

    @Autowired
    RideRepository rideRepository;

    @Autowired
    PersonRepository personRepository;

    private HashMap<Integer, Double> free = new HashMap<>();

    public Ride save(Ride ride) {
        return rideRepository.save(ride);
    }

    public Ride findById(Long rideId) {
        Optional<Ride> optionalRide = rideRepository.findById(rideId);
        if (optionalRide.isPresent()) {
            return optionalRide.get();
        } else return null;
    }

    public List<TopDriverDTO> getTopDriver(LocalDateTime startTime, LocalDateTime endTime) {
        Integer id = 0;
        List<TopDriverDTO> listTopDriverDTO = new ArrayList<>();
        for (Person pers : personRepository.findAll()) {
            List<Ride> rides = rideRepository.getTopRides(pers.getName(), startTime, endTime);

            HashMap<Integer, Double> distance = new HashMap<>();
            for (Ride ride : rides) {
                id = +1;
                distance.put(id, ride.getDistance());
            }

            //get average Distance
            Double distanceAVG = getAVG(distance);

            HashMap<Integer, Double> times = new HashMap<>();
            for (Ride ride : rides) {
                id = +1;
                times.put(id, Double.parseDouble(ride.getStartTime()) + Double.parseDouble(ride.getEndTime()));
            }
            id = 0;
            Integer maxKey = Collections.max(times.entrySet(), Map.Entry.comparingByValue()).getKey();
            getFreeTime(times, id, maxKey);

            //get max summ free time
            Double maxValue = Collections.max(free.entrySet(), Map.Entry.comparingByValue()).getValue();

            //get count summ free time
            Double countValue = free.values().stream().mapToDouble(i -> i).sum();

            TopDriverDTO topDriverDTO = new TopDriverDTO();
            topDriverDTO.setName(pers.getName());
            topDriverDTO.setEmail(pers.getEmail());
            topDriverDTO.setAverageDistance(distanceAVG);
            topDriverDTO.setMaxRideDurationInSecods(Math.round(maxValue));
            topDriverDTO.setTotalRideDurationInSeconds(Math.round(countValue));
            listTopDriverDTO.add(topDriverDTO);
        }

        //Sorted AverageDistance for TOP
        Collections.sort(listTopDriverDTO, new Comparator<TopDriverDTO>() {
            public int compare(TopDriverDTO c1, TopDriverDTO c2) {
                if (c1.getAverageDistance() > c2.getAverageDistance()) return -1;
                if (c1.getAverageDistance() > c2.getAverageDistance()) return 1;
                return 0;
            }
        });

        return listTopDriverDTO;
    }

    public void getFreeTime(HashMap<Integer, Double> times, Integer id, Integer max) {
        if (id.equals(max)) {
            return;
        }
        Double d1 = times.get(id);
        Double d2 = times.get(id + 1);
        Double d3 = d1 - d2;
        free.put(id, d3);
        getFreeTime(times, id, max);
    }

    public static Double getAVG(HashMap<Integer, Double> distance) {
        double sum = 0;
        int count = 0;
        Iterator<Integer> it = distance.keySet().iterator();
        while (it.hasNext()) {
            int y = it.next();
            if (y % 2 == 0) {
                sum = (double) (sum + distance.get(y));
                count++;
            }
        }
        double d = sum / count;
        return d;
    }

}
