package com.rkm.projectmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rkm.projectmanagement.entities.Project;
import com.rkm.projectmanagement.entities.Vendor;
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
@DisplayName("Integration tests for Vendor API endpoints")
@Tag("integration")
public class VendorControllerIntegrationTest {

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
        System.out.println("this is the token " + this.token);
    }


    @Test
    @DisplayName("Check findAllVendors (GET)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testFindAllVendorsSuccess() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/vendors").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Finding All Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(3)));
    }

    @Test
    @DisplayName("Check findVendorById (GET)")
    void testFindVendorByIdSuccess() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/vendors/1").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Finding One Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("House of Armor"));
    }

    @Test
    @DisplayName("Check findArtifactById with non-existent id (GET)")
    void testFindVendorByIdNotFound() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/vendors/4").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find vendor with id of 4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Check addArtifact with valid input (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testAddVendorSuccess() throws Exception {
        Vendor a = new Vendor();
        a.setName("Whispering Herbs");

        String json = this.objectMapper.writeValueAsString(a);

        this.mockMvc.perform(MockMvcRequestBuilders.post(this.baseUrl +"/vendors").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Add Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("Whispering Herbs"));
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/vendors").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Finding All Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(4)));
    }

    @Test
    @DisplayName("Check addArtifact with invalid input (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testAddVendorErrorWithInvalidInput() throws Exception {
        Vendor a = new Vendor();
        a.setName("");

        String json = this.objectMapper.writeValueAsString(a);

        this.mockMvc.perform(MockMvcRequestBuilders.post(this.baseUrl + "/vendors").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("name is required."));
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/vendors").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Finding All Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(3)));
    }

    @Test
    @DisplayName("Check updateArtifact with valid input (PUT)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testUpdateVendorSuccess() throws Exception {
        Vendor a = new Vendor();
        a.setId(1);
        a.setName("Updated vendor name");

        String json = this.objectMapper.writeValueAsString(a);

        this.mockMvc.perform(MockMvcRequestBuilders.put(this.baseUrl + "/vendors/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Update Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("Updated vendor name"));
    }

    @Test
    @DisplayName("Check updateArtifact with non-existent id (PUT)")
    void testUpdateVendorErrorWithNonExistentId() throws Exception {
        Vendor a = new Vendor();
        a.setId(5); // This id does not exist in the database.
        a.setName("Updated vendor name");

        String json = this.objectMapper.writeValueAsString(a);

        this.mockMvc.perform(MockMvcRequestBuilders.put(this.baseUrl + "/vendors/5").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find vendor with id of 5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Check updateArtifact with invalid input (PUT)")
    void testUpdateVendorErrorWithInvalidInput() throws Exception {
        Vendor a = new Vendor();
        a.setId(1); // Valid id
        a.setName(""); // Updated name is empty.

        String json = this.objectMapper.writeValueAsString(a);

        this.mockMvc.perform(MockMvcRequestBuilders.put(this.baseUrl + "/vendors/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("name is required."));
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/vendors/1").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Finding One Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("House of Armor"));
    }

    @Test
    @DisplayName("Check deleteArtifact with valid input (DELETE)")
    void testDeleteVendorSuccess() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(this.baseUrl + "/vendors/1").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Delete Success"));
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/projects/1").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find project with id of 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Check deleteArtifact with non-existent id (DELETE)")
    void testDeleteVendorErrorWithNonExistentId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(this.baseUrl + "/vendors/5").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find vendor with id of 5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

}
