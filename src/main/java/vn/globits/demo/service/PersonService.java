package vn.globits.demo.service;

import vn.globits.demo.dto.PersonDTO;

import java.util.List;

public interface PersonService {
    List<PersonDTO> getAllPersons();
    org.springframework.data.domain.Page<PersonDTO> getPersonPage(org.springframework.data.domain.Pageable pageable);
    PersonDTO getPersonById(Long id);
    PersonDTO createPerson(PersonDTO dto);
    PersonDTO updatePerson(Long id, PersonDTO dto);
    void deletePerson(Long id);
}
