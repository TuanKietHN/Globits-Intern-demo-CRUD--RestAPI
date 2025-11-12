package vn.globits.demo.dto;

import vn.globits.demo.domain.User;
import vn.globits.demo.domain.Role;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private boolean isActive;
    private PersonDTO person;
    private List<RoleDTO> roles; // Danh sách roles của user
    private List<Long> roleIds;  // Danh sách roleIds để assign/update

    // Constructor mặc định
    public UserDTO() {}

    public UserDTO(vn.globits.demo.domain.User u) {
        if (u != null) {
            this.id = u.getId();
            this.email = u.getEmail();
            this.isActive = u.isActive();
            if (u.getPerson() != null) {
                this.person = new PersonDTO(u.getPerson());
            }
            if (u.getRoles() != null) {
                this.roles = u.getRoles().stream().map(RoleDTO::new).collect(Collectors.toList());
            }
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public PersonDTO getPerson() {
        return person;
    }

    public void setPerson(PersonDTO person) {
        this.person = person;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }
}
