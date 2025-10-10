package vn.globits.demo.service;

import vn.globits.demo.dto.PersonDTO;

import java.util.List;

public interface PersonService {
    List<PersonDTO> getAllPersons();
    PersonDTO getPersonById(Long id);
    PersonDTO createPerson(PersonDTO dto);
    PersonDTO updatePerson(Long id, PersonDTO dto);
    void deletePerson(Long id);
}
