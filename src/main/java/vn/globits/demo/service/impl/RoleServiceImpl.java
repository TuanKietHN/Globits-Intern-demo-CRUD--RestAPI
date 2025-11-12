package vn.globits.demo.service.impl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.globits.demo.domain.Role;
import vn.globits.demo.domain.User;
import vn.globits.demo.dto.RoleDTO;
import vn.globits.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import vn.globits.demo.service.RoleService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService{
    @Autowired
    private RoleRepository roleRepository;


    public List<RoleDTO> findAllRoles() {
        return roleRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }


    public RoleDTO findRoleById(Long id) {
        return roleRepository.findById(id).map(this::toDTO).orElse(null);
    }


    public RoleDTO createRole(RoleDTO dto) {
        if (dto == null || dto.getRole() == null || dto.getRole().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role is required");
        }
        if (roleRepository.existsByRole(dto.getRole())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Role already exists");
        }
        Role r = new Role();
        r.setRole(dto.getRole().trim());
        r.setDescription(dto.getDescription());
        Role saved = roleRepository.save(r);
        return toDTO(saved);
    }


    public RoleDTO updateRole(Long id, RoleDTO dto) {
        Role r = roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
        if (dto.getRole() != null && !dto.getRole().trim().isEmpty()) {
            if (roleRepository.existsByRoleAndIdNot(dto.getRole().trim(), id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Role already exists");
            }
            r.setRole(dto.getRole().trim());
        }
        r.setDescription(dto.getDescription());
        return toDTO(roleRepository.save(r));
    }


    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
        roleRepository.delete(role);
    }


    private RoleDTO toDTO(Role r) {
        RoleDTO d = new RoleDTO();
        d.setId(r.getId()); d.setRole(r.getRole()); d.setDescription(r.getDescription());
        return d;
    }
}
