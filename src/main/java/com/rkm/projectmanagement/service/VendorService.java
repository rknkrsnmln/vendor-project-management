package com.rkm.projectmanagement.service;

import com.rkm.projectmanagement.entities.Vendor;
import com.rkm.projectmanagement.exception.ObjectNotFoundException;
import com.rkm.projectmanagement.exception.VendorNotFoundException;
import com.rkm.projectmanagement.repository.VendorRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class VendorService {

    public final VendorRepository vendorRepository;

    public Vendor findById(Integer vendorId) {
        return this.vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ObjectNotFoundException("vendor", vendorId));
    }

    public List<Vendor> findAll() {
        return this.vendorRepository.findAll();
    }

    public Vendor save(Vendor newVendor) {
//        newProject.setId(String.valueOf(idWorker.nextId()));
        return this.vendorRepository.save(newVendor);
    }


    public Vendor update(Integer vendorId, Vendor update) {
        Vendor vendorFound = this.vendorRepository.findById(vendorId)
                .map(oldVendor -> {
                    oldVendor.setName(update.getName() == null ? oldVendor.getName() : update.getName());
                    return vendorRepository.save(oldVendor);
                })
                .orElseThrow(() -> new ObjectNotFoundException("vendor", vendorId));
        return vendorFound;
    }

    public void delete(Integer vendorId) {
        Vendor vendorToBeDeleted = this.vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ObjectNotFoundException("vendor", vendorId));

        //Before deleting the vendor, we will unassign this vendor's owned projects.
        vendorToBeDeleted.removeAllVendors();
        this.vendorRepository.deleteById(vendorId);
    }
}
