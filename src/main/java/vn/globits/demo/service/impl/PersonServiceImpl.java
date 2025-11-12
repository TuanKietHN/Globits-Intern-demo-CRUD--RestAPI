package vn.globits.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.globits.demo.domain.Person;
import vn.globits.demo.dto.PersonDTO;
import vn.globits.demo.repository.PersonRepository;
import vn.globits.demo.repository.UserRepository;
import vn.globits.demo.service.PersonService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<PersonDTO> getAllPersons() {
        return personRepository.findAll()
                .stream()
                .map(PersonDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public org.springframework.data.domain.Page<PersonDTO> getPersonPage(org.springframework.data.domain.Pageable pageable) {
        return personRepository.findAll(pageable).map(PersonDTO::new);
    }

    @Override
    public PersonDTO getPersonById(Long id) {
        Person p = personRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found"));
        return new PersonDTO(p);
    }

    @Override
    public PersonDTO createPerson(PersonDTO dto) {
        Person entity = dto.toEntity();
        return new PersonDTO(personRepository.save(entity));
    }

    @Override
    public PersonDTO updatePerson(Long id, PersonDTO dto) {
        Person existing = personRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found"));

        existing.setFullName(dto.getFullName());
        existing.setGender(dto.getGender());
        existing.setBirthDate(dto.getBirthDate());
        existing.setPhoneNumber(dto.getPhoneNumber());
        existing.setAddress(dto.getAddress());

        return new PersonDTO(personRepository.save(existing));
    }

    @Override
    public void deletePerson(Long id) {
        if (!personRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found");
        }
        if (userRepository.existsByPerson_Id(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot delete person: This person is linked to a user account"
            );
        }
        personRepository.deleteById(id);
    }
}
