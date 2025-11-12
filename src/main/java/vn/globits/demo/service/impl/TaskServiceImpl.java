package vn.globits.demo.service.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.globits.demo.domain.Company;
import vn.globits.demo.domain.Person;
import vn.globits.demo.domain.Project;
import vn.globits.demo.domain.Task;
import vn.globits.demo.dto.TaskDTO;
import vn.globits.demo.repository.CompanyRepository;
import vn.globits.demo.repository.PersonRepository;
import vn.globits.demo.repository.ProjectRepository;
import vn.globits.demo.repository.TaskRepository;
import vn.globits.demo.service.TaskService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private CompanyRepository companyRepository;

    private Specification<Task> buildSpec(Long companyId, Long projectId, Long personId, Integer status, Integer priority, String name) {
        Specification<Task> spec = Specification.where(null);
        if (companyId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.join("project").join("company").get("id"), companyId));
        }
        if (projectId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.join("project").get("id"), projectId));
        }
        if (personId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.join("person").get("id"), personId));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        if (priority != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("priority"), priority));
        }
        if (name != null && !name.trim().isEmpty()) {
            String like = "%" + name.trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), like));
        }
        return spec;
    }

    @Override
    public Page<TaskDTO> search(Long companyId, Long projectId, Long personId, Integer status, Integer priority, String name, Pageable pageable) {
        Specification<Task> spec = buildSpec(companyId, projectId, personId, status, priority, name);
        return taskRepository.findAll(spec, pageable).map(TaskDTO::new);
    }

    @Override
    public TaskDTO getById(Long id) {
        Task t = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        return new TaskDTO(t);
    }

    @Override
    public TaskDTO create(TaskDTO dto) {
        Task t = new Task();
        t.setName(dto.getName());
        t.setDescription(dto.getDescription());
        t.setStartTime(dto.getStartTime());
        t.setEndTime(dto.getEndTime());
        t.setPriority(dto.getPriority() != null ? dto.getPriority() : 1);
        t.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        if (dto.getProjectId() != null) {
            Project p = projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project not found"));
            t.setProject(p);
        }
        if (dto.getPersonId() != null) {
            Person per = personRepository.findById(dto.getPersonId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Person not found"));
            t.setPerson(per);
        }
        return new TaskDTO(taskRepository.save(t));
    }

    @Override
    public TaskDTO update(Long id, TaskDTO dto) {
        Task t = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        if (dto.getName() != null) t.setName(dto.getName());
        if (dto.getDescription() != null) t.setDescription(dto.getDescription());
        if (dto.getStartTime() != null) t.setStartTime(dto.getStartTime());
        if (dto.getEndTime() != null) t.setEndTime(dto.getEndTime());
        if (dto.getPriority() != null) t.setPriority(dto.getPriority());
        if (dto.getStatus() != null) t.setStatus(dto.getStatus());
        if (dto.getProjectId() != null) {
            Project p = projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project not found"));
            t.setProject(p);
        }
        if (dto.getPersonId() != null) {
            Person per = personRepository.findById(dto.getPersonId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Person not found"));
            t.setPerson(per);
        }
        return new TaskDTO(taskRepository.save(t));
    }

    @Override
    public void delete(Long id) {
        Task t = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        taskRepository.delete(t);
    }

    public byte[] exportExcel(Long companyId, Long projectId, Long personId, Integer status, Integer priority, String name) {
        Specification<Task> spec = buildSpec(companyId, projectId, personId, status, priority, name);
        List<Task> tasks = taskRepository.findAll(spec);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Tasks");
            Row header = sheet.createRow(0);
            String[] headers = {"Project", "Description", "Start Time", "End Time", "Priority", "Status", "Person"};
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }
            int rowIdx = 1;
            for (Task t : tasks) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(t.getProject() != null ? Objects.toString(t.getProject().getName(), "") : "");
                row.createCell(1).setCellValue(Objects.toString(t.getDescription(), ""));
                row.createCell(2).setCellValue(t.getStartTime() != null ? t.getStartTime().toString() : "");
                row.createCell(3).setCellValue(t.getEndTime() != null ? t.getEndTime().toString() : "");
                row.createCell(4).setCellValue(t.getPriority());
                row.createCell(5).setCellValue(t.getStatus());
                row.createCell(6).setCellValue(t.getPerson() != null ? Objects.toString(t.getPerson().getFullName(), "") : "");
            }
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to export Excel");
        }
    }
}
