package vn.globits.demo.repository.custom;

import vn.globits.demo.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryCustom {
    List<UserDTO> findAllDTOs();
    Optional<UserDTO> findDTOById(Long id);
}
