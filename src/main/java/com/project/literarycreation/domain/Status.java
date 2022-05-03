package com.project.literarycreation.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
public class Status {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column
    private String name;
}
