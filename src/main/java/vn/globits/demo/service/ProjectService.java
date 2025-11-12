package vn.globits.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.globits.demo.dto.ProjectDTO;

public interface ProjectService {
    Page<ProjectDTO> getPage(Pageable pageable);
    ProjectDTO getById(Long id);
    ProjectDTO create(ProjectDTO dto);
    ProjectDTO update(Long id, ProjectDTO dto);
    void delete(Long id);
}
