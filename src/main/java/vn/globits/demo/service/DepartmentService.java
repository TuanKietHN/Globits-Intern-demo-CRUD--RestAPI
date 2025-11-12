package vn.globits.demo.service;

import vn.globits.demo.dto.DepartmentDTO;

import java.util.List;

public interface DepartmentService {
    List<DepartmentDTO> getAll();
    DepartmentDTO getById(Long id);
    DepartmentDTO create(DepartmentDTO dto);
    DepartmentDTO update(Long id, DepartmentDTO dto);
    void delete(Long id);
    List<DepartmentDTO> getByCompany(Long companyId);
}
