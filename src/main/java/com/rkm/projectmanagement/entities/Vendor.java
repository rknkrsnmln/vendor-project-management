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
}
