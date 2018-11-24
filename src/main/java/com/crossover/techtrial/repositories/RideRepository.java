/**
 * 
 */
package com.crossover.techtrial.repositories;


import com.crossover.techtrial.model.Ride;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import java.time.LocalDateTime;
import java.util.List;


/**
 * @author crossover
 *
 */
@RestResource(exported = false)
public interface RideRepository extends CrudRepository<Ride, Long> {
    @Query("select r " +
            "from ride r, person p " +
            "where r.ride_id = p.id " +
            "and p.name = :name " +
            "and r.start_time >= :startTime and r.end_time <= :startTime")
    List<Ride> getTopRides(@Param("name") String name, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
