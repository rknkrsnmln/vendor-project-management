package com.rkm.projectmanagement.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "project")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project implements Serializable {

    @Id
    private String id;

    private String name;

    private String description;

    private String imageUrl;

    @ManyToOne
    private Vendor owner;


}
