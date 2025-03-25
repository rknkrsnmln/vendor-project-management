package com.rkm.projectmanagement.dtos;

import jakarta.validation.constraints.NotEmpty;

public record ProjectDto(String id,
                         @NotEmpty(message = "name is required")
                         String name,
                         @NotEmpty(message = "description is required")
                         String description,
                         @NotEmpty(message = "imageUrl is required")
                         String imageUrl,
                         VendorDto owner) {
}
