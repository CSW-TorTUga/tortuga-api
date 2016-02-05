package st.ilu.rms4csw.controller;

import com.fasterxml.jackson.core.type.TypeReference;
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
import st.ilu.rms4csw.MockLoggedInUserHolder;
import st.ilu.rms4csw.TestContext;
import st.ilu.rms4csw.TestHelper;
import st.ilu.rms4csw.controller.base.advice.RestExceptionHandler;
import st.ilu.rms4csw.model.major.Major;
import st.ilu.rms4csw.model.terminal.PasscodeAuthenticationRequest;
import st.ilu.rms4csw.model.user.Gender;
import st.ilu.rms4csw.model.user.Role;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.repository.user.MajorRepository;
import st.ilu.rms4csw.repository.user.UserRepository;

import javax.annotation.Resource;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private MockLoggedInUserHolder loggedInUserHolder;

    private MockMvc mockMvc;

    private User user1;

    private User user2;

    private Major major1;

    @Before
    public void setUp() throws Exception {
        userRepository.deleteAllInBatch();
        majorRepository.deleteAllInBatch();

        loggedInUserHolder.setUp();
        user1 = loggedInUserHolder.getLoggedInUser();

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
        user2.setEnabled(true);

        userRepository.save(Arrays.asList(user2));

        major1 = majorRepository.save(TestHelper.createMajor());

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void tearDown() throws Exception {
        userRepository.deleteAllInBatch();
        majorRepository.deleteAllInBatch();
    }

    @Test
    public void testFindAll() throws Exception {
        mockMvc.perform(get("/api/v1/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)));
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
        user3.setRole(Role.ADMIN);
        user3.setFirstName("Team");
        user3.setLastName("Teamington");
        user3.setGender(Optional.of(Gender.FEMALE));
        user3.setStudentId(Optional.empty());
        user3.setMajor(Optional.empty());
        user3.setEmail("testuser@ilu.st");
        user3.setLoginName("test_user");
        user3.setPassword("change me.");
        user3.setId(null);
        user3.setEnabled(true);

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user3)))

                .andExpect(header().string("Location", Matchers.notNullValue()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.loginName", is("test_user")));
    }

    @Test
    public void testPostUserWithExistingEmail() throws Exception {
        User user3 = new User();
        user3.setExpirationDate(Optional.empty());
        user3.setPhoneNumber("123456789");
        user3.setRole(Role.ADMIN);
        user3.setFirstName("Team");
        user3.setLastName("Teamington");
        user3.setGender(Optional.of(Gender.FEMALE));
        user3.setStudentId(Optional.empty());
        user3.setMajor(Optional.empty());
        user3.setEmail("team@ilu.st");
        user3.setLoginName("some_other_user_totally");
        user3.setPassword("change me.");
        user3.setId(null);

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user3)))

                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGeneratePasscode() throws Exception {
        getPasscode();
    }

    private String getPasscode() throws Exception {
        String json = mockMvc.perform(post("/api/v1/users/" + loggedInUserHolder.getLoggedInUser().getId() + "/passcode")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Map<String, Object> response = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});

        assertNotNull(response.get("passcode"));

        @SuppressWarnings("unchecked")
        List<String> code = (List<String>) response.get("passcode");
        assertTrue(code.size() == 5);

        return code.stream().reduce("", (a, b) -> a + b);
    }

    @Test
    public void testAuthenticateWithPasscode() throws Exception {
        String passcode = getPasscode();

        PasscodeAuthenticationRequest par = new PasscodeAuthenticationRequest();
        par.setPasscode(passcode);

        mockMvc.perform(post("/api/v1/terminal/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(par)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testNonExistentPasscode() throws Exception {
        PasscodeAuthenticationRequest par = new PasscodeAuthenticationRequest();
        par.setPasscode("blabla");

        mockMvc.perform(post("/api/v1/terminal/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(par)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGeneratePasscodeForExistingUserButNotMyself() throws Exception {
        mockMvc.perform(post("/api/v1/users/" + user2.getId() + "/passcode")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isOk());
    }

    @Test
    public void testGeneratePasscodeForNonExistingUser() throws Exception {
        mockMvc.perform(post("/api/v1/users/blabla/passcode")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPostStudentWithoutMajor() throws Exception {
        User user3 = new User();
        user3.setExpirationDate(Optional.empty());
        user3.setPhoneNumber("123456789");
        user3.setRole(Role.STUDENT);
        user3.setFirstName("");
        user3.setLastName("studentington");
        user3.setGender(Optional.of(Gender.FEMALE));
        user3.setStudentId(Optional.empty());
        user3.setMajor(Optional.empty());
        user3.setEmail("testuser@ilu.st");
        user3.setLoginName("test_user");
        user3.setPassword("change me.");
        user3.setId(null);
        user3.setEnabled(true);

        String json = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user3)))

                .andExpect(status().is4xxClientError())
                .andReturn().getResponse().getContentAsString();

        RestExceptionHandler.ValidationError validationError = objectMapper.readValue(json, RestExceptionHandler.ValidationError.class);
        assertFalse("There has to be an error message for the major field", validationError.getErrors().get("major").isEmpty());
        assertFalse("There has to be an error message for the firstName field", validationError.getErrors().get("firstName").isEmpty());
        assertFalse("There has to be an error message for the studentId field", validationError.getErrors().get("studentId").isEmpty());
        assertEquals("There have to be 3 errors", validationError.getErrors().size(), 3);
    }

    @Test
    public void testUserNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/users/asas").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
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
        user3.setStudentId(Optional.of("1234567"));
        user3.setMajor(Optional.of(major1));
        user3.setEmail("testuser@ilu.st");
        user3.setLoginName("test_user2");
        user3.setPassword("change me.");
        user3.setId(null);
        user3.setEnabled(true);

        String location = mockMvc.perform(post("/api/v1/users")
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

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/v1/users/" + user1.getId())).andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/users/" + user1.getId())).andExpect(status().isNotFound());
    }

    @Test
    public void testPatchUserWithExpirationDate() throws Exception {
        User patch = new User();
        patch.setId(null);
        patch.setExpirationDate(Optional.of(new Date()));
        patch.setRole(Role.STUDENT);
        patch.setMajor(Optional.of(major1));
        patch.setStudentId(Optional.of("1234"));

        String json = mockMvc.perform(patch("/api/v1/users/" + user1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patch))
        )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        User returned = objectMapper.readValue(json, User.class);

        assertTrue(returned.getExpirationDate().isPresent());
        assertTrue(returned.getRole() == Role.STUDENT);

        patch = new User();
        patch.setId(null);
        patch.setEmail("blabla@bla.de");
        patch.setExpirationDate(Optional.empty());

        json = mockMvc.perform(patch("/api/v1/users/" + user1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patch))
        )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        returned = objectMapper.readValue(json, User.class);

        assertTrue(returned.getExpirationDate().isPresent());
    }

    @Test
    public void testPatchNonExistentUser() throws Exception {
        User patch = new User();
        patch.setId(null);
        patch.setEmail("bla@ilu.st");

        mockMvc.perform(patch("/api/v1/users/blabla")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPatchUser() throws Exception {
        User patch = new User();
        patch.setId(null);
        patch.setEmail("bla@ilu.st");

        mockMvc.perform(patch("/api/v1/users/" + user1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loginName", is(user1.getLoginName())))
                .andExpect(jsonPath("$.email", is("bla@ilu.st")));
    }
}
