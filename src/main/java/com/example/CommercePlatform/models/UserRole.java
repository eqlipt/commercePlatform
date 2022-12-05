package com.example.CommercePlatform.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "role", length = 100, nullable = false, columnDefinition = "varchar(100)")
    @NotEmpty(message = "Введите название роли")
    private String role;

    public UserRole() {
    }

    public UserRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
