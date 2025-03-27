package com.rkm.projectmanagement.controller;

import com.rkm.projectmanagement.dtos.ProjectDto;
import com.rkm.projectmanagement.dtos.ResultBaseDto;
import com.rkm.projectmanagement.dtos.VendorDto;
import com.rkm.projectmanagement.entities.Project;
import com.rkm.projectmanagement.entities.Vendor;
import com.rkm.projectmanagement.service.VendorService;
import com.rkm.projectmanagement.system.converter.VendorDtoToVendorConverter;
import com.rkm.projectmanagement.system.converter.VendorToVendorDtoConverter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}")
public class VendorController {

    public final VendorService vendorService;

    public final VendorToVendorDtoConverter vendorToVendorDtoConverter;

    public final VendorDtoToVendorConverter vendorDtoToVendorConverter;

    public VendorController(VendorService vendorService,
                            VendorToVendorDtoConverter vendorToVendorDtoConverter,
                            VendorDtoToVendorConverter vendorDtoToVendorConverter) {
        this.vendorService = vendorService;
        this.vendorToVendorDtoConverter = vendorToVendorDtoConverter;
        this.vendorDtoToVendorConverter = vendorDtoToVendorConverter;
    }

    @GetMapping("/vendors/{vendorId}")
    public ResponseEntity<ResultBaseDto<VendorDto>> findVendorById(@PathVariable(name = "vendorId") Integer vendorId) {
        Vendor vendorFound = this.vendorService.findById(vendorId);
        VendorDto vendorDto = this.vendorToVendorDtoConverter.convert(vendorFound);
//        return new ResultBaseDTO<ProjectDto>(true,"Finding One Success", HttpStatus.OK.value(), projectFound);
        return new ResponseEntity<>(ResultBaseDto.<VendorDto>builder()
                .flag(true)
                .message("Finding One Success")
                .code(HttpStatus.OK.value())
                .data(vendorDto)
                .build(), HttpStatus.OK);
    }

    @GetMapping("/vendors")
    public ResponseEntity<ResultBaseDto<List<VendorDto>>> findAllVendors() {
        List<Vendor> vendorsFound = this.vendorService.findAll();
        List<VendorDto> vendorDtos = vendorsFound.stream()
                .map(vendorDto -> this.vendorToVendorDtoConverter.convert(vendorDto))
                .collect(Collectors.toList());
        return new ResponseEntity<>(ResultBaseDto.<List<VendorDto>>builder()
                .flag(true)
                .message("Finding All Success")
                .code(HttpStatus.OK.value())
                .data(vendorDtos)
                .build(), HttpStatus.OK);
    }

    @PostMapping("/vendors")
    public ResponseEntity<ResultBaseDto<VendorDto>> addVendor(@RequestBody @Valid VendorDto vendorDto) {
        Vendor newVendor = this.vendorDtoToVendorConverter.convert(vendorDto);
        Vendor savedVendor = this.vendorService.save(newVendor);
        VendorDto savedVendorDto = this.vendorToVendorDtoConverter.convert(savedVendor);
        return new ResponseEntity<>(ResultBaseDto.<VendorDto>builder()
                .flag(true)
                .message("Add Success")
                .code(HttpStatus.CREATED.value())
                .data(savedVendorDto)
                .build(), HttpStatus.CREATED);
    }

    @PutMapping("/vendors/{vendorId}")
    public ResponseEntity<ResultBaseDto<VendorDto>> updateVendor(@PathVariable Integer vendorId, @Valid @RequestBody VendorDto vendorDto){
        Vendor update = this.vendorDtoToVendorConverter.convert(vendorDto);
        Vendor updatedVendor = this.vendorService.update(vendorId, update);
        VendorDto updatedVendorDto = this.vendorToVendorDtoConverter.convert(updatedVendor);
        return new ResponseEntity<>(ResultBaseDto.<VendorDto>builder()
                .flag(true)
                .message("Update Success")
                .code(HttpStatus.OK.value())
                .data(updatedVendorDto)
                .build(), HttpStatus.OK);
    }

    @DeleteMapping("/vendors/{vendorId}")
    public ResponseEntity<ResultBaseDto<String>> deleteVendor(@PathVariable Integer vendorId){
        this.vendorService.delete(vendorId);
        return new ResponseEntity<>(ResultBaseDto.<String>builder()
                .flag(true)
                .message("Delete Success")
                .data("Data with Id of " + vendorId + " deleted successfully")
                .code(HttpStatus.OK.value())
                .build(), HttpStatus.OK);
    }
}
