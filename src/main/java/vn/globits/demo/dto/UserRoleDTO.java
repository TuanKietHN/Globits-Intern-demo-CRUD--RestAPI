package vn.globits.demo.dto;

import java.time.LocalDateTime;

public class UserRoleDTO {
    private Long id;
    private Long userId;
    private String userEmail;
    private Long roleId;
    private String roleName;
    private String roleDescription;
    private LocalDateTime assignedDate;
    private Boolean isActive;

    // Constructor mặc định
    public UserRoleDTO() {}

    public UserRoleDTO(vn.globits.demo.domain.UserRole ur) {
        if (ur != null) {
            this.id = ur.getId();
            this.userId = ur.getUser() != null ? ur.getUser().getId() : null;
            this.userEmail = ur.getUser() != null ? ur.getUser().getEmail() : null;
            this.roleId = ur.getRole() != null ? ur.getRole().getId() : null;
            this.roleName = ur.getRole() != null ? ur.getRole().getRole() : null;
            this.roleDescription = ur.getRole() != null ? ur.getRole().getDescription() : null;
            this.assignedDate = ur.getAssignedDate();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public LocalDateTime getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(LocalDateTime assignedDate) {
        this.assignedDate = assignedDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
