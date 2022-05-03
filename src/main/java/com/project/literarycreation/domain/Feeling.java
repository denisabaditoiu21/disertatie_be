package com.project.literarycreation.domain;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "feelings")
public class Feeling {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column
    private String name;
}
