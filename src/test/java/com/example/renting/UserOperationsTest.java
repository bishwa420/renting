package com.example.renting;

import com.example.renting.appuser.controller.UserController;
import com.example.renting.appuser.db.entity.User;
import com.example.renting.appuser.db.repo.UserRepository;
import com.example.renting.appuser.model.request.CreateUserRequest;
import com.example.renting.model.BasicRestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserOperationsTest {

    private static final Logger log = LoggerFactory.getLogger(UserOperationsTest.class);

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void init() {

        MockitoAnnotations.initMocks(this);

        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(getConflictUser());

        log.info("Before each method called & executed successfully");

    }

    private Optional<User> getConflictUser() {

        log.info("Conflict method called....");
        User user = new User();
        user.email = "abc@def.ghi";
        return Optional.of(user);
    }

    @Test
    public void contextLoads() throws Exception {

        assertThat(userController).isNotNull();
    }


    @Test
    public void whenSignupRequestBodyIsInvalid_expectResponseGivenAsBadRequest() throws Exception {

        CreateUserRequest request = new CreateUserRequest();
        request.email = "invalid";
        request.name = "John";
        request.role = "ADMIN";
        MvcResult result = this.mockMvc.perform(post("/user/create")
                                                .header("token", "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiI3OThjZDUzZWYxNzE0ZjgyODk4Yjk5YTJlMzQ4ZjM0MiIsInN1YiI6IkF1dGhlbnRpY2F0aW9uIHRva2VuIiwiZXhwIjoxNjEyNzE0ODY5LCJpYXQiOjE2MTI2Mjg0NjksImF1ZCI6ImlhbWJpc2h3YUBzdHVkZW50LnN1c3QuZWR1Iiwicm9sZSI6IkFETUlOIiwidXNlcklkIjoyfQ.WuIh9HdeqBFiiLf7Ig6SxjhNxBSeLW3ht7GTxrq954D_pEPZ9iiYsFosGrF8EzqomQEpUK4BIc65j14fHoWr1Q")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(new ObjectMapper().writeValueAsString(request)))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest())
                                        .andReturn();
        String content = result.getResponse().getContentAsString();

        BasicRestResponse response = new ObjectMapper().readValue(content, BasicRestResponse.class);

        assert(response.message.equals("Password must be given"));
    }

    //@Test
    public void whenSignupRequestHasDuplicateEmail_expectConflictResponseIsGiven() throws Exception {

        CreateUserRequest request = new CreateUserRequest();
        request.name = "John";
        request.email = "bishwa420@gmail.com";
        request.role = "REALTOR";

        MvcResult result = this.mockMvc.perform(post("/user")
                                                .content(new ObjectMapper().writeValueAsString(request))
                                                .contentType(MediaType.APPLICATION_JSON))
                                        .andDo(print())
                                        .andExpect(status().isConflict())
                                        .andReturn();

        String content = result.getResponse().getContentAsString();
        BasicRestResponse response = new ObjectMapper().readValue(content, BasicRestResponse.class);

        assertThat(response.message.equals("User with email: " + "a@b.c already exists"));
    }

    @Test
    public void whenUserListIsFetched_expectTheUserList() throws Exception {

    }
}
