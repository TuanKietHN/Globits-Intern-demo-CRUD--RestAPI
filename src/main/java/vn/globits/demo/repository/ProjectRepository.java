package vn.globits.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.globits.demo.domain.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, Long id);
}
