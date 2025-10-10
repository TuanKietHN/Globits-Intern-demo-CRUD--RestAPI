package vn.globits.demo.service;

import org.springframework.stereotype.Service;
import vn.globits.demo.dto.CountryDTO;
import java.util.List;
@Service
public interface CountryService {
    List<CountryDTO> getAllCountries();
    CountryDTO getCountryById(Long id);
    CountryDTO createCountry(CountryDTO country);
    CountryDTO updateCountry(Long id, CountryDTO country);
    void deleteCountry(Long id);
}
