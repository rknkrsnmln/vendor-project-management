package com.rkm.projectmanagement.system.converter;

import com.rkm.projectmanagement.dtos.VendorDto;
import com.rkm.projectmanagement.entities.Vendor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class VendorDtoToVendorConverter implements Converter<VendorDto, Vendor> {

    @Override
    public Vendor convert(VendorDto source) {
        Vendor vendor = new Vendor();
        vendor.setId(source.id());
        vendor.setName(source.name());
        return vendor;
    }
}
