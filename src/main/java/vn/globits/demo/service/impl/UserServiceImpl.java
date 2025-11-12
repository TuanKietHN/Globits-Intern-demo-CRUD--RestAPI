package vn.globits.demo.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.globits.demo.domain.Role;
import vn.globits.demo.domain.User;
import vn.globits.demo.dto.RoleDTO;
import vn.globits.demo.dto.UserDTO;
import vn.globits.demo.repository.RoleRepository;
import vn.globits.demo.repository.UserRepository;
import vn.globits.demo.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return new UserDTO(user);
    }

    @Override
    public UserDTO createUser(UserDTO dto) {
        if (dto == null || dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }
        if (userRepository.existsByEmail(dto.getEmail().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        User entity = new User();
        entity.setEmail(dto.getEmail().trim());
        entity.setPassword(dto.getPassword());
        entity.setIsActive(dto.isActive());
        if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.getRoleIds()));
            entity.setRoles(roles);
        }
        User saved = userRepository.save(entity);
        return new UserDTO(saved);
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO dto) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            if (userRepository.existsByEmail(dto.getEmail().trim()) && !existing.getEmail().equals(dto.getEmail().trim())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
            }
            existing.setEmail(dto.getEmail().trim());
        }
        if (dto.getPassword() != null) {
            existing.setPassword(dto.getPassword());
        }
        existing.setIsActive(dto.isActive());
        if (dto.getRoleIds() != null) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.getRoleIds()));
            existing.clearRoles();
            existing.setRoles(roles);
        }
        User saved = userRepository.save(existing);
        return new UserDTO(saved);
    }

    @Override
    public void deleteUser(Long id) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        userRepository.delete(existing);
    }
}
