package vn.globits.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.globits.demo.dto.TaskDTO;

public interface TaskService {
    Page<TaskDTO> search(Long companyId, Long projectId, Long personId, Integer status, Integer priority, String name, Pageable pageable);
    TaskDTO getById(Long id);
    TaskDTO create(TaskDTO dto);
    TaskDTO update(Long id, TaskDTO dto);
    void delete(Long id);
}
