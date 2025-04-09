package com.rkm.projectmanagement.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rkm.projectmanagement.dtos.ProjectDto;
import com.rkm.projectmanagement.entities.Project;
import com.rkm.projectmanagement.exception.ObjectNotFoundException;
import com.rkm.projectmanagement.exception.ProjectNotFoundException;
import com.rkm.projectmanagement.service.ProjectService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;



@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class ProjectControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void checkObjectMapper() {
        assertNotNull(objectMapper);
    }

    List<Project> projects;

    @Value("${api.endpoint.base-url}")
    String baseUrl;


    @BeforeEach
    void setUp() {
        this.projects = new ArrayList<>();

        Project a1 = new Project();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");
        this.projects.add(a1);

        Project a2 = new Project();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl");
        this.projects.add(a2);

        Project a3 = new Project();
        a3.setId("1250808601744904193");
        a3.setName("Elder Wand");
        a3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
        a3.setImageUrl("ImageUrl");
        this.projects.add(a3);

        Project a4 = new Project();
        a4.setId("1250808601744904194");
        a4.setName("The Marauder's Map");
        a4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        a4.setImageUrl("ImageUrl");
        this.projects.add(a4);

        Project a5 = new Project();
        a5.setId("1250808601744904195");
        a5.setName("The Sword Of Gryffindor");
        a5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
        a5.setImageUrl("ImageUrl");
        this.projects.add(a5);

        Project a6 = new Project();
        a6.setId("1250808601744904196");
        a6.setName("Resurrection Stone");
        a6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
        a6.setImageUrl("ImageUrl");
        this.projects.add(a6);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindProjectByIdSuccess() throws Exception {
        //Given
        BDDMockito.given(this.projectService.findById("1250808601744904191")).willReturn(this.projects.getFirst());

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/projects/1250808601744904191")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Finding One Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value("1250808601744904191"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("Deluminator"));

    }

    @Test
    void testFindProjectByIdNotFound() throws Exception {
        //Given
//        BDDMockito.given(this.projectService.findById("1250808601744904191")).willThrow(new ProjectNotFoundException("1250808601744904191"));
        BDDMockito.given(this.projectService.findById("1250808601744904191")).willThrow(new ObjectNotFoundException("project", "1250808601744904191"));

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/projects/1250808601744904191")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find project with id of 1250808601744904191"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());

    }

    @Test
    void testFindAllProjectsSuccess() throws Exception {
        //Given
        BDDMockito.given(this.projectService.findAll()).willReturn(this.projects);

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/projects")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Finding All Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(this.projects.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value("1250808601744904191"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("Deluminator"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id").value("1250808601744904192"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name").value("Invisibility Cloak"));
    }

    @Test
    void testFindAllPagedProjectsSuccess() throws Exception {
        //Given
        Pageable pageable = PageRequest.of(0, 20);
        PageImpl<Project> projectPage = new PageImpl<>(this.projects, pageable, this.projects.size());
        BDDMockito.given(this.projectService.findAll(Mockito.any(Pageable.class))).willReturn(projectPage);
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("page", "0");
        requestParams.add("size", "20");

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/projects/paged")
                        .accept(MediaType.APPLICATION_JSON)
                        .params(requestParams))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Finding All Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content", Matchers.hasSize(this.projects.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].id").value("1250808601744904191"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].name").value("Deluminator"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[1].id").value("1250808601744904192"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[1].name").value("Invisibility Cloak"));
    }

    @Test
    void testAddProjectSuccess() throws Exception {
        // Given
        ProjectDto projectDto = new ProjectDto(null,
                "Remembrall",
                "A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered.",
                "ImageUrl",
                null);
        String json = this.objectMapper.writeValueAsString(projectDto);
//        System.out.println(json);

//        String json = "{\n" +
//                "    \"name\": \"Remembrall\",\n" +
//                "    \"description\": \"A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered.\",\n" +
//                "    \"imageUrl\": \"ImageUrl\"\n" +
//                "}";
//        System.out.println(json);

        Project savedProject = new Project();
        savedProject.setId("1250808601744904197");
        savedProject.setName("Remembrall");
        savedProject.setDescription("A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered.");
        savedProject.setImageUrl("ImageUrl");

        BDDMockito.given(this.projectService.save(Mockito.any(Project.class))).willReturn(savedProject);

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.post(this.baseUrl + "/projects").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Add Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(savedProject.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value(savedProject.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.imageUrl").value(savedProject.getImageUrl()));
    }

    @Test
    void testUpdateProjectSuccess() throws Exception {
        // Given
        ProjectDto projectDto = new ProjectDto("1250808601744904192",
                "Invisibility Cloak",
                "A new description.",
                "ImageUrl",
                null);
        String json = this.objectMapper.writeValueAsString(projectDto);

//        String json = "{\n" +
//                "    \"id\": \"1250808601744904191\",\n" +
//                "    \"name\": \"Deluminator-update\",\n" +
//                "    \"description\": \"A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.-update\",\n" +
//                "    \"imageUrl\": \"ImageUrl-update-lagi\"\n" +
//                "}";

        Project updatedProject = new Project();
        updatedProject.setId("1250808601744904191");
        updatedProject.setName("Deluminator-update");
        updatedProject.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.-update");
        updatedProject.setImageUrl("ImageUrl-update-lagi");

        BDDMockito.given(this.projectService.update(ArgumentMatchers.eq("1250808601744904191"), Mockito.any(Project.class))).willReturn(updatedProject);

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.put(this.baseUrl + "/projects/1250808601744904191").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Update Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value("1250808601744904191"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(updatedProject.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value(updatedProject.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.imageUrl").value(updatedProject.getImageUrl()));
    }

    @Test
    void testUpdateProjectErrorWithNonExistentId() throws Exception {
        // Given
        ProjectDto projectDto = new ProjectDto("1250808601744904192",
                "Invisibility Cloak",
                "A new description.",
                "ImageUrl",
                null);
        String json = this.objectMapper.writeValueAsString(projectDto);

//        String json = "{\n" +
//                "    \"id\": \"1250808601744904192\",\n" +
//                "    \"name\": \"Invisibility Cloak\",\n" +
//                "    \"description\": \"A new description.\",\n" +
//                "    \"imageUrl\": \"ImageUrl\"\n" +
//                "}";

//        BDDMockito.given(this.projectService.update(ArgumentMatchers.eq("1250808601744904192"), Mockito.any(Project.class))).willThrow(new ProjectNotFoundException("1250808601744904192"));
        BDDMockito.given(this.projectService.update(ArgumentMatchers.eq("1250808601744904192"), Mockito.any(Project.class))).willThrow(new ObjectNotFoundException("project", "1250808601744904192"));

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.put(this.baseUrl + "/projects/1250808601744904192").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find project with id of 1250808601744904192"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());
    }

    @Test
    void testDeleteProjectSuccess() throws Exception {
        // Given
        Mockito.doNothing().when(this.projectService).delete("1250808601744904191");

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.delete(this.baseUrl + "/projects/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Delete Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value("Data with Id of 1250808601744904191 deleted successfully"));
    }

    @Test
    void testDeleteProjectErrorWithNonExistentId() throws Exception {
        // Given
//        doThrow(new ProjectNotFoundException("1250808601744904191")).when(this.projectService).delete("1250808601744904191");
        Mockito.doThrow(new ObjectNotFoundException("project", "1250808601744904191")).when(this.projectService).delete("1250808601744904191");

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.delete(this.baseUrl + "/projects/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find project with id of 1250808601744904191"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());
    }


}