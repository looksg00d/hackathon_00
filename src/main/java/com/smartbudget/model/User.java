package com.smartbudget.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "app_user") // Переименуйте таблицу здесь
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String username;
    @Column
    private String firstName;
    @Column
    private String surname;
    @Column
    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
}
