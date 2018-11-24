/**
 * 
 */
package com.crossover.techtrial.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.crossover.techtrial.model.Person;
import com.crossover.techtrial.service.PersonService;

/**
 * 
 * @author crossover
 */

@RestController
@RequestMapping("/api/person")
public class PersonController {
  
  @Autowired
  PersonService personService;
  
  @PostMapping(path = "/register")
  public ResponseEntity<Person> register(@RequestBody Person p) {
      return ResponseEntity.ok(personService.save(p));
  }
  
  @GetMapping(path = "/getAllPerson")
  public ResponseEntity<List<Person>> getAllPersons() {
    return ResponseEntity.ok(personService.getAll());
  }
  
  @GetMapping(path = "/getPerson?personId={perso-id}")
  public ResponseEntity<Person> getPersonById(@PathVariable(name="person-id", required=true)Long personId) {
    Person person = personService.findById(personId);
    if (person != null) {
      return ResponseEntity.ok(person);
    }
    return ResponseEntity.notFound().build();
  }
  
}
