/**
 * 
 */
package com.crossover.techtrial.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import com.crossover.techtrial.model.Person;

/**
 * Person repository for basic operations on Person entity.
 * @author crossover
 */
@RestResource(exported=false)
public interface PersonRepository extends CrudRepository<Person, Long> {
    @Query("Select p from person p where p.name = :name limit = 1")
    Person getPerson(@Param("name") String name);
}
