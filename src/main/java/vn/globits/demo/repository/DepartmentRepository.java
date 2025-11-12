package vn.globits.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.globits.demo.domain.Department;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByCompany_Id(Long companyId);
    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, Long id);
}
