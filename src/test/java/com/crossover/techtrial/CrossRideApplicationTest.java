/**
 *
 */
package com.crossover.techtrial;

import com.crossover.techtrial.controller.RideController;
import com.crossover.techtrial.dto.TopDriverDTO;
import com.crossover.techtrial.model.Person;
import com.crossover.techtrial.model.Ride;
import com.crossover.techtrial.service.RideServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.crossover.techtrial.util.TestUtil.asJsonString;

/**
 * @author crossover
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RideController.class)
public class CrossRideApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private RideController rideController;

    @MockBean
    private ResponseEntity responseEntity;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testCreateNewRide() throws Exception {
        RideServiceImpl rideService = new RideServiceImpl();
        Ride ride = new Ride();
        ride.setId(1L);
        ride.setDistance(200.0);
        ride.setStartTime("2018-08-08T01:10:10");
        ride.setEndTime("2018-08-08T12:10:00");
        Person person = new Person();
        person.setId(1L);
        person.setEmail("AlexsandrPushkin@mail.com");
        person.setName("Alexsandr Pushkin");
        person.setRegistrationNumber("DK181100001");
        ride.setDriver(person);
        ride.setRider(person);

        when(rideService.save(ride)).thenReturn(new Ride());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/ride/createNewRide")
                .content(asJsonString(ride))
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testGetRide() throws Exception {
        RideServiceImpl rideService = new RideServiceImpl();
        when(rideService.findById(1L)).thenReturn(new Ride());
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/ride/getRide?rideId={rideId}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.rideId", is(1)))
                .andDo(print());
    }

    @Test
    public void testGetTopRides() throws Exception {
        RideServiceImpl rideService = new RideServiceImpl();
        when(rideService.getTopDriver(LocalDateTime.now().plusHours(24).plusMinutes(20), LocalDateTime.now()))
                .thenReturn(new ArrayList<TopDriverDTO>());
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/ride/getTopRides"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.max", is(5)))
                .andExpect(jsonPath("$.endTime", is(LocalDateTime.now().plusHours(24).plusMinutes(20))))
                .andExpect(jsonPath("$.startTime", is(LocalDateTime.now())))
                .andDo(print());
    }

}
