package com.rkm.projectmanagement.service;

import com.rkm.projectmanagement.entities.Project;
import com.rkm.projectmanagement.entities.Vendor;
import com.rkm.projectmanagement.exception.ObjectNotFoundException;
import com.rkm.projectmanagement.exception.VendorNotFoundException;
import com.rkm.projectmanagement.repository.ProjectRepository;
import com.rkm.projectmanagement.repository.VendorRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class VendorServiceTest {

    @Mock
    VendorRepository vendorRepository;

    @Mock
    ProjectRepository projectRepository;

    @InjectMocks
    VendorService vendorService;


    List<Vendor> vendors;

    @BeforeEach
    void setUp() {
        Vendor w1 = new Vendor();
        w1.setId(1);
        w1.setName("House of Armor");

        Vendor w2 = new Vendor();
        w2.setId(2);
        w2.setName("Chest of Magic Devices");

        Vendor w3 = new Vendor();
        w3.setId(3);
        w3.setName("The Tooth of Sword");

        this.vendors = new ArrayList<>();
        this.vendors.add(w1);
        this.vendors.add(w2);
        this.vendors.add(w3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllSuccess() {
        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardRepository.
        BDDMockito.given(this.vendorRepository.findAll()).willReturn(this.vendors);

        // When. Act on the target behavior. Act steps should cover the method to be tested.
        List<Vendor> actualVendors = this.vendorService.findAll();

        // Then. Assert expected outcomes.
        Assertions.assertThat(actualVendors.size()).isEqualTo(this.vendors.size());

        // Verify wizardRepository.findAll() is called exactly once.
        Mockito.verify(this.vendorRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testFindByIdNotFound() {
        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardRepository.
        BDDMockito.given(this.vendorRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        // When. Act on the target behavior. Act steps should cover the method to be tested.
        Throwable thrown = Assertions.catchThrowable(() -> {
            Vendor returnedVendor = this.vendorService.findById(1);
        });

        // Then. Assert expected outcomes.
        Assertions.assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find vendor with id of 1");
        Mockito.verify(this.vendorRepository, Mockito.times(1)).findById(1);
    }

    @Test
    void testFindByIdSuccess() {
// Given. Arrange inputs and targets. Define the behavior of Mock object wizardRepository.
        Vendor w = new Vendor();
        w.setId(1);
        w.setName("House of Armor");

        BDDMockito.given(this.vendorRepository.findById(1)).willReturn(Optional.of(w)); // Define the behavior of the mock object.

        // When. Act on the target behavior. Act steps should cover the method to be tested.
        Vendor returnedVendor = this.vendorService.findById(1);

        // Then. Assert expected outcomes.
        Assertions.assertThat(returnedVendor.getId()).isEqualTo(w.getId());
        Assertions.assertThat(returnedVendor.getName()).isEqualTo(w.getName());
        Mockito.verify(this.vendorRepository, Mockito.times(1)).findById(1);
    }

    @Test
    void testSaveSuccess() {
        // Given
        Vendor newVendor = new Vendor();
        newVendor.setName("Chest of Magic Devices");

        BDDMockito.given(this.vendorRepository.save(newVendor)).willReturn(newVendor);

        // When
        Vendor returnedWizard = this.vendorService.save(newVendor);

        // Then
        Assertions.assertThat(returnedWizard.getName()).isEqualTo(newVendor.getName());
        Mockito.verify(this.vendorRepository, Mockito.times(1)).save(newVendor);
    }

    @Test
    void testUpdateSuccess() {
        // Given
        Vendor oldWizard = new Vendor();
        oldWizard.setId(1);
        oldWizard.setName("The Tooth of Sword");

        Vendor update = new Vendor();
        update.setName("The Tooth of Sword - update");

        BDDMockito.given(this.vendorRepository.findById(1)).willReturn(Optional.of(oldWizard));
        BDDMockito.given(this.vendorRepository.save(oldWizard)).willReturn(oldWizard);

        // When
        Vendor updatedWizard = this.vendorService.update(1, update);

        // Then
        Assertions.assertThat(updatedWizard.getId()).isEqualTo(1);
        Assertions.assertThat(updatedWizard.getName()).isEqualTo(update.getName());
        Mockito.verify(this.vendorRepository, Mockito.times(1)).findById(1);
        Mockito.verify(this.vendorRepository, Mockito.times(1)).save(oldWizard);
    }

    @Test
    void testUpdateNotFound() {
        // Given
        Vendor update = new Vendor();
        update.setName("The Tooth of Sword - update");

        BDDMockito.given(this.vendorRepository.findById(1)).willReturn(Optional.empty());

        // When
        assertThrows(ObjectNotFoundException.class, () -> {
            this.vendorService.update(1, update);
        });

        // Then
        Mockito.verify(this.vendorRepository, Mockito.times(1)).findById(1);
    }

    @Test
    void testDeleteSuccess() {
        // Given
        Vendor wizard = new Vendor();
        wizard.setId(1);
        wizard.setName("House of Armor");

        BDDMockito.given(this.vendorRepository.findById(1)).willReturn(Optional.of(wizard));
        doNothing().when(this.vendorRepository).deleteById(1);

        // When
        this.vendorService.delete(1);

        // Then
        Mockito.verify(this.vendorRepository, Mockito.times(1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound() {
        // Given
        BDDMockito.given(this.vendorRepository.findById(1)).willReturn(Optional.empty());

        // When
        assertThrows(ObjectNotFoundException.class, () -> {
            this.vendorService.delete(1);
        });

        // Then
        Mockito.verify(this.vendorRepository, Mockito.times(1)).findById(1);

    }

    @Test
    void testAssignArtifactSuccess() {
// Given
        Project a = new Project();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Vendor w2 = new Vendor();
        w2.setId(2);
        w2.setName("Chest of Magic Devices");
        w2.addProject(a);

        Vendor w3 = new Vendor();
        w3.setId(3);
        w3.setName("House of Armor");

        BDDMockito.given(this.projectRepository.findById("1250808601744904192")).willReturn(Optional.of(a));
        BDDMockito.given(this.vendorRepository.findById(3)).willReturn(Optional.of(w3));

        // When
        this.vendorService.assignProject(3, "1250808601744904192");

        // Then
        Assertions.assertThat(a.getOwner().getId()).isEqualTo(3);
        Assertions.assertThat(w3.getProjects()).contains(a);
    }

    @Test
    void testAssignVendorIdNotFound() {
        // Given
        Project a = new Project();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Vendor w2 = new Vendor();
        w2.setId(2);
        w2.setName("Chest of Magic Devices");
        w2.addProject(a);

        BDDMockito.given(this.projectRepository.findById("1250808601744904192")).willReturn(Optional.of(a));
        BDDMockito.given(this.vendorRepository.findById(3)).willReturn(Optional.empty());

        // When
        ObjectNotFoundException thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.vendorService.assignProject(3, "1250808601744904192");
        });

        // Then
        Assertions.assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find vendor with id of 3");
        Assertions.assertThat(a.getOwner().getId()).isEqualTo(2);
        Mockito.verify(this.projectRepository, Mockito.times(1)).findById("1250808601744904192");


    }

    @Test
    void testAssignProjectIdNotFound() {
        // Given
        Project a = new Project();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Vendor w2 = new Vendor();
        w2.setId(2);
        w2.setName("Chest of Magic Devices");
        w2.addProject(a);

        BDDMockito.given(this.projectRepository.findById("1250808601744904191")).willReturn(Optional.empty());

        // When
        ObjectNotFoundException thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.vendorService.assignProject(2, "1250808601744904191");
        });

        // Then
        Assertions.assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find project with id of 1250808601744904191");
        Assertions.assertThat(a.getOwner().getId()).isEqualTo(2);

    }
}