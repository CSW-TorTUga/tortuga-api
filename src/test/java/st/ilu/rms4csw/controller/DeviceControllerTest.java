package st.ilu.rms4csw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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
import st.ilu.rms4csw.model.cabinet.Cabinet;
import st.ilu.rms4csw.model.device.Device;
import st.ilu.rms4csw.model.devicecategory.DeviceCategory;
import st.ilu.rms4csw.repository.device.DeviceCategoryRepository;
import st.ilu.rms4csw.repository.device.DeviceRepository;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Mischa Holz
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestContext.class)
@WebAppConfiguration
public class DeviceControllerTest {

    @Resource
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private Device device1;

    private Device device2;

    DeviceCategory deviceCategory;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceCategoryRepository deviceCategoryRepository;

    @Before
    public void setUp() throws Exception {
        deviceRepository.deleteAllInBatch();
        deviceCategoryRepository.deleteAllInBatch();

        deviceCategory = new DeviceCategory();
        deviceCategory.setName("Category");

        deviceCategory = deviceCategoryRepository.save(deviceCategory);

        device1 = new Device();
        device1.setName("Device 1");
        device1.setAccessories("Accessories");
        device1.setAcquisitionDate(Optional.of(new Date()));
        device1.setCabinet(Cabinet.CABINET_6);
        device1.setCategory(deviceCategory);
        device1.setDescription("description");
        device1.setInventoryNumber("1234");

        device2 = new Device();
        device2.setName("Device 2");
        device2.setAccessories("Accessories");
        device2.setAcquisitionDate(Optional.of(new Date()));
        device2.setCabinet(Cabinet.CABINET_6);
        device2.setCategory(deviceCategory);
        device2.setDescription("description");
        device2.setInventoryNumber("1234");

        deviceRepository.save(Arrays.asList(device1, device2));

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void tearDown() throws Exception {
        deviceRepository.deleteAllInBatch();
        deviceCategoryRepository.deleteAllInBatch();
    }

    @Test
    public void testFindAll() throws Exception {
        mockMvc.perform(get("/api/v1/devices").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(device1.getId())))
                .andExpect(jsonPath("$[0].name", is(device1.getName())))
                .andExpect(jsonPath("$[1].id", is(device2.getId())));

        mockMvc.perform(get("/api/v1/devices?name=" + device1.getName()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(device1.getId())))
                .andExpect(jsonPath("$[0].name", is(device1.getName())));
    }

    @Test
    public void testFindOne() throws Exception {
        mockMvc.perform(get("/api/v1/devices/" + device1.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(device1.getId())))
                .andExpect(jsonPath("$.name", is(device1.getName())));
    }

    @Test
    public void testPostDevice() throws Exception {
        Device device3 = new Device();
        device3.setName("Device 3");
        device3.setAccessories("Accessories");
        device3.setAcquisitionDate(Optional.of(new Date()));
        device3.setCabinet(Cabinet.CABINET_6);
        device3.setCategory(deviceCategory);
        device3.setDescription("description");
        device3.setInventoryNumber("1234");
        device3.setId(null);

        String location = mockMvc.perform(post("/api/v1/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(device3))
        )
                .andExpect(jsonPath("$.name", is(device3.getName())))
                .andExpect(header().string("Location", Matchers.notNullValue()))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(get(location).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(device3.getName())));
    }

    @Test
    public void testPutDevice() throws Exception {
        device2.setName("Device 3");

         mockMvc.perform(put("/api/v1/devices/" + device2.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(device2))
        )
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$.id", is(device2.getId())))
                 .andExpect(jsonPath("$.name", is(device2.getName())));
    }

    @Test
    public void testPatchDevice() throws Exception {
        Device devicePatch = new Device();
        devicePatch.setName("Device3");
        devicePatch.setId(null);

        mockMvc.perform(patch("/api/v1/devices/" + device1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(devicePatch))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(device1.getId())))
                .andExpect(jsonPath("$.name", is("Device3")));
    }

    @Test
    public void testDeleteDevice() throws Exception {
        mockMvc.perform(delete("/api/v1/devices/" + device1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/devices/" + device1.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
