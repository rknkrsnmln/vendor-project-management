package com.rkm.projectmanagement.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vendor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vendor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "owner")
    private List<Project> projects = new ArrayList<>();

    public void addProject(Project project) {
        project.setOwner(this);
        this.projects.add(project);
    }

    public Integer getNumberOfProjects() {
        return this.projects.size();
    }

    public void removeAllVendors() {
        this.projects.stream().forEach(project -> project.setOwner(null));
        this.projects = null;
    }

    public void removeProject(Project projectToBeAssigned) {
        // Remove artifact owner.
        projectToBeAssigned.setOwner(null);
        this.projects.remove(projectToBeAssigned);
    }

}
