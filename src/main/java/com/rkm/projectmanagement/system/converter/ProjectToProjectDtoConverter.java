package com.rkm.projectmanagement.system.converter;

import com.rkm.projectmanagement.dtos.ProjectDto;
import com.rkm.projectmanagement.entities.Project;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProjectToProjectDtoConverter implements Converter<Project, ProjectDto> {

    private final VendorToVendorDtoConverter vendorToVendorDtoConverter;

    public ProjectToProjectDtoConverter(VendorToVendorDtoConverter vendorToVendorDtoConverter) {
        this.vendorToVendorDtoConverter = vendorToVendorDtoConverter;
    }

    @Override
    public ProjectDto convert(Project source) {
        ProjectDto projectDto = new ProjectDto(source.getId(),
                source.getName(),
                source.getDescription(),
                source.getImageUrl(),
                source.getOwner() != null
                        ? this.vendorToVendorDtoConverter.convert(source.getOwner())
                        : null);
        return projectDto;
    }
}
