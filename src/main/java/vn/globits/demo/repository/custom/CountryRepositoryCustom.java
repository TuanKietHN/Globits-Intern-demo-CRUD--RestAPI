package vn.globits.demo.repository.custom;

import vn.globits.demo.dto.CountryDTO;
import java.util.List;
import java.util.Optional;

public interface CountryRepositoryCustom {
    List<CountryDTO> findAllDTOs();
    Optional<CountryDTO> findDTOById(Long id);
}

