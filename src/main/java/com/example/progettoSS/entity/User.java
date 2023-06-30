package com.example.progettoSS.entity;

import lombok.*;

import jakarta.persistence.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")

public class User{
    @Id
    private String id;

    private String name;
    private String surname;
    private String email;
    private String phone;
    private String password;
    private String cpassword;

    private String role;
   

}