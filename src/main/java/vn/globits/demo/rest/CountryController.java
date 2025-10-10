package vn.globits.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import vn.globits.demo.dto.CountryDTO;
import vn.globits.demo.service.CountryService;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
public class CountryController {

    @Autowired
    private CountryService countryService;

    // -------------------------------
    // GET: Lấy danh sách tất cả quốc gia
    // -------------------------------
    @GetMapping
    public List<CountryDTO> getAllCountries() {
        return countryService.getAllCountries();
    }

    // -------------------------------
    // GET: Lấy chi tiết theo ID
    // -------------------------------
    @GetMapping("/{id}")
    public CountryDTO getCountryById(@PathVariable Long id) {
        return countryService.getCountryById(id);
    }

    // -------------------------------
    // POST: Tạo mới quốc gia
    // -------------------------------
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CountryDTO createCountry(@RequestBody CountryDTO dto) {
        if (dto.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New country cannot already have an ID");
        }
        return countryService.createCountry(dto);
    }

    // -------------------------------
    // PUT: Cập nhật quốc gia
    // -------------------------------
    @PutMapping("/{id}")
    public CountryDTO updateCountry(@PathVariable Long id, @RequestBody CountryDTO dto) {
        return countryService.updateCountry(id, dto);
    }

    // -------------------------------
    // DELETE: Xóa quốc gia
    // -------------------------------
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCountry(@PathVariable Long id) {
        countryService.deleteCountry(id);
    }
}
