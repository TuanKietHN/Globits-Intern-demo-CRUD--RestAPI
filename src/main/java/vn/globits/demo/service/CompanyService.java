package vn.globits.demo.service;
import org.springframework.stereotype.Service;
import vn.globits.demo.dto.CompanyDTO;
import java.util.List;
@Service
public interface CompanyService {
    List<CompanyDTO> findAll();
    CompanyDTO findCompanyById(Long id);
    CompanyDTO createCompany(CompanyDTO company);
    CompanyDTO updateCompany(Long id, CompanyDTO company);
    void deleteCompany(Long id);
}
