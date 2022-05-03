package com.project.literarycreation.domain;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
public class Paragraph {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Lob
    @Column(name = "text", columnDefinition = "LONGTEXT")
    private String text;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "creation_id")
    private LiteraryCreation creation;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "feeling_id")
    private Feeling feeling;
}
