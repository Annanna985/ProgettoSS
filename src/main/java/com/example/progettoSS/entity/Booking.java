package com.example.progettoSS.entity;

import lombok.*;

import javax.persistence.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "booking")

public class Booking{

    @Id
    private String id;


    private String namePlant;


    private String  idUser;


    private String quantity;



	


}