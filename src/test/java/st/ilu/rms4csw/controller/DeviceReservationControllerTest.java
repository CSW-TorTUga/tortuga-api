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
import st.ilu.rms4csw.model.device.Device;
import st.ilu.rms4csw.model.devicecategory.DeviceCategory;
import st.ilu.rms4csw.model.reservation.DeviceReservation;
import st.ilu.rms4csw.model.reservation.TimeSpan;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.repository.device.DeviceCategoryRepository;
import st.ilu.rms4csw.repository.device.DeviceRepository;
import st.ilu.rms4csw.repository.reservation.DeviceReservationRepository;
import st.ilu.rms4csw.repository.user.UserRepository;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Mischa Holz
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestContext.class)
@WebAppConfiguration
public class DeviceReservationControllerTest {

    @Resource
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private Device device;

    private Device otherDevice;

    private User user;

    private User otherUser;

    private DeviceCategory deviceCategory;

    private DeviceReservation one;

    private DeviceReservation two;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeviceReservationRepository deviceReservationRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceCategoryRepository deviceCategoryRepository;

    @Autowired
    private MockLoggedInUserHolder mockLoggedInUserHolder;


    @Before
    public void setUp() throws Exception {
        for(DeviceReservation reservation : deviceReservationRepository.findAll()) {
            deviceReservationRepository.delete(reservation);
        }

        deviceRepository.deleteAllInBatch();
        deviceCategoryRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

        mockLoggedInUserHolder.setUp();

        deviceCategory = TestHelper.createDeviceCategory();
        deviceCategory = deviceCategoryRepository.save(deviceCategory);

        device = TestHelper.createDevice(deviceCategory);
        device = deviceRepository.save(device);

        otherDevice = TestHelper.createOtherDevice(deviceCategory);
        otherDevice = deviceRepository.save(otherDevice);

        otherUser = TestHelper.createUser();
        otherUser = userRepository.save(otherUser);

        user = mockLoggedInUserHolder.getLoggedInUser();

        one = new DeviceReservation();
        one.setTimeSpan(new TimeSpan(new Date(100), new Date(200)));
        one.setBorrowed(false);
        one.setDevice(device);
        one.setUser(user);
        one = deviceReservationRepository.save(one);

        two = new DeviceReservation();
        two.setTimeSpan(new TimeSpan(new Date(300), new Date(400)));
        two.setBorrowed(false);
        two.setDevice(otherDevice);
        two.setUser(otherUser);
        two = deviceReservationRepository.save(two);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void tearDown() throws Exception {
        for(DeviceReservation reservation : deviceReservationRepository.findAll()) {
            deviceReservationRepository.delete(reservation);
        }

        deviceRepository.deleteAllInBatch();
        deviceCategoryRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    public void testFindAll() throws Exception {
        mockMvc.perform(get("/api/v1/devicereservations").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(one.getId())))
                .andExpect(jsonPath("$[1].id", is(two.getId())));
    }

    @Test
    public void testFindOne() throws Exception {
        mockMvc.perform(get("/api/v1/devicereservations/" + one.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(one.getId())))
                .andExpect(jsonPath("$.user.id", is(one.getUser().getId())));
    }

    @Test
    public void testSuggestDeviceForReservationAndFindOldFavorite() throws Exception {
        mockMvc.perform(get("/api/v1/devices?beginningTime=600&endTime=700&category=" + deviceCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(one.getDevice().getId())))
                .andExpect(jsonPath("$[1].id", is(two.getDevice().getId())));
    }

    @Test
    public void testSuggestDeviceForReservationAndFindAvailableNonFavorite() throws Exception {
        mockMvc.perform(get("/api/v1/devices?beginningTime=50&endTime=150&category=" + deviceCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(two.getDevice().getId())));
    }

    @Test
    public void testSuggestDeviceForReservationAndFindNoOldReservations() throws Exception {
        deviceReservationRepository.deleteAllInBatch();

        mockMvc.perform(get("/api/v1/devices?beginningTime=50&endTime=150&category=" + deviceCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testPostDeviceReservation() throws Exception {
        DeviceReservation three = new DeviceReservation();
        three.setTimeSpan(new TimeSpan(new Date(401), new Date(500)));
        three.setBorrowed(false);
        three.setDevice(device);
        three.setUser(user);

        String location = mockMvc.perform(post("/api/v1/devicereservations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(three))
        )
                .andExpect(header().string("Location", Matchers.notNullValue()))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(get(location).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(three.getId())));
    }

    @Test
    public void testPostDeviceFromInactiveCategory() throws Exception {
        deviceReservationRepository.deleteAllInBatch();
        deviceRepository.deleteAllInBatch();
        deviceCategoryRepository.deleteAllInBatch();

        deviceCategory = TestHelper.createDeviceCategory();
        deviceCategory.setActive(false);

        deviceCategory = deviceCategoryRepository.save(deviceCategory);

        device = TestHelper.createDevice(deviceCategory);
        device = deviceRepository.save(device);

        DeviceReservation three = new DeviceReservation();
        three.setTimeSpan(new TimeSpan(new Date(900), new Date(950)));
        three.setBorrowed(false);
        three.setDevice(device);
        three.setUser(user);

        String json = mockMvc.perform(post("/api/v1/devicereservations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(three))
        ).andExpect(status().is4xxClientError())
                .andReturn().getResponse().getContentAsString();


        RestExceptionHandler.ValidationError error = objectMapper.readValue(json, RestExceptionHandler.ValidationError.class);
        Assert.assertFalse("Device from inactive category", error.getErrors().get("device").isEmpty());
    }

    @Test
    public void testPostOverlappingDeviceReservation() throws Exception {
        DeviceReservation three = new DeviceReservation();
        three.setTimeSpan(new TimeSpan(new Date(150), new Date(500)));
        three.setBorrowed(false);
        three.setDevice(device);
        three.setUser(user);

        mockMvc.perform(post("/api/v1/devicereservations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(three))
        ).andExpect(status().is4xxClientError())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testPostDeviceReservationOverMidnight() throws Exception {
        DeviceReservation three = new DeviceReservation();
        three.setTimeSpan(new TimeSpan(new Date(0), new Date(86_401_000)));
        three.setBorrowed(false);
        three.setDevice(device);
        three.setUser(user);
        three.setId(null);

        mockMvc.perform(post("/api/v1/devicereservations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(three))
        ).andExpect(status().is4xxClientError())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testPatchDeviceReservation() throws Exception {
        DeviceReservation patch = new DeviceReservation();
        patch.setId(null);
        patch.setTimeSpan(new TimeSpan(new Date(900), new Date(1000)));

        mockMvc.perform(patch("/api/v1/devicereservations/" + one.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patch))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(one.getId())))
                .andExpect(jsonPath("$.timeSpan.beginning", is(900)));
    }

    @Test
    public void testPatchInvalidDevice() throws Exception {
        DeviceReservation patch = new DeviceReservation();
        patch.setId(null);

        mockMvc.perform(patch("/api/v1/devicereservations/blabla")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patch))
        )
                .andExpect(status().isNotFound());
    }

    @Test
    public void testBorrowDevice() throws Exception {
        try (ByteArrayOutputStream outContent = new ByteArrayOutputStream()) {
            System.setOut(new PrintStream(outContent));

            DeviceReservation patch = new DeviceReservation();
            patch.setId(null);
            patch.setBorrowed(true);

            mockMvc.perform(patch("/api/v1/devicereservations/" + one.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(patch))
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(one.getId())))
                    .andExpect(jsonPath("$.borrowed", is(true)));

            patch.setBorrowed(false);

            mockMvc.perform(patch("/api/v1/devicereservations/" + one.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(patch))
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(one.getId())))
                    .andExpect(jsonPath("$.borrowed", is(false)));

            assertTrue(outContent.toString().contains("OPEN"));
        } finally {
            System.setOut(null);
        }

    }

    @Test
    public void testDeleteDeviceReservation() throws Exception {
        mockMvc.perform(delete("/api/v1/devicereservations/" + one.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/devicereservations/" + one.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
