package com.rkm.projectmanagement.controller;

import com.rkm.projectmanagement.dtos.ProjectDto;
import com.rkm.projectmanagement.dtos.ResultBaseDto;
import com.rkm.projectmanagement.entities.Project;
import com.rkm.projectmanagement.service.ProjectService;
import com.rkm.projectmanagement.system.converter.ProjectDtoToProjectConverter;
import com.rkm.projectmanagement.system.converter.ProjectToProjectDtoConverter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}")
public class ProjectController {

    private final ProjectService projectService;

    private final ProjectToProjectDtoConverter projectToProjectDtoConverter;

    private final ProjectDtoToProjectConverter projectDtoToProjectConverter;

    public ProjectController(ProjectService projectService,
                             ProjectToProjectDtoConverter projectToProjectDtoConverter,
                             ProjectDtoToProjectConverter projectDtoToProjectConverter) {
        this.projectService = projectService;
        this.projectToProjectDtoConverter = projectToProjectDtoConverter;
        this.projectDtoToProjectConverter = projectDtoToProjectConverter;
    }

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<ResultBaseDto<ProjectDto>> findProjectById(@PathVariable(name = "projectId") String projectId) {
        Project projectFound = this.projectService.findById(projectId);
        ProjectDto projectDto = this.projectToProjectDtoConverter.convert(projectFound);
//        return new ResultBaseDTO<ProjectDto>(true,"Finding One Success", HttpStatus.OK.value(), projectFound);
        return new ResponseEntity<>(ResultBaseDto.<ProjectDto>builder()
                .flag(true)
                .message("Finding One Success")
                .code(HttpStatus.OK.value())
                .data(projectDto)
                .build(), HttpStatus.OK);
    }

    @GetMapping("/projects")
    public ResponseEntity<ResultBaseDto<List<ProjectDto>>> findAllProjects() {
        List<Project> projectFound = this.projectService.findAll();
        List<ProjectDto> projectDtos = projectFound.stream()
                .map(projectDto -> this.projectToProjectDtoConverter.convert(projectDto))
                .collect(Collectors.toList());
        return new ResponseEntity<>(ResultBaseDto.<List<ProjectDto>>builder()
                .flag(true)
                .message("Finding All Success")
                .code(HttpStatus.OK.value())
                .data(projectDtos)
                .build(), HttpStatus.OK);
    }

    @PostMapping("/projects")
    public ResponseEntity<ResultBaseDto<ProjectDto>> addProject(@RequestBody @Valid ProjectDto projectDto) {
        Project newProject = this.projectDtoToProjectConverter.convert(projectDto);
        Project savedProject = this.projectService.save(newProject);
        ProjectDto savedProjectDto = this.projectToProjectDtoConverter.convert(savedProject);
        return new ResponseEntity<>(ResultBaseDto.<ProjectDto>builder()
                .flag(true)
                .message("Add Success")
                .code(HttpStatus.CREATED.value())
                .data(savedProjectDto)
                .build(), HttpStatus.CREATED);
    }

    @PutMapping("/projects/{projectId}")
    public ResponseEntity<ResultBaseDto<ProjectDto>> updateProject(@PathVariable String projectId, @Valid @RequestBody ProjectDto projectDto){
        Project update = this.projectDtoToProjectConverter.convert(projectDto);
        Project updatedArtifact = this.projectService.update(projectId, update);
        ProjectDto updatedArtifactDto = this.projectToProjectDtoConverter.convert(updatedArtifact);
        return new ResponseEntity<>(ResultBaseDto.<ProjectDto>builder()
                .flag(true)
                .message("Update Success")
                .code(HttpStatus.OK.value())
                .data(updatedArtifactDto)
                .build(), HttpStatus.OK);
    }

    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<ResultBaseDto<String>> deleteProject(@PathVariable String projectId){
        this.projectService.delete(projectId);
        return new ResponseEntity<>(ResultBaseDto.<String>builder()
                .flag(true)
                .message("Delete Success")
                .data("Data with Id of " + projectId + " deleted successfully")
                .code(HttpStatus.OK.value())
                .build(), HttpStatus.OK);
    }

}
