package vn.globits.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.globits.demo.domain.UserRole;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByUser_Id(Long userId);
    List<UserRole> findByRole_Id(Long roleId);
    void deleteByUser_IdAndRole_Id(Long userId, Long roleId);
    boolean existsByUser_IdAndRole_Id(Long userId, Long roleId);
}
