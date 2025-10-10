package vn.globits.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.globits.demo.domain.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
