package vn.globits.demo.service;

import vn.globits.demo.dto.UserRoleDTO;

import java.util.List;

public interface UserRoleService {
    UserRoleDTO save(UserRoleDTO dto);
    List<UserRoleDTO> findAll();
}
