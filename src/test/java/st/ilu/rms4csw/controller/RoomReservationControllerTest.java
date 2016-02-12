package st.ilu.rms4csw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.*;
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
import st.ilu.rms4csw.model.reservation.RepeatOption;
import st.ilu.rms4csw.model.reservation.RoomReservation;
import st.ilu.rms4csw.model.reservation.TimeSpan;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.repository.reservation.RoomReservationRepository;
import st.ilu.rms4csw.repository.user.UserRepository;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
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
    @Ignore
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
    @Ignore
    public void testRepeatRoomReservations() throws Exception {
        roomReservationRepository.deleteAllInBatch();

        RoomReservation three = new RoomReservation();
        three.setTimeSpan(new TimeSpan(new Date(0), new Date(500)));
        three.setTitle("beschreibung");
        three.setId(null);
        three.setRepeatOption(Optional.of(RepeatOption.WEEKLY));
        three.setRepeatUntil(Optional.of(new Date(3 * 7 * 24 * 60 * 60 * 1000 + 5)));

        String json = mockMvc.perform(post("/api/v1/roomreservations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(three)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        RoomReservation master = objectMapper.readValue(json, RoomReservation.class);
        String sharedId = master.getSharedId().orElseThrow(() -> new AssertionError("Shared Id needs to be present"));

        assertTrue(master.getSharedId().isPresent());

        //noinspection PointlessArithmeticExpression
        mockMvc.perform(get("/api/v1/roomreservations?approved=false&sort=timeSpan.beginning&direction=ASC").contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].timeSpan.beginning", is(0)))
                .andExpect(jsonPath("$[0].sharedId", is(sharedId)))
                .andExpect(jsonPath("$[1].timeSpan.beginning", is(1 * 7 * 24 * 60 * 60 * 1000)))
                .andExpect(jsonPath("$[1].sharedId", is(sharedId)))
                .andExpect(jsonPath("$[2].timeSpan.beginning", is(2 * 7 * 24 * 60 * 60 * 1000)))
                .andExpect(jsonPath("$[2].sharedId", is(sharedId)))
                .andExpect(jsonPath("$[3].timeSpan.beginning", is(3 * 7 * 24 * 60 * 60 * 1000)))
                .andExpect(jsonPath("$[3].sharedId", is(sharedId)));
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
    @Ignore
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
    public void testOpenRoomWithRoomReservation() throws Exception {
        RoomReservation open = new RoomReservation();
        open.setTimeSpan(new TimeSpan(new Date(new Date().getTime() - 30 * 60 * 1000), new Date(new Date().getTime() + 30 * 60 * 1000)));
        open.setOpen(true);
        open.setApproved(true);
        open.setTitle("title");
        open.setUser(mockLoggedInUserHolder.getLoggedInUser());

        roomReservationRepository.save(open);

        mockMvc.perform(patch("/api/v1/terminal/door")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"open\":true}"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Ignore
    public void testIllegalRepeatOption() throws Exception {
        RoomReservation three = new RoomReservation();
        three.setRepeatOption(Optional.of(RepeatOption.WEEKLY));

        mockMvc.perform(post("/api/v1/roomreservations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(three))
        )
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Ignore
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

    @Test
    public void testDeleteNonExistentRoomReservation() throws Exception {
        mockMvc.perform(delete("/api/v1/roomreservations/blabla")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
