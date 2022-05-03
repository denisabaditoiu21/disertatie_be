package com.project.literarycreation.domain;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
public class LiteraryCreation {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] cover;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] pdf;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")
    private User author;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "status_id")
    private Status status;
}
