package vn.globits.demo.dto;

import vn.globits.demo.domain.User;
import vn.globits.demo.domain.Role;

import java.util.Set;
import java.util.stream.Collectors;

public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private boolean isActive;
    private PersonDTO person;

    // ✅ thêm mới
    private Set<RoleDTO> roles;

    public UserDTO() {}

    public UserDTO(User entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.email = entity.getEmail();
            this.password = entity.getPassword();
            this.isActive = entity.isActive();

            if (entity.getPerson() != null) {
                this.person = new PersonDTO(entity.getPerson());
            }

            // ✅ ánh xạ Role entity sang RoleDTO
            if (entity.getRoles() != null) {
                this.roles = entity.getRoles().stream()
                        .map(role -> {
                            RoleDTO dto = new RoleDTO();
                            dto.setId(role.getId());
                            dto.setRole(role.getRole());
                            dto.setDescription(role.getDescription());
                            return dto;
                        })
                        .collect(Collectors.toSet());
            }
        }
    }

    public User toEntity() {
        User user = new User();
        if (this.id != null) {
            user.setId(this.id);
        }
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setIsActive(this.isActive);

        if (this.person != null) {
            user.setPerson(this.person.toEntity());
        }

        // ⚠️ phần roles chỉ nên set nếu cần lưu (create/update)
        // nếu bạn chưa có logic đó trong service, có thể bỏ qua
        return user;
    }

    // --- Getter & Setter ---
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

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }
}
