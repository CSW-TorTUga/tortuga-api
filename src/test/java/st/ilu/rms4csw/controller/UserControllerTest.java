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
import st.ilu.rms4csw.model.user.Gender;
import st.ilu.rms4csw.model.user.Role;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.repository.user.UserRepository;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Mischa Holz
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestContext.class)
@WebAppConfiguration
public class UserControllerTest {

    @Resource
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private User user1;

    private User user2;

    @Before
    public void setUp() throws Exception {
        userRepository.deleteAllInBatch();

        user1 = new User();
        user1.setExpirationDate(Optional.empty());
        user1.setPhoneNumber("123456789");
        user1.setRole(Role.ADMIN);
        user1.setFirstName("Admin");
        user1.setLastName("Admington");
        user1.setGender(Optional.of(Gender.FEMALE));
        user1.setStudentId(Optional.empty());
        user1.setMajor(Optional.empty());
        user1.setEmail("admin@ilu.st");
        user1.setLoginName("admin");
        user1.setPassword("change me.");

        user2 = new User();
        user2.setExpirationDate(Optional.empty());
        user2.setPhoneNumber("123456789");
        user2.setRole(Role.CSW_TEAM);
        user2.setFirstName("Team");
        user2.setLastName("Teamington");
        user2.setGender(Optional.of(Gender.FEMALE));
        user2.setStudentId(Optional.empty());
        user2.setMajor(Optional.empty());
        user2.setEmail("team@ilu.st");
        user2.setLoginName("team");
        user2.setPassword("change me.");

        userRepository.save(Arrays.asList(user1, user2));

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void tearDown() throws Exception {
        userRepository.deleteAllInBatch();
    }

    @Test
    public void testFindAll() throws Exception {
        mockMvc.perform(get("/api/v1/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(user1.getId())))
                .andExpect(jsonPath("$[0].loginName", is(user1.getLoginName())))
                .andExpect(jsonPath("$[1].id", is(user2.getId())));
    }

    @Test
    public void testFindUser1() throws Exception {
        mockMvc.perform(get("/api/v1/users/" + user1.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(user1.getId())))
                .andExpect(jsonPath("$.loginName", is(user1.getLoginName())));
    }

    @Test
    public void testPostUser() throws Exception {
        User user3 = new User();
        user3.setExpirationDate(Optional.empty());
        user3.setPhoneNumber("123456789");
        user3.setRole(Role.CSW_TEAM);
        user3.setFirstName("Team");
        user3.setLastName("Teamington");
        user3.setGender(Optional.of(Gender.FEMALE));
        user3.setStudentId(Optional.empty());
        user3.setMajor(Optional.empty());
        user3.setEmail("testuser@ilu.st");
        user3.setLoginName("test_user");
        user3.setPassword("change me.");
        user3.setId(null);

        mockMvc .perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user3)))

                .andExpect(header().string("Location", Matchers.notNullValue()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.loginName", is("test_user")));
    }

    @Test
    public void testUserNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/users/asas").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPutUser() throws Exception {

    }

    @Test
    public void testPostStudent() throws Exception {
        User user3 = new User();
        user3.setExpirationDate(Optional.empty());
        user3.setPhoneNumber("123456789");
        user3.setRole(Role.STUDENT);
        user3.setFirstName("Team");
        user3.setLastName("Teamington");
        user3.setGender(Optional.of(Gender.FEMALE));
        user3.setStudentId(Optional.empty());
        user3.setMajor(Optional.empty());
        user3.setEmail("testuser@ilu.st");
        user3.setLoginName("test_user2");
        user3.setPassword("change me.");
        user3.setId(null);

        String location = mockMvc .perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user3)))

                .andExpect(header().string("Location", Matchers.notNullValue()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.loginName", is("test_user2")))
                .andReturn().getResponse().getHeader("Location");

        User userReturned = objectMapper.readValue(mockMvc.perform(get(location)
            .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse().getContentAsString(), User.class);

        assertTrue("A student must have an expiration date and it has to be in the future", new Date().before(userReturned.getExpirationDate().orElseThrow(NullPointerException::new)));
    }
}
