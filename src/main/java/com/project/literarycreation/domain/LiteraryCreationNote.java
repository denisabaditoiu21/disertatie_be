package com.project.literarycreation.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
public class LiteraryCreationNote {


    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private LiteraryCreation literaryCreation;
	
	private Integer note;
}
