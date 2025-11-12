package vn.globits.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.globits.demo.domain.Role;
import vn.globits.demo.domain.User;
import vn.globits.demo.domain.UserRole;
import vn.globits.demo.dto.UserRoleDTO;
import vn.globits.demo.repository.RoleRepository;
import vn.globits.demo.repository.UserRepository;
import vn.globits.demo.repository.UserRoleRepository;
import vn.globits.demo.service.UserRoleService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserRoleDTO save(UserRoleDTO dto) {
        if (dto.getUserId() == null || dto.getRoleId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId and roleId are required");
        }
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found"));
        if (userRoleRepository.existsByUser_IdAndRole_Id(user.getId(), role.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already has this role");
        }
        UserRole entity = new UserRole(user, role);
        return new UserRoleDTO(userRoleRepository.save(entity));
    }

    @Override
    public List<UserRoleDTO> findAll() {
        return userRoleRepository.findAll().stream().map(UserRoleDTO::new).collect(Collectors.toList());
    }
}
