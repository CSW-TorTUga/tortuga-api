package st.ilu.rms4csw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
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
import st.ilu.rms4csw.TestContext;
import st.ilu.rms4csw.TestHelper;
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
import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    private User user;

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


    @Before
    public void setUp() throws Exception {
        deviceReservationRepository.deleteAllInBatch();
        deviceRepository.deleteAllInBatch();
        deviceCategoryRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

        deviceCategory = TestHelper.createDeviceCategory();
        deviceCategory = deviceCategoryRepository.save(deviceCategory);

        device = TestHelper.createDevice(deviceCategory);
        device = deviceRepository.save(device);

        user = TestHelper.createUser();
        user = userRepository.save(user);

        one = new DeviceReservation();
        one.setTimeSpan(new TimeSpan(new Date(100), new Date(200)));
        one.setBorrowed(false);
        one.setDevice(device);
        one.setUser(user);
        one = deviceReservationRepository.save(one);

        two = new DeviceReservation();
        two.setTimeSpan(new TimeSpan(new Date(300), new Date(400)));
        two.setBorrowed(false);
        two.setDevice(device);
        two.setUser(user);
        two = deviceReservationRepository.save(two);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void tearDown() throws Exception {
        deviceReservationRepository.deleteAllInBatch();
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
}