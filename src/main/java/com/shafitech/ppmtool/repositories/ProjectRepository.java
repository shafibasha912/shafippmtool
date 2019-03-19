package com.shafitech.ppmtool.repositories;

import com.shafitech.ppmtool.domain.Project;
import org.springframework.data.repository.CrudRepository;


public interface ProjectRepository extends CrudRepository<Project,Long> {
    Project findByProjectIdentifier(String projectIdentifier);
    @Override
    Iterable<Project> findAll();
    Iterable<Project> findAllByProjectLeader(String username);
}
