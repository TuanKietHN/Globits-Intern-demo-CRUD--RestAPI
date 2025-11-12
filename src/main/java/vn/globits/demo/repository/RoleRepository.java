package vn.globits.demo.repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.globits.demo.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.globits.demo.dto.RoleDTO;

import java.util.List;
import java.util.Optional;
public interface RoleRepository extends JpaRepository<Role, Long>{
    Optional<Role> findByRole(String role);
    boolean existsByRole(String role);
    boolean existsByRoleAndIdNot(String role, Long id);
}
