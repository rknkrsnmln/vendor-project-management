package com.rkm.projectmanagement.system.converter;

import com.rkm.projectmanagement.dtos.VendorDto;
import com.rkm.projectmanagement.entities.Vendor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class VendorToVendorDtoConverter implements Converter<Vendor, VendorDto> {
    @Override
    public VendorDto convert(Vendor source) {
        VendorDto vendorDto = new VendorDto(
                source.getId(),
                source.getName(),
                source.getNumberOfProjects());
        return vendorDto;
    }
}
