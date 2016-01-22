package st.ilu.rms4csw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import st.ilu.rms4csw.MockLoggedInUserHolder;
import st.ilu.rms4csw.TestContext;
import st.ilu.rms4csw.TestHelper;
import st.ilu.rms4csw.controller.base.advice.RestExceptionHandler;
import st.ilu.rms4csw.model.reservation.RoomReservation;
import st.ilu.rms4csw.model.reservation.TimeSpan;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.repository.reservation.RoomReservationRepository;
import st.ilu.rms4csw.repository.user.UserRepository;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Mischa Holz
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestContext.class)
@WebAppConfiguration
public class RoomReservationControllerTest {

    @Resource
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private RoomReservation one;

    private RoomReservation two;

    @Autowired
    private RoomReservationRepository roomReservationRepository;

    @Autowired
    private MockLoggedInUserHolder mockLoggedInUserHolder;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        roomReservationRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

        mockLoggedInUserHolder.setUp();

        User other = TestHelper.createUser();
        other = userRepository.save(other);

        one = new RoomReservation();
        one.setTimeSpan(new TimeSpan(new Date(100), new Date(200)));
        one.setApproved(true);
        one.setTitle("beschreibung");
        one.setOpen(false);
        one.setUser(mockLoggedInUserHolder.getLoggedInUser());

        two = new RoomReservation();
        two.setTimeSpan(new TimeSpan(new Date(201), new Date(300)));
        two.setTitle("beschreibung");
        two.setApproved(false);
        two.setOpen(false);
        two.setUser(other);

        roomReservationRepository.save(Arrays.asList(one, two));

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void tearDown() throws Exception {
        roomReservationRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    public void testFindAll() throws Exception {
        mockMvc.perform(get("/api/v1/roomreservations").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(one.getId())))
                .andExpect(jsonPath("$[1].id", is(two.getId())));

        assertNotEquals(one.getUser().getId(), two.getUser().getId());

        mockMvc.perform(get("/api/v1/roomreservations?user=" + one.getUser().getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(one.getId())));

        mockMvc.perform(get("/api/v1/roomreservations?user.DOESNOTEXIST.EVENLESS=bla").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testFindOne() throws Exception {
        mockMvc.perform(get("/api/v1/roomreservations/" + one.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(one.getId())))
                .andExpect(jsonPath("$.user.id", is(one.getUser().getId())));
    }

    @Test
    public void testOverlappingRoomReservations() throws Exception {
        RoomReservation three = new RoomReservation();
        three.setTimeSpan(new TimeSpan(new Date(150), new Date(500)));
        three.setOpen(true);
        three.setApproved(true);
        three.setTitle("titel");

        String json = mockMvc.perform(post("/api/v1/roomreservations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(three)))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse().getContentAsString();

        RestExceptionHandler.ValidationError error = objectMapper.readValue(json, RestExceptionHandler.ValidationError.class);
        Assert.assertFalse("RoomtReservations overlapping has to be a global error", error.getErrors().get(RestExceptionHandler.ValidationError.GLOBAL_ERROR_KEY).isEmpty());
    }

    @Test
    public void testPatchRoomReservation() throws Exception {
        RoomReservation three = new RoomReservation();
        three.setApproved(true);
        three.setId(null);

        mockMvc.perform(patch("/api/v1/roomreservations/" + two.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(three)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.approved", is(true)));
    }

    @Test
    public void testPostRoomReservation() throws Exception {
        RoomReservation three = new RoomReservation();
        three.setTimeSpan(new TimeSpan(new Date(401), new Date(500)));
        three.setOpen(true);
        three.setApproved(true);
        three.setTitle("beschreibung");
        three.setId(null);

        String location = mockMvc.perform(post("/api/v1/roomreservations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(three))
        )
                .andExpect(header().string("Location", Matchers.notNullValue()))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(get(location).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user.id", is(mockLoggedInUserHolder.getLoggedInUser().getId())))
                .andExpect(jsonPath("$.approved", is(false)))
                .andExpect(jsonPath("$.open", is(false)));
    }


    @Test
    public void testTimeSpanTest() throws Exception {

        String json = "{\"title\":\"Test\",\"timeSpan\":{\"beginning\":1453474800000,\"end\":1453478400000}," +
                "\"user\":{\"id\":\"ab2fa9b1a0ea4653b7024105edf34718\",\"loginName\":\"admin\",\"firstName\":\"Ilu\"," +
                "\"lastName\":\"St\",\"email\":\"bp@ilu.st\",\"phoneNumber\":\"\",\"role\":\"ADMIN\"}}";

        String location = mockMvc.perform(post("/api/v1/roomreservations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(header().string("Location", Matchers.notNullValue()))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(get(location).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user.id", is(mockLoggedInUserHolder.getLoggedInUser().getId())))
                .andExpect(jsonPath("$.approved", is(false)))
                .andExpect(jsonPath("$.open", is(false)));

    }

    @Test
    public void testDeleteRoomReservation() throws Exception {
        mockMvc.perform(delete("/api/v1/roomreservations/" + one.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/roomreservations/" + one.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
