package vn.globits.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.globits.demo.domain.Company;
import vn.globits.demo.domain.Person;
import vn.globits.demo.domain.Project;
import vn.globits.demo.dto.ProjectDTO;
import vn.globits.demo.repository.CompanyRepository;
import vn.globits.demo.repository.PersonRepository;
import vn.globits.demo.repository.ProjectRepository;
import vn.globits.demo.service.ProjectService;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private PersonRepository personRepository;

    @Override
    public Page<ProjectDTO> getPage(Pageable pageable) {
        return projectRepository.findAll(pageable).map(ProjectDTO::new);
    }

    @Override
    public ProjectDTO getById(Long id) {
        Project p = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        return new ProjectDTO(p);
    }

    @Override
    public ProjectDTO create(ProjectDTO dto) {
        if (dto.getCode() == null || dto.getCode().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code is required");
        }
        if (projectRepository.existsByCode(dto.getCode().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Code already exists");
        }
        Project p = new Project();
        p.setCode(dto.getCode().trim());
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        if (dto.getCompanyId() != null) {
            Company c = companyRepository.findById(dto.getCompanyId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));
            p.setCompany(c);
        }
        if (dto.getPersonIds() != null && !dto.getPersonIds().isEmpty()) {
            Set<Person> persons = new HashSet<>(personRepository.findAllById(dto.getPersonIds()));
            if (p.getCompany() != null) {
                persons = persons.stream()
                        .filter(per -> per.getCompany() != null && per.getCompany().getId().equals(p.getCompany().getId()))
                        .collect(Collectors.toSet());
            }
            p.setPersons(persons);
        }
        return new ProjectDTO(projectRepository.save(p));
    }

    @Override
    public ProjectDTO update(Long id, ProjectDTO dto) {
        Project p = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        if (dto.getCode() != null && !dto.getCode().trim().isEmpty()) {
            if (projectRepository.existsByCodeAndIdNot(dto.getCode().trim(), id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Code already exists");
            }
            p.setCode(dto.getCode().trim());
        }
        if (dto.getName() != null) p.setName(dto.getName());
        if (dto.getDescription() != null) p.setDescription(dto.getDescription());
        if (dto.getCompanyId() != null) {
            Company c = companyRepository.findById(dto.getCompanyId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));
            p.setCompany(c);
            if (dto.getPersonIds() != null) {
                Set<Person> persons = new HashSet<>(personRepository.findAllById(dto.getPersonIds()));
                persons = persons.stream()
                        .filter(per -> per.getCompany() != null && per.getCompany().getId().equals(c.getId()))
                        .collect(Collectors.toSet());
                p.setPersons(persons);
            }
        } else if (dto.getPersonIds() != null) {
            Set<Person> persons = new HashSet<>(personRepository.findAllById(dto.getPersonIds()));
            if (p.getCompany() != null) {
                persons = persons.stream()
                        .filter(per -> per.getCompany() != null && per.getCompany().getId().equals(p.getCompany().getId()))
                        .collect(Collectors.toSet());
            }
            p.setPersons(persons);
        }
        return new ProjectDTO(projectRepository.save(p));
    }

    @Override
    public void delete(Long id) {
        Project p = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        projectRepository.delete(p);
    }
}
