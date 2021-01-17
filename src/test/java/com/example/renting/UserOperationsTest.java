package com.example.renting;

import com.example.renting.appuser.controller.UserController;
import com.example.renting.appuser.db.entity.User;
import com.example.renting.appuser.db.repo.UserRepository;
import com.example.renting.appuser.model.SignupRequest;
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

        Mockito.when(userRepository.findByEmail(Mockito.any()))
                .thenReturn(getConflictUser());

    }

    private Optional<User> getConflictUser() {

        User user = new User();
        user.email = "abc@def.ghi";
        return Optional.of(user);
    }

    @Test
    public void contextLoads() throws Exception {

        assertThat(userController).isNotNull();
    }


    @Test
    public void whenSignupRequestBodyIsInvalid_responseIsAlsoGivenAsBadRequest() throws Exception {

        SignupRequest request = new SignupRequest();
        request.email = "invalid";
        request.name = "John";
        request.password = "pass";
        request.role = "ADMIN";
        MvcResult result = this.mockMvc.perform(post("/user")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(new ObjectMapper().writeValueAsString(request)))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest())
                                        .andReturn();
        String content = result.getResponse().getContentAsString();

        BasicRestResponse response = new ObjectMapper().readValue(content, BasicRestResponse.class);
        assertThat(response.message.equals("Email address must be valid"));
    }

    @Test
    public void whenSignupRequestHasDuplicateEmail_ThenConflictResponseIsGiven() throws Exception {

        SignupRequest request = new SignupRequest();
        request.name = "John";
        request.email = "bishwa420@gmail.com";
        request.password = "pass";
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
}
