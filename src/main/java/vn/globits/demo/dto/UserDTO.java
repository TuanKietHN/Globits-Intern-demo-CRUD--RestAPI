package vn.globits.demo.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import vn.globits.demo.dto.PersonDTO;
import vn.globits.demo.domain.User;

public class UserDTO{
private Long id;

private String email;

private String password;

private boolean isActive;
    private PersonDTO person;
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

        return user;
    }

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

}
