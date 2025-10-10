package vn.globits.demo.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.globits.demo.domain.Country;
import vn.globits.demo.dto.CountryDTO;
import vn.globits.demo.repository.CountryRepository;
import vn.globits.demo.repository.custom.CountryRepositoryCustom;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CountryServiceImpl implements CountryService, CountryRepositoryCustom {

    @Autowired
    private CountryRepository countryRepository;

    @PersistenceContext
    private EntityManager em;

    // -------------------------------
    // 1️⃣ Custom query: Trả về DTO luôn
    // -------------------------------
    @Override
    public List<CountryDTO> findAllDTOs() {
        List<Country> countries = em.createQuery("SELECT c FROM Country c", Country.class)
                .getResultList();
        return countries.stream().map(CountryDTO::new).collect(Collectors.toList());
    }

    @Override
    public Optional<CountryDTO> findDTOById(Long id) {
        Country c = em.find(Country.class, id);
        return c == null ? Optional.empty() : Optional.of(new CountryDTO(c));
    }

    // -------------------------------
    // 2️⃣ CRUD chính dùng CountryRepository
    // -------------------------------
    @Override
    public List<CountryDTO> getAllCountries() {
        return countryRepository.findAll()
                .stream()
                .map(CountryDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public CountryDTO getCountryById(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Country not found"));
        return new CountryDTO(country);
    }

    @Override
    public CountryDTO createCountry(CountryDTO dto) {
        Country entity = new Country();
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setDescription(dto.getDescription());

        try {
            Country saved = countryRepository.save(entity);
            return new CountryDTO(saved);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Code '" + dto.getCode() + "' already exists!");
        }
    }

    @Override
    public CountryDTO updateCountry(Long id, CountryDTO dto) {
        Country existing = countryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Country not found"));

        existing.setName(dto.getName());
        existing.setCode(dto.getCode());
        existing.setDescription(dto.getDescription());

        try {
            Country updated = countryRepository.save(existing);
            return new CountryDTO(updated);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Code '" + dto.getCode() + "' already exists!");
        }
    }

    @Override
    public void deleteCountry(Long id) {
        if (!countryRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Country not found");
        }
        countryRepository.deleteById(id);
    }
}
