package com.rkm.projectmanagement.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rkm.projectmanagement.entities.Project;
import com.rkm.projectmanagement.entities.Vendor;
import com.rkm.projectmanagement.exception.ProjectNotFoundException;
import com.rkm.projectmanagement.repository.ProjectRepository;
import com.rkm.projectmanagement.utils.IdWorker;
import org.assertj.core.api.Assertions;
import org.hibernate.ObjectNotFoundException;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;


@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    ProjectRepository projectRepository;

    @Mock
    IdWorker idWorker;


    @InjectMocks
    ProjectService projectService;

    List<Project> projects;

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
    void testFindByIdSuccess() {
        //Given
        /*
        "id": "1250808601744904192",
        "name": "Invisibility Cloak Project",
        "description": "An invisibility cloak is used to make the wearer invisible.",
        "imageUrl": "ImageUrl",
        */
        Project p = new Project();
        p.setId("1250808601744904192");
        p.setName("Invisibility Cloak Project");
        p.setDescription("An invisibility cloak is used to make the wearer invisible.");
        p.setImageUrl("ImageUrl");

        Vendor v = new Vendor();
        v.setId(2);
        v.setName("Vendor of Armor");

        p.setOwner(v);

        BDDMockito.given(projectRepository.findById("1250808601744904192"))
                .willReturn(Optional.of(p));
        //When
//        Project projectReturned = projectService.findById(p.getId());
        Project projectReturned = projectService.findById("1250808601744904192");

        //Then
        Assertions.assertThat(projectReturned).isEqualTo(p);
        Assertions.assertThat(projectReturned.getId()).isEqualTo(p.getId());
        Assertions.assertThat(projectReturned.getName()).isEqualTo(p.getName());
        Assertions.assertThat(projectReturned.getImageUrl()).isEqualTo(p.getImageUrl());
        Assertions.assertThat(projectReturned.getDescription()).isEqualTo(p.getDescription());
        Mockito.verify(projectRepository, Mockito.times(1)).findById("1250808601744904192");
    }

    @Test
    void testFindByIdNotFound() {
        //Given
        BDDMockito.given(projectRepository.findById(Mockito.any(String.class)))
                .willReturn(Optional.empty());
        //When
        Throwable thrown = Assertions.catchThrowable(() -> {
            Project projectReturned = projectService.findById("1250808601744904192");
        });

        //Then
        Assertions.assertThat(thrown)
                .isInstanceOf(ProjectNotFoundException.class)
                .hasMessage("Could not found project with id of 1250808601744904192");
        Mockito.verify(projectRepository, Mockito.times(1)).findById("1250808601744904192");
    }

    @Test
    void testFindAllProjectSuccess() {
        //Given
        BDDMockito.given(projectRepository.findAll()).willReturn(this.projects);
        //When
        List<Project> actualProjects = this.projectService.findAll();
        //Then
        Assertions.assertThat(actualProjects).hasSize(this.projects.size());
        Mockito.verify(projectRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testSaveSuccess() {
        // Given
        Project newArtifact = new Project();
        newArtifact.setName("Artifact 3");
        newArtifact.setDescription("Description...");
        newArtifact.setImageUrl("ImageUrl...");

        BDDMockito.given(this.idWorker.nextId()).willReturn(123456L);
        BDDMockito.given(this.projectRepository.save(newArtifact)).willReturn(newArtifact);

        // When
        Project savedProject = this.projectService.save(newArtifact);

        // Then
        Assertions.assertThat(savedProject.getId()).isEqualTo("123456");
        Assertions.assertThat(savedProject.getName()).isEqualTo(newArtifact.getName());
        Assertions.assertThat(savedProject.getDescription()).isEqualTo(newArtifact.getDescription());
        Assertions.assertThat(savedProject.getImageUrl()).isEqualTo(newArtifact.getImageUrl());
        Mockito.verify(this.projectRepository, Mockito.times(1)).save(newArtifact);
    }

    @Test
    void testUpdateSuccess() {
        // Given
        Project oldProject = new Project();
        oldProject.setId("1250808601744904192");
        oldProject.setName("Invisibility Cloak");
        oldProject.setDescription("An invisibility cloak is used to make the wearer invisible.");
        oldProject.setImageUrl("ImageUrl");

        Project update = new Project();
        // update.setId("1250808601744904192");
        update.setName("Invisibility Cloak");
        update.setDescription("A new description.");
        update.setImageUrl("ImageUrl");

        BDDMockito.given(this.projectRepository.findById("1250808601744904192")).willReturn(Optional.of(oldProject));
        BDDMockito.given(this.projectRepository.save(oldProject)).willReturn(oldProject);

        // When
        Project updatedProject = this.projectService.update("1250808601744904192", update);

        // Then
        Assertions.assertThat(updatedProject.getId()).isEqualTo("1250808601744904192");
        Assertions.assertThat(updatedProject.getDescription()).isEqualTo(update.getDescription());
        Mockito.verify(this.projectRepository, Mockito.times(1)).findById("1250808601744904192");
        Mockito.verify(this.projectRepository, Mockito.times(1)).save(oldProject);
    }

    @Test
    void testUpdateNotFound() {
        // Given
        Project update = new Project();
        update.setName("Invisibility Cloak");
        update.setDescription("A new description.");
        update.setImageUrl("ImageUrl");

        BDDMockito.given(this.projectRepository.findById("1250808601744904192")).willReturn(Optional.empty());

        // When
        assertThrows(ProjectNotFoundException.class, () -> {
            this.projectService.update("1250808601744904192", update);
        });

        // Then
        Mockito.verify(this.projectRepository, Mockito.times(1)).findById("1250808601744904192");
    }

    @Test
    void testDeleteSuccess() {
        // Given
        Project project = new Project();
        project.setId("1250808601744904192");
        project.setName("Invisibility Cloak");
        project.setDescription("An invisibility cloak is used to make the wearer invisible.");
        project.setImageUrl("ImageUrl");

        BDDMockito.given(this.projectRepository.findById("1250808601744904192")).willReturn(Optional.of(project));
        doNothing().when(this.projectRepository).deleteById("1250808601744904192");

        // When
        this.projectService.delete("1250808601744904192");

        // Then
        Mockito.verify(this.projectRepository, Mockito.times(1)).deleteById("1250808601744904192");
    }

    @Test
    void testDeleteNotFound() {
        // Given
        BDDMockito.given(this.projectRepository.findById("1250808601744904192")).willReturn(Optional.empty());

        // When
        assertThrows(ProjectNotFoundException.class, () -> {
            this.projectService.delete("1250808601744904192");
        });

        // Then
        Mockito.verify(this.projectRepository, Mockito.times(1)).findById("1250808601744904192");
    }

}