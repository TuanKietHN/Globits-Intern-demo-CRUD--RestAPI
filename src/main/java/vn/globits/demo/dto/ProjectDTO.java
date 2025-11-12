package vn.globits.demo.dto;

import vn.globits.demo.domain.Project;

import java.util.List;
import java.util.stream.Collectors;

public class ProjectDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Long companyId;
    private List<Long> personIds;

    public ProjectDTO() {}

    public ProjectDTO(Project p) {
        if (p != null) {
            this.id = p.getId();
            this.code = p.getCode();
            this.name = p.getName();
            this.description = p.getDescription();
            this.companyId = p.getCompany() != null ? p.getCompany().getId() : null;
            if (p.getPersons() != null) {
                this.personIds = p.getPersons().stream().map(vn.globits.demo.domain.Person::getId).collect(Collectors.toList());
            }
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public List<Long> getPersonIds() { return personIds; }
    public void setPersonIds(List<Long> personIds) { this.personIds = personIds; }
}
