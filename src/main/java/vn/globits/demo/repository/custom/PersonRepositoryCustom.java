package vn.globits.demo.repository.custom;


import vn.globits.demo.dto.PersonDTO;

import java.util.List;
import java.util.Optional;

public interface PersonRepositoryCustom {
    List<PersonDTO> findAllDTOs();
    Optional<PersonDTO> findDTOById(Long id);
}

