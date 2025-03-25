package com.rkm.projectmanagement.system.converter;

import com.rkm.projectmanagement.dtos.ProjectDto;
import com.rkm.projectmanagement.entities.Project;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProjectDtoToProjectConverter implements Converter<ProjectDto, Project> {

    @Override
    public Project convert(ProjectDto source) {
        Project project = new Project();
        project.setId(source.id());
        project.setName(source.name());
        project.setDescription(source.description());
        project.setImageUrl(source.imageUrl());
        return project;
    }
}
