package vn.globits.demo.service.impl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.globits.demo.domain.Company;
import vn.globits.demo.dto.CompanyDTO;
import vn.globits.demo.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import vn.globits.demo.service.CompanyService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class CompanyServiceImpl implements CompanyService{
    @Autowired
    private CompanyRepository companyRepository;


    public List<CompanyDTO> findAll() {
        return companyRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public org.springframework.data.domain.Page<CompanyDTO> getPage(org.springframework.data.domain.Pageable pageable) {
        return companyRepository.findAll(pageable).map(this::toDTO);
    }


    public CompanyDTO findCompanyById(Long id) {
        return companyRepository.findById(id).map(this::toDTO).orElse(null);
    }


    public CompanyDTO createCompany(CompanyDTO dto) {
        Company c = new Company();
        c.setName(dto.getName()); c.setCode(dto.getCode()); c.setAddress(dto.getAddress());
        Company saved = companyRepository.save(c);
        return toDTO(saved);
    }


    public CompanyDTO updateCompany(Long id, CompanyDTO dto) {
        Optional<Company> opt = companyRepository.findById(id);
        if (!opt.isPresent()) return null;
        Company c = opt.get();
        c.setName(dto.getName()); c.setCode(dto.getCode()); c.setAddress(dto.getAddress());
        return toDTO(companyRepository.save(c));
    }


    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));

        if (!company.getPeople().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot delete company: It still has employees (Person)"
            );
        }

        companyRepository.delete(company);
    }


    private CompanyDTO toDTO(Company c) {
        CompanyDTO d = new CompanyDTO();
        d.setId(c.getId()); d.setName(c.getName()); d.setCode(c.getCode()); d.setAddress(c.getAddress());
        return d;
    }
}
