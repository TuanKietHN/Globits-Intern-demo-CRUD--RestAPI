package vn.globits.demo.dto;

import vn.globits.demo.domain.Person;

import java.time.LocalDate;

public class PersonDTO {
    private Long id;
    private String fullName;
    private String gender;
    private LocalDate birthDate;
    private String phoneNumber;
    private String address;

    public PersonDTO() {}

    public PersonDTO(Person entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.fullName = entity.getFullName();
            this.gender = entity.getGender();
            this.birthDate = entity.getBirthDate();
            this.phoneNumber = entity.getPhoneNumber();
            this.address = entity.getAddress();
        }
    }
    public Person toEntity() {
        Person p = new Person();
        if (this.id != null) {
            p.setId(this.id); // chỉ set nếu đang update
        }
        p.setFullName(this.fullName);
        p.setGender(this.gender);
        p.setBirthDate(this.birthDate);
        p.setPhoneNumber(this.phoneNumber);
        p.setAddress(this.address);
        return p;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
