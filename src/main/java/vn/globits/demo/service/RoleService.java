package vn.globits.demo.service;
import org.springframework.stereotype.Service;
import vn.globits.demo.dto.RoleDTO;
import java.util.List;
@Service
public interface RoleService {
    List<RoleDTO> findAllRoles();
    RoleDTO findRoleById(Long id);
    RoleDTO createRole(RoleDTO role);
    RoleDTO updateRole(Long id, RoleDTO role);
    void deleteRole(Long id);
}
