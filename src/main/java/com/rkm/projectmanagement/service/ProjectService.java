package com.rkm.projectmanagement.service;

import com.rkm.projectmanagement.entities.Project;
import com.rkm.projectmanagement.exception.ObjectNotFoundException;
import com.rkm.projectmanagement.repository.ProjectRepository;
import com.rkm.projectmanagement.utils.IdWorker;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final IdWorker idWorker;

    public Project findById(String projectId) {
        return this.projectRepository.findById(projectId)
                .orElseThrow(() -> new ObjectNotFoundException("project", projectId));
    }

    public List<Project> findAll() {
        return this.projectRepository.findAll();
    }

    public Page<Project> findAll(Pageable pageable) {
        return this.projectRepository.findAll(pageable);
    }

    public Project save(Project newProject) {
        newProject.setId(String.valueOf(idWorker.nextId()));
        return this.projectRepository.save(newProject);
    }


    public Project update(String projectId, Project update) {
        Project projectFound = this.projectRepository.findById(projectId)
                .map(oldProject -> {
                    oldProject.setName(update.getName() == null ? oldProject.getName() : update.getName());
                    oldProject.setDescription(update.getDescription() == null ? oldProject.getDescription() : update.getDescription());
                    oldProject.setImageUrl(update.getImageUrl() == null ? oldProject.getImageUrl() : update.getImageUrl());
                    return projectRepository.save(oldProject);
                })
                .orElseThrow(() -> new ObjectNotFoundException("project", projectId));
        return projectFound;
    }

    public void delete(String projectId) {
        this.projectRepository.findById(projectId)
                .orElseThrow(() -> new ObjectNotFoundException("project", projectId));
        this.projectRepository.deleteById(projectId);
    }
}
