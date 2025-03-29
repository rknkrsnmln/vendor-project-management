package com.rkm.projectmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rkm.projectmanagement.dtos.VendorDto;
import com.rkm.projectmanagement.entities.Project;
import com.rkm.projectmanagement.entities.Vendor;
import com.rkm.projectmanagement.exception.ObjectNotFoundException;
import com.rkm.projectmanagement.exception.VendorNotFoundException;
import com.rkm.projectmanagement.service.VendorService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@AutoConfigureMockMvc
@SpringBootTest
class VendorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    VendorService vendorService;

    @Autowired
    private ObjectMapper objectMapper;

    List<Vendor> vendors;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @Test
    void checkObjectMapper() {
        assertNotNull(objectMapper);
    }

    @BeforeEach
    void setUp() {
        Project a1 = new Project();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");

        Project a2 = new Project();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl");

        Project a3 = new Project();
        a3.setId("1250808601744904193");
        a3.setName("Elder Wand");
        a3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
        a3.setImageUrl("ImageUrl");

        Project a4 = new Project();
        a4.setId("1250808601744904194");
        a4.setName("The Marauder's Map");
        a4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        a4.setImageUrl("ImageUrl");

        Project a5 = new Project();
        a5.setId("1250808601744904195");
        a5.setName("The Sword Of Gryffindor");
        a5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
        a5.setImageUrl("ImageUrl");

        Project a6 = new Project();
        a6.setId("1250808601744904196");
        a6.setName("Resurrection Stone");
        a6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
        a6.setImageUrl("ImageUrl");

        this.vendors = new ArrayList<>();

        Vendor w1 = new Vendor();
        w1.setId(1);
        w1.setName("House of Armor");
        w1.addProject(a2);
        this.vendors.add(w1);

        Vendor w2 = new Vendor();
        w2.setId(2);
        w2.setName("Chest of Magic Devices");
        w2.addProject(a6);
        w2.addProject(a4);
        this.vendors.add(w2);

        Vendor w3 = new Vendor();
        w3.setId(3);
        w3.setName("The Tooth of The Sword");
        w3.addProject(a5);
        w3.addProject(a3);
        this.vendors.add(w3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findVendorByIdSuccess() throws Exception {
        //Given
        BDDMockito.given(this.vendorService.findById(1)).willReturn(this.vendors.getFirst());

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/vendors/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Finding One Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("House of Armor"));
    }

    @Test
    void testFindVendorByIdNotFound() throws Exception {
        //Given
//        BDDMockito.given(this.vendorService.findById(1)).willThrow(new VendorNotFoundException(1));
        BDDMockito.given(this.vendorService.findById(1)).willThrow(new ObjectNotFoundException("vendor", 1));

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/vendors/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find vendor with id of 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());

    }

    @Test
    void findAllVendors() throws Exception {
        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardService.
        BDDMockito.given(this.vendorService.findAll()).willReturn(this.vendors);

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/vendors").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Finding All Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(this.vendors.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("House of Armor"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name").value("Chest of Magic Devices"));
    }

    @Test
    void addVendorSuccess() throws Exception {
        VendorDto vendorDto = new VendorDto(null, "Chest of Magic Devices", 0);

        String json = this.objectMapper.writeValueAsString(vendorDto);

        Vendor savedVendor = new Vendor();
        savedVendor.setId(4);
        savedVendor.setName("Chest of Magic Devices");

        BDDMockito.given(this.vendorService.save(Mockito.any(Vendor.class))).willReturn(savedVendor);

        this.mockMvc.perform(MockMvcRequestBuilders.post(this.baseUrl + "/vendors").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Add Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("Chest of Magic Devices"));

    }

    @Test
    void updateVendorSuccess() throws Exception {
        VendorDto vendorDto = new VendorDto(null, "Updated vendor name", 0);

        Vendor updatedVendor = new Vendor();
        updatedVendor.setId(1);
        updatedVendor.setName("Updated vendor name");

        String json = this.objectMapper.writeValueAsString(vendorDto);

        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardService.
        BDDMockito.given(this.vendorService.update(ArgumentMatchers.eq(1), Mockito.any(Vendor.class))).willReturn(updatedVendor);

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.put(this.baseUrl + "/vendors/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Update Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("Updated vendor name"));
    }

    @Test
    void updateVendorErrorWithNonExistentId() throws Exception {
        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardService.
//        BDDMockito.given(this.vendorService.update(ArgumentMatchers.eq(5), Mockito.any(Vendor.class))).willThrow(new VendorNotFoundException(5));
        BDDMockito.given(this.vendorService.update(ArgumentMatchers.eq(5), Mockito.any(Vendor.class))).willThrow(new ObjectNotFoundException("vendor", 5));

        VendorDto vendorDto = new VendorDto(5, // This id does not exist in the database.
                "Updated vendor name",
                0);

        String json = this.objectMapper.writeValueAsString(vendorDto);

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.put(this.baseUrl + "/vendors/5").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find vendor with id of 5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    void deleteVendorSuccess() throws Exception {
        // Given
        Mockito.doNothing().when(this.vendorService).delete(5);

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.delete(this.baseUrl + "/vendors/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Delete Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value("Data with Id of 5 deleted successfully"));
    }

    @Test
    void deleteVendorErrorWithNonExistentId() throws Exception {
        // Given
//        doThrow(new VendorNotFoundException(5)).when(this.vendorService).delete(5);
        Mockito.doThrow(new ObjectNotFoundException("vendor", 5)).when(this.vendorService).delete(5);

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.delete(this.baseUrl + "/vendors/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find vendor with id of 5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignProjectSuccess() throws Exception {
        // Given
        Mockito.doNothing().when(this.vendorService).assignProject(2, "1250808601744904191");

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.put(this.baseUrl + "/vendors/2/projects/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Project Assignment Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value("Assignment Project of id 1250808601744904191 to Vendor with id of 2 done successfully"));
    }

    @Test
    void testAssignArtifactErrorWithNonExistentWizardId() throws Exception {
        // Given
        Mockito.doThrow(new ObjectNotFoundException("vendor", 5)).when(this.vendorService).assignProject(5, "1250808601744904191");

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.put(this.baseUrl + "/vendors/5/projects/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find vendor with id of 5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactErrorWithNonExistentArtifactId() throws Exception {
        // Given
        Mockito.doThrow(new ObjectNotFoundException("project", "1250808601744904199")).when(this.vendorService).assignProject(2, "1250808601744904199");

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.put(this.baseUrl + "/vendors/2/projects/1250808601744904199").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find project with id of 1250808601744904199"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }
}