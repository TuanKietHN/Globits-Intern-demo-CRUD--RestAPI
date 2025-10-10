package vn.globits.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.globits.demo.domain.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
