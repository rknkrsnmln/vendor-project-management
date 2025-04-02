package com.rkm.projectmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rkm.projectmanagement.entities.User;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration tests for User API endpoints")
@Tag("integration")
public class UserControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String token;

    @Value("${api.endpoint.base-url}")
    String baseUrl;


    @BeforeEach
    void setUp() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post(this.baseUrl + "/users/login").with(SecurityMockMvcRequestPostProcessors.httpBasic("john", "123456")));
        MvcResult mvcResult = resultActions.andDo(MockMvcResultHandlers.print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        this.token = "Bearer " + json.getJSONObject("data").getString("token"); // Add "Bearer " as prefix.
        System.out.println("this is toke " + this.token);
    }

    @Test
    @DisplayName("Check findAllUsers (GET)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testFindAllUsersSuccess() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/users").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Finding All Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(3)));
    }

    @Test
    @DisplayName("Check findUserById (GET)")
    void testFindUserByIdSuccess() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/users/2").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Finding One Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("eric"));
    }

    @Test
    @DisplayName("Check findUserById with non-existent id (GET)")
    void testFindUserByIdNotFound() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/users/5").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find user with id of 5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Check addUser with valid input (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testAddUserSuccess() throws Exception {
        User usersUser = new User();
        usersUser.setUsername("lily");
        usersUser.setPassword("123456");
        usersUser.setEnabled(true);
        usersUser.setRoles("admin usersUser"); // The delimiter is space.

        String json = this.objectMapper.writeValueAsString(usersUser);

        this.mockMvc.perform(MockMvcRequestBuilders.post(this.baseUrl + "/users").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Adding Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("lily"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.enabled").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles").value("admin usersUser"));
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/users").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Finding All Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(4)));
    }

    @Test
    @DisplayName("Check addUser with invalid input (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testAddUserErrorWithInvalidInput() throws Exception {
        User usersUser = new User();
        usersUser.setUsername(""); // Username is not provided.
        usersUser.setPassword(""); // Password is not provided.
        usersUser.setRoles(""); // Roles field is not provided.

        String json = this.objectMapper.writeValueAsString(usersUser);

        this.mockMvc.perform(MockMvcRequestBuilders.post(this.baseUrl + "/users").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("username is required."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.password").value("password is required."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles").value("roles are required."));
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/users").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Finding All Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(3)));
    }

    @Test
    @DisplayName("Check updateUser with valid input (PUT)")
    void testUpdateUserSuccess() throws Exception {
        User usersUser = new User();
        usersUser.setUsername("tom123"); // Username is changed. It was tom.
        usersUser.setEnabled(false);
        usersUser.setRoles("user");

        String json = this.objectMapper.writeValueAsString(usersUser);

        this.mockMvc.perform(MockMvcRequestBuilders.put(this.baseUrl + "/users/3").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Updating Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("tom123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.enabled").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles").value("user"));
    }

    @Test
    @DisplayName("Check updateUser with non-existent id (PUT)")
    void testUpdateUserErrorWithNonExistentId() throws Exception {
        User usersUser = new User();
        usersUser.setId(5); // This id does not exist in the database.
        usersUser.setUsername("john123"); // Username is changed.
        usersUser.setEnabled(true);
        usersUser.setRoles("admin user");

        String json = this.objectMapper.writeValueAsString(usersUser);

        this.mockMvc.perform(MockMvcRequestBuilders.put(this.baseUrl + "/users/5").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find user with id of 5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Check updateUser with invalid input (PUT)")
    void testUpdateUserErrorWithInvalidInput() throws Exception {
        User usersUser = new User();
        usersUser.setId(1); // Valid id
        usersUser.setUsername(""); // Updated username is empty.
        usersUser.setRoles(""); // Updated roles field is empty.

        String json = this.objectMapper.writeValueAsString(usersUser);

        this.mockMvc.perform(MockMvcRequestBuilders.put(this.baseUrl + "/users/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("username is required."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles").value("roles are required."));
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/users/1").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Finding One Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("john"));
    }

    @Test
    @DisplayName("Check deleteUser with valid input (DELETE)")
    void testDeleteUserSuccess() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(this.baseUrl + "/users/2").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Deleting Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value("Delete user with id of 2 is successful"));
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/users/2").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find user with id of 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Check deleteUser with non-existent id (DELETE)")
    void testDeleteUserErrorWithNonExistentId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(this.baseUrl + "/users/5").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find user with id of 5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Check deleteUser with insufficient permission (DELETE)")
    void testDeleteUserNoAccessAsRoleUser() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post(this.baseUrl + "/users/login").with(SecurityMockMvcRequestPostProcessors.httpBasic("eric", "654321")));
        MvcResult mvcResult = resultActions.andDo(MockMvcResultHandlers.print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        String ericToken = "Bearer " + json.getJSONObject("data").getString("token");

        this.mockMvc.perform(MockMvcRequestBuilders.delete(this.baseUrl + "/users/2").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, ericToken))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.FORBIDDEN.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Access forbidden"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value("Error 403 - you don't have permission to access this resource."));
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/users").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Finding All Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].username").value("john"));
    }
}
