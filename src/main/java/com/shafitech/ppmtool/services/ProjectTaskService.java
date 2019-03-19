package com.shafitech.ppmtool.services;

import com.shafitech.ppmtool.domain.Backlog;
import com.shafitech.ppmtool.domain.Project;
import com.shafitech.ppmtool.domain.ProjectTask;
import com.shafitech.ppmtool.exceptions.ProjectNotFoundException;
import com.shafitech.ppmtool.repositories.BacklogRepository;
import com.shafitech.ppmtool.repositories.ProjectRepository;
import com.shafitech.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;


    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {

       //PTs to be added to a specific project, project != null, BL exists
       Backlog backlog = projectService.findByProjectId(projectIdentifier,username).getBacklog();
               //backlogRepository.findByProjectIdentifier(projectIdentifier);
       //set the bl to pt
       projectTask.setBacklog(backlog);
       //we want our project sequence to be like this: IDPRO-1  IDPRO-2  ...100 101
       Integer BacklogSequence = backlog.getPTSequence();
       // Update the BL SEQUENCE
       BacklogSequence++;
       backlog.setPTSequence(BacklogSequence);
       //Add Sequence to Project Task
       projectTask.setProjectSequence(backlog.getProjectIdentifier() + "-" + BacklogSequence);
       projectTask.setProjectIdentifier(projectIdentifier);
       //INITIAL status when status is null
       if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
           projectTask.setStatus("TO_DO");
       }
       //INITIAL priority when priority null
       if (projectTask.getPriority() == null||projectTask.getPriority()== 0) { //In the future we need projectTask.getPriority()== 0 to handle the form
           projectTask.setPriority(3);
       }
       return projectTaskRepository.save(projectTask);

    }


    public Iterable<ProjectTask> findBacklogById(String id, String username){
        projectService.findByProjectId(id,username);
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username){
        //make sure we are searching on existing backlog
        projectService.findByProjectId(backlog_id,username);
        //make sure that our task exist
    ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
    if(projectTask==null){
        throw new ProjectNotFoundException("Project Task with ID '"+pt_id+"' not found");
    }
        //make sure that the our backlog/project id in the path corresponds to right project
    if(!projectTask.getProjectIdentifier().equalsIgnoreCase(backlog_id)){
        throw new ProjectNotFoundException("Project Task '"+pt_id+"' doesn't exist in project: '"+backlog_id+"'");
    }

        return projectTask;
    }


    //update project task
    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id, String username){
        //find existing project task
    ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id, username);
        //replace it with updated task
        projectTask = updatedTask;
        //save update
        return projectTaskRepository.save(projectTask);
    }


    public void deletePTByProjectSequence(String backlog_id,String pt_id,String username){
       ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id,username);
//        Backlog backlog = projectTask.getBacklog();
//        List<ProjectTask> pts = backlog.getProjectTasks();
//        pts.remove(projectTask);
//        backlogRepository.save(backlog);
       projectTaskRepository.delete(projectTask);
    }

    }
