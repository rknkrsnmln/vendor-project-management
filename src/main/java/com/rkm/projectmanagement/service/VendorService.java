package com.rkm.projectmanagement.service;

import com.rkm.projectmanagement.entities.Project;
import com.rkm.projectmanagement.entities.Vendor;
import com.rkm.projectmanagement.exception.ObjectNotFoundException;
import com.rkm.projectmanagement.exception.VendorNotFoundException;
import com.rkm.projectmanagement.repository.ProjectRepository;
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

    public final ProjectRepository projectRepository;

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

    public void assignProject(Integer vendorId, String projectId) {
        // Find this project by id from DB.
        Project projectToBeAssigned = this.projectRepository.findById(projectId)
                .orElseThrow(() -> new ObjectNotFoundException("project", projectId));

        // Find this vendor by id fromm DB.
        Vendor vendor = this.vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ObjectNotFoundException("vendor", vendorId));

        // Artifact assignment
        // We need to see if the project is already owned by some vendor.
        if (projectToBeAssigned.getOwner() != null) {
            projectToBeAssigned.getOwner().removeProject(projectToBeAssigned);
        }
        vendor.addProject(projectToBeAssigned);
    }
}
