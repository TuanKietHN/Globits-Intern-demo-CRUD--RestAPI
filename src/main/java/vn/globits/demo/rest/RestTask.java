package vn.globits.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.globits.demo.dto.TaskDTO;
import vn.globits.demo.service.TaskService;
import vn.globits.demo.service.impl.TaskServiceImpl;

@RestController
@RequestMapping("/api/tasks")
public class RestTask {
    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<Page<TaskDTO>> search(@RequestParam(required = false) Long companyId,
                                                @RequestParam(required = false) Long projectId,
                                                @RequestParam(required = false) Long personId,
                                                @RequestParam(required = false) Integer status,
                                                @RequestParam(required = false) Integer priority,
                                                @RequestParam(required = false) String name,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(taskService.search(companyId, projectId, personId, status, priority, name, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getById(id));
    }

    @PostMapping
    public ResponseEntity<TaskDTO> create(@RequestBody TaskDTO dto) {
        return ResponseEntity.ok(taskService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> update(@PathVariable Long id, @RequestBody TaskDTO dto) {
        return ResponseEntity.ok(taskService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> export(@RequestParam(required = false) Long companyId,
                                         @RequestParam(required = false) Long projectId,
                                         @RequestParam(required = false) Long personId,
                                         @RequestParam(required = false) Integer status,
                                         @RequestParam(required = false) Integer priority,
                                         @RequestParam(required = false) String name) {
        byte[] bytes = ((TaskServiceImpl) taskService).exportExcel(companyId, projectId, personId, status, priority, name);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tasks.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(bytes);
    }
}
