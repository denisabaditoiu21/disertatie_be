package com.project.literarycreation.domain;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
public class GroupInvitation {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private Long receiverId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "groupId")
    private Group group;

    @Column
    private Boolean accepted;


}
