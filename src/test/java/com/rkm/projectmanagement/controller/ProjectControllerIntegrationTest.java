package com.rkm.projectmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rkm.projectmanagement.entities.Project;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration tests for Project API endpoints")
@Tag("integration")
public class ProjectControllerIntegrationTest {

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
    @DisplayName("Check findAllArtifacts (GET)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testFindAllProjectsSuccess() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/projects").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Finding All Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(6)));
    }

    @Test
    @DisplayName("Check findAllArtifacts (GET)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testFindAllPagedProjectsSuccess() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/projects/paged").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Finding All Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content", Matchers.hasSize(6)));
    }

    @Test
    @DisplayName("Check findArtifactById (GET)")
    void testFindProjectByIdSuccess() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/projects/1250808601744904191").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Finding One Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value("1250808601744904191"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("Deluminator"));
    }

    @Test
    @DisplayName("Check findArtifactById with non-existent id (GET)")
    void testFindProjectByIdNotFound() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/projects/1250808601744904199").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find project with id of 1250808601744904199"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Check addArtifact with valid input (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testAddProjectSuccess() throws Exception {
        Project a = new Project();
        a.setName("Remembrall");
        a.setDescription("A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered.");
        a.setImageUrl("ImageUrl");

        String json = this.objectMapper.writeValueAsString(a);

        this.mockMvc.perform(MockMvcRequestBuilders.post(this.baseUrl + "/projects").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Add Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("Remembrall"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value("A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.imageUrl").value("ImageUrl"));
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/projects").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Finding All Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(7)));
    }

    @Test
    @DisplayName("Check addArtifact with invalid input (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testAddArtifactErrorWithInvalidInput() throws Exception {
        Project a = new Project();
        a.setName(""); // Name is not provided.
        a.setDescription(""); // Description is not provided.
        a.setImageUrl(""); // ImageUrl is not provided.

        String json = this.objectMapper.writeValueAsString(a);

        this.mockMvc.perform(MockMvcRequestBuilders.post(this.baseUrl + "/projects").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("name is required"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value("description is required"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.imageUrl").value("imageUrl is required"));
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/projects").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Finding All Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(6)));
    }

    @Test
    @DisplayName("Check updateArtifact with valid input (PUT)")
    void testUpdateArtifactSuccess() throws Exception {
        Project a = new Project();
        a.setId("1250808601744904192");
        a.setName("Updated artifact name");
        a.setDescription("Updated description");
        a.setImageUrl("Updated imageUrl");

        String json = this.objectMapper.writeValueAsString(a);

        this.mockMvc.perform(MockMvcRequestBuilders.put(this.baseUrl + "/projects/1250808601744904192").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Update Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value("1250808601744904192"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("Updated artifact name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value("Updated description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.imageUrl").value("Updated imageUrl"));
    }

    @Test
    @DisplayName("Check updateArtifact with non-existent id (PUT)")
    void testUpdateArtifactErrorWithNonExistentId() throws Exception {
        Project a = new Project();
        a.setId("1250808601744904199"); // This id does not exist in the database.
        a.setName("Updated artifact name");
        a.setDescription("Updated description");
        a.setImageUrl("Updated imageUrl");

        String json = this.objectMapper.writeValueAsString(a);

        this.mockMvc.perform(MockMvcRequestBuilders.put(this.baseUrl + "/projects/1250808601744904199").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find project with id of 1250808601744904199"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Check updateArtifact with invalid input (PUT)")
    void testUpdateArtifactErrorWithInvalidInput() throws Exception {
        Project a = new Project();
        a.setId("1250808601744904191"); // Valid id
        a.setName(""); // Updated name is empty.
        a.setDescription(""); // Updated description is empty.
        a.setImageUrl(""); // Updated imageUrl is empty.

        String json = this.objectMapper.writeValueAsString(a);

        this.mockMvc.perform(MockMvcRequestBuilders.put(this.baseUrl + "/projects/1250808601744904191").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("name is required"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value("description is required"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.imageUrl").value("imageUrl is required"));
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/projects/1250808601744904191").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Finding One Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value("1250808601744904191"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("Deluminator"));
    }

    @Test
    @DisplayName("Check deleteArtifact with valid input (DELETE)")
    void testDeleteArtifactSuccess() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(this.baseUrl + "/projects/1250808601744904191").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Delete Success"));
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/projects/1250808601744904191").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find project with id of 1250808601744904191"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Check deleteArtifact with non-existent id (DELETE)")
    void testDeleteArtifactErrorWithNonExistentId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(this.baseUrl + "/projects/1250808601744904199").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find project with id of 1250808601744904199"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }


}
