package vn.globits.demo.dto;

import vn.globits.demo.domain.Department;

public class DepartmentDTO {
    private Long id;
    private String code;
    private String name;
    private Long parentId;
    private Long companyId;

    public DepartmentDTO() {}

    public DepartmentDTO(Department d) {
        if (d != null) {
            this.id = d.getId();
            this.code = d.getCode();
            this.name = d.getName();
            this.parentId = d.getParent() != null ? d.getParent().getId() : null;
            this.companyId = d.getCompany() != null ? d.getCompany().getId() : null;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
}
