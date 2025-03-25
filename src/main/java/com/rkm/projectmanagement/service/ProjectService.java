package com.rkm.projectmanagement.service;

import com.rkm.projectmanagement.entities.Project;
import com.rkm.projectmanagement.exception.ProjectNotFoundException;
import com.rkm.projectmanagement.repository.ProjectRepository;
import com.rkm.projectmanagement.utils.IdWorker;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final IdWorker idWorker;

    public Project findById(String projectId) {
        return this.projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

    public List<Project> findAll() {
        return this.projectRepository.findAll();
    }

    public Project save(Project newProject) {
        newProject.setId(String.valueOf(idWorker.nextId()));
        return this.projectRepository.save(newProject);
    }


    public Project update(String projectId, Project update) {
        Project projectFound = projectRepository.findById(projectId)
                .map(oldProject -> {
                    oldProject.setName(update.getName() == null ? oldProject.getName() : update.getName());
                    oldProject.setDescription(update.getDescription() == null ? oldProject.getDescription() : update.getDescription());
                    oldProject.setImageUrl(update.getImageUrl() == null ? oldProject.getImageUrl() : update.getImageUrl());
                    return projectRepository.save(oldProject);
                })
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        return projectFound;
    }

    public void delete(String projectId) {
        this.projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        this.projectRepository.deleteById(projectId);
    }
}
