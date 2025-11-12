package vn.globits.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.globits.demo.domain.Company;
import vn.globits.demo.domain.Department;
import vn.globits.demo.dto.DepartmentDTO;
import vn.globits.demo.repository.CompanyRepository;
import vn.globits.demo.repository.DepartmentRepository;
import vn.globits.demo.service.DepartmentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public List<DepartmentDTO> getAll() {
        return departmentRepository.findAll().stream().map(DepartmentDTO::new).collect(Collectors.toList());
    }

    @Override
    public DepartmentDTO getById(Long id) {
        Department d = departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
        return new DepartmentDTO(d);
    }

    @Override
    public DepartmentDTO create(DepartmentDTO dto) {
        if (dto.getCode() == null || dto.getCode().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code is required");
        }
        if (departmentRepository.existsByCode(dto.getCode().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Code already exists");
        }
        Department d = new Department();
        d.setCode(dto.getCode().trim());
        d.setName(dto.getName());
        if (dto.getCompanyId() != null) {
            Company c = companyRepository.findById(dto.getCompanyId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));
            d.setCompany(c);
        }
        if (dto.getParentId() != null) {
            Department p = departmentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parent department not found"));
            d.setParent(p);
        }
        return new DepartmentDTO(departmentRepository.save(d));
    }

    @Override
    public DepartmentDTO update(Long id, DepartmentDTO dto) {
        Department d = departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
        if (dto.getCode() != null && !dto.getCode().trim().isEmpty()) {
            if (departmentRepository.existsByCodeAndIdNot(dto.getCode().trim(), id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Code already exists");
            }
            d.setCode(dto.getCode().trim());
        }
        if (dto.getName() != null) {
            d.setName(dto.getName());
        }
        if (dto.getCompanyId() != null) {
            Company c = companyRepository.findById(dto.getCompanyId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));
            d.setCompany(c);
        }
        if (dto.getParentId() != null) {
            Department p = departmentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parent department not found"));
            d.setParent(p);
        }
        return new DepartmentDTO(departmentRepository.save(d));
    }

    @Override
    public void delete(Long id) {
        Department d = departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
        departmentRepository.delete(d);
    }

    @Override
    public List<DepartmentDTO> getByCompany(Long companyId) {
        return departmentRepository.findByCompany_Id(companyId).stream().map(DepartmentDTO::new).collect(Collectors.toList());
    }
}
