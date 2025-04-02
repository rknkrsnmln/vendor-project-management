package com.rkm.projectmanagement.dtos;

import jakarta.validation.constraints.NotEmpty;

public record VendorDto(Integer id,
                        @NotEmpty(message = "name is required.")
                        String name,
                        Integer numberOfProject) {

}
