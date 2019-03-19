package com.shafitech.ppmtool.web;

import com.shafitech.ppmtool.domain.Project;
import com.shafitech.ppmtool.services.MapValidationErrorService;
import com.shafitech.ppmtool.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping
    public ResponseEntity<?> createNewProject(@Valid  @RequestBody Project project, BindingResult result, Principal principal){

        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidateError(result);
        if (errorMap!=null) return errorMap;

        Project project1 = projectService.saveOrUpdateProject(project, principal.getName());
        return new ResponseEntity<Project>(project1,HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public  ResponseEntity<?> findProjectById(@PathVariable String projectId, Principal principal){
       Project project = projectService.findByProjectId(projectId, principal.getName());
       return new ResponseEntity<Project>(project,HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Project> getAllProjects(Principal principal){
        return projectService.findAllProjects(principal.getName());
    }


    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal){
        projectService.deleteProjectById(projectId,principal.getName());
        return new ResponseEntity<String>("Project with Id '"+projectId+"' was deleted successfully",HttpStatus.OK);
    }


}
