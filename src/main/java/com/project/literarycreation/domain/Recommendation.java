package com.project.literarycreation.domain;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private Long receiverId;
    
    private Integer accepted;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "creation_id")
    private LiteraryCreation creation;

}
