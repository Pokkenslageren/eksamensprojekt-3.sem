package org.example.eksamensprojekt3sem.Member;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

@Entity
@Table(name = "members")
public class MemberModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private long memberId;

    @Column(name = "name")
    @NotBlank(message = "Navn mangler")
    private String name;

    @Column(name = "email")
    @NotBlank(message = "Email mangler")
    private String email;

    @Column(name = "phone")
    @NotBlank(message = "Telefonnummer mangler")
    private String phone;

    @Column(name = "adress")
    @NotBlank(message = "Adresse mangler")
    private String adress;

    @Column(name = "date_of_birth")
    @NotBlank(message = "Dato mangler")
    private Date dateOfBirth;

    public MemberModel() {
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
