package com.project.literarycreation.domain;

import com.project.literarycreation.Utils.RoleName;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private RoleName name;
}
