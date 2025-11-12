package vn.globits.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.globits.demo.dto.PersonDTO;
import vn.globits.demo.service.CompanyService;
import vn.globits.demo.service.PersonService;

import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/api/persons")
public class RestPerson {

    @Autowired
    private PersonService personService;

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<PersonDTO>> getAllPersons() {
        return ResponseEntity.ok(personService.getAllPersons());
    }

    @GetMapping("/page")
    public ResponseEntity<org.springframework.data.domain.Page<PersonDTO>> getPersonPage(@RequestParam(defaultValue = "0") int page,
                                                                                         @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(personService.getPersonPage(org.springframework.data.domain.PageRequest.of(page, size)));
    }


    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> getPersonById(@PathVariable Long id) {
        PersonDTO dto = personService.getPersonById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PersonDTO> createPerson(@RequestBody PersonDTO dto) {
        PersonDTO created = personService.createPerson(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDTO> updatePerson(@PathVariable Long id, @RequestBody PersonDTO dto) {
        PersonDTO updated = personService.updatePerson(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/avatar")
    public ResponseEntity<PersonDTO> uploadAvatar(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws Exception {
        PersonDTO person = personService.getPersonById(id);
        if (person == null) {
            return ResponseEntity.notFound().build();
        }
        Path uploadDir = Paths.get("uploads/avatars");
        Files.createDirectories(uploadDir);
        String filename = id + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path target = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        person.setAvatar(target.toString());
        PersonDTO updated = personService.updatePerson(id, person);
        return ResponseEntity.ok(updated);
    }
}
